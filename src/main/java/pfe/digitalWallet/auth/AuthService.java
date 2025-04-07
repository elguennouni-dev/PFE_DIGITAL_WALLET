package pfe.digitalWallet.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pfe.digitalWallet.auth.jwt.JwtBlacklistService;
import pfe.digitalWallet.auth.jwt.JwtUtil;
import pfe.digitalWallet.core.appuser.AppUser;
import pfe.digitalWallet.core.appuser.UserService;
import pfe.digitalWallet.core.appuser.dao.LoginRequest;
import pfe.digitalWallet.core.appuser.dto.UserDto;
import pfe.digitalWallet.core.loginattempt.LoginAttempt;
import pfe.digitalWallet.core.loginhistory.LoginHistory;
import pfe.digitalWallet.shared.dto.LogoutRequest;
import pfe.digitalWallet.shared.dto.SignupRequest;
import pfe.digitalWallet.shared.enums.attempt.AttemptStatus;
import pfe.digitalWallet.shared.enums.login.LoginStatus;
import pfe.digitalWallet.shared.mapper.UserMapper;
import pfe.digitalWallet.shared.validation.PasswordValidator;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PasswordValidator passwordValidator;
    private final SecurityEventService securityEventService;
    private final PasswordEncoder passwordEncoder;
    private final JwtBlacklistService jwtBlacklistService;
    private final UserMapper userMapper;


    public Optional<UserDto> login(LoginRequest request) {
        LocalDateTime time = LocalDateTime.now();

        Optional<AppUser> appUserOptional = userService.getByUsername(request.username());
        if (appUserOptional.isEmpty()) {
            return Optional.empty();
        }

        AppUser appUser = appUserOptional.get();

        if (!passwordValidator.isValidPassword(request.password(), appUser.getId())) {
            // Log failed login attempt
            logLoginAttempt(appUser, time, AttemptStatus.FAILURE);
            return Optional.empty();
        }

        try {
            String token = jwtUtil.generateToken(appUser.getUsername());
            UserDto userDto = userMapper.toDto(appUser);
            userDto.withToken(token);
            // Log successful login attempt
            logLoginHistory(request, appUser, time, LoginStatus.LOGGED_IN);
            return Optional.of(userDto);
        } catch (Exception e) {
            handleException("Error during token generation", e);
        }
        return Optional.empty();
    }

    public Optional<UserDto> signup(SignupRequest request) {
        LocalDateTime time = LocalDateTime.now();

        Optional<AppUser> existingUsername = userService.getByUsername(request.username());
        if (existingUsername.isPresent()) {
            return Optional.empty();
        }

        Optional<AppUser> existingEmail = userService.findByEmail(request.email());
        if (existingEmail.isPresent()) {
            return Optional.empty();
        }

        try {
            String encodedPassword = passwordEncoder.encode(request.password());

            AppUser appUser = new AppUser();
            appUser.setUsername(request.username());
            appUser.setEmail(request.email());
            appUser.setCreatedAt(time);
            appUser.setUpdatedAt(time);
            appUser.setPassword(encodedPassword);

            Optional<AppUser> savedUser = userService.save(appUser);
            if (savedUser.isEmpty()) {
                return Optional.empty();
            }

            String token = jwtUtil.generateToken(request.username());
            UserDto userDto = userMapper.toDto(savedUser.get());
            userDto.withToken(token);

            return Optional.of(userDto);
        } catch (Exception e) {
            handleException("Error during signup process", e);
        }
        return Optional.empty();
    }

    public void logout(LogoutRequest request) {
        String token = request.token();

        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token is empty or invalid");
        }

        String username = jwtUtil.getUsernameFromToken(token);

        Optional<AppUser> user = userService.getByUsername(username);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        // Log logout attempt
        logLogoutHistory(user.get(), token);
        jwtBlacklistService.blacklistToken(token);
    }

    private void logLoginAttempt(AppUser appUser, LocalDateTime time, AttemptStatus status) {
        try {
            LoginAttempt attempt = new LoginAttempt();
            attempt.setAppUser(appUser);
            attempt.setDateTime(time);
            attempt.setLoginStatus(status);
            securityEventService.saveLoginAttempt(attempt);
        } catch (Exception e) {
            handleException("Error during login attempt logging", e);
        }
    }

    private void logLoginHistory(LoginRequest request, AppUser appUser, LocalDateTime time, LoginStatus status) {
        try {
            LoginHistory history = new LoginHistory();
            history.setDevice(request.device());
            history.setLocation(request.location());
            history.setAppUser(appUser);
            history.setDateTime(time);
            history.setLoginStatus(status);
            securityEventService.saveLoginHistory(history);
        } catch (Exception e) {
            handleException("Error during login history logging", e);
        }
    }

    private void logLogoutHistory(AppUser appUser, String token) {
        try {
            LoginHistory history = new LoginHistory();
            history.setAppUser(appUser);
            history.setDateTime(LocalDateTime.now());
            history.setLoginStatus(LoginStatus.LOGGED_OUT);
            securityEventService.saveLogoutHistory(history);
        } catch (Exception e) {
            handleException("Error during logout history logging", e);
        }
    }

    private void handleException(String message, Exception e) {
        e.printStackTrace();  // This could be replaced with a more specific error handling mechanism if needed
        throw new RuntimeException(message, e);
    }
}

