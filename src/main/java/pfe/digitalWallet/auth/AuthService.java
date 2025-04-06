package pfe.digitalWallet.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pfe.digitalWallet.auth.jwt.JwtBlacklistService;
import pfe.digitalWallet.auth.jwt.JwtUtil;
import pfe.digitalWallet.core.appuser.AppUser;
import pfe.digitalWallet.core.appuser.UserService;
import pfe.digitalWallet.core.loginattempt.LoginAttempt;
import pfe.digitalWallet.core.loginhistory.LoginHistory;
import pfe.digitalWallet.shared.dto.LoginRequest;
import pfe.digitalWallet.shared.dto.LogoutRequest;
import pfe.digitalWallet.shared.dto.SignupRequest;
import pfe.digitalWallet.shared.dto.UserDto;
import pfe.digitalWallet.shared.enums.attempt.AttemptStatus;
import pfe.digitalWallet.shared.enums.login.LoginStatus;
import pfe.digitalWallet.shared.validation.PasswordValidator;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PasswordValidator passwordValidator;
    private final SecurityEventService securityEventService;
    private final PasswordEncoder passwordEncoder;

    private final JwtBlacklistService jwtBlacklistService;

    public AuthService(
            JwtUtil jwtUtil,
            UserService userService,
            PasswordValidator passwordValidator,
            SecurityEventService securityEventService,
            PasswordEncoder passwordEncoder,
            JwtBlacklistService jwtBlacklistService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.passwordValidator = passwordValidator;
        this.securityEventService = securityEventService;
        this.passwordEncoder = passwordEncoder;
        this.jwtBlacklistService = jwtBlacklistService;
    }

    public Optional<UserDto> login(LoginRequest request) {
        LocalDateTime time = LocalDateTime.now();

        // Check if user exists
        Optional<AppUser> appUserOptional = userService.getByUsername(request.getUsername());
        if (appUserOptional.isEmpty()) {
            return Optional.empty();
        }

        AppUser appUser = appUserOptional.get();

        try {
            // Check password securely
            if (!passwordValidator.isValidPassword(request.getPassword(), appUser.getId())) {
                try {
                    LoginAttempt attempt = new LoginAttempt();
                    attempt.setAppUser(appUser);
                    attempt.setDateTime(time);
                    attempt.setLoginStatus(AttemptStatus.FAILURE);
                    securityEventService.saveLoginAttempt(attempt);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return Optional.empty();
            }

            try {
                // Generate JWT token
                String token = jwtUtil.generateToken(appUser.getUsername());

                // Convert user to DTO and set token
                UserDto userDto = UserDto.from(appUser);
                userDto.setToken(token);

                // Save login history
                try {
                    LoginHistory history = new LoginHistory();
                    history.setDevice(request.getDevice());
                    history.setLocation(request.getLocation());
                    history.setAppUser(appUser);
                    history.setDateTime(time);
                    history.setLoginStatus(LoginStatus.LOGGED_IN);
                    securityEventService.saveLoginHistory(history);
                } catch (Exception e) {
                    e.printStackTrace();

                }

                return Optional.of(userDto);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error during token generation", e);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error during login process", e);
        }
    }



    public Optional<UserDto> signup(SignupRequest request) {
        LocalDateTime time = LocalDateTime.now();

        // Check if username exists
        Optional<AppUser> existingUsername = userService.getByUsername(request.getUsername());
        if (existingUsername.isPresent()) {
            return Optional.empty();
        }

        // Check if email exists
        Optional<AppUser> existingEmail = userService.findByEmail(request.getEmail());
        if (existingEmail.isPresent()) {
            return Optional.empty();
        }

        try {
            // Encode password
            String encodedPassword = passwordEncoder.encode(request.getPassword());

            AppUser appUser = new AppUser();
            appUser.setUsername(request.getUsername());
            appUser.setEmail(request.getEmail());
            appUser.setCreatedAt(time);
            appUser.setUpdatedAt(time);
            appUser.setPassword(encodedPassword);

            // Save user
            Optional<AppUser> savedUser = userService.save(appUser);

            if (savedUser.isEmpty()) {
                return Optional.empty();
            }

            try {
                // Generate JWT token
                String token = jwtUtil.generateToken(request.getUsername());

                UserDto userDto = UserDto.from(savedUser.get());
                userDto.setToken(token);

                return Optional.of(userDto);
            } catch (Exception e) {
                e.printStackTrace();
                userService.deleteById(savedUser.get().getId());
                throw new RuntimeException("Error during token generation", e);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error during signup process", e);
        }
    }


    public void logout(LogoutRequest request) {
        String token = request.getToken();

        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token is empty or invalid");
        }

        String username = jwtUtil.getUsernameFromToken(token);

        Optional<AppUser> user = userService.getByUsername(username);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        LoginHistory history = new LoginHistory();
        history.setAppUser(user.get());
        history.setDateTime(LocalDateTime.now());
        history.setLoginStatus(LoginStatus.LOGGED_OUT);
        securityEventService.saveLogoutHistory(history);

        this.jwtBlacklistService.blacklistToken(token);
    }









}



