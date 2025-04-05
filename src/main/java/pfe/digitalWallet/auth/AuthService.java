package pfe.digitalWallet.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

    public AuthService(JwtUtil jwtUtil,UserService userService, PasswordValidator passwordValidator, SecurityEventService securityEventService, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.passwordValidator = passwordValidator;
        this.securityEventService = securityEventService;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserDto> login(LoginRequest request) {
        // Login datetime
        LocalDateTime time = LocalDateTime.now();

        // Username not exist
        Optional<AppUser> appUserOptional = userService.getByUsername(request.getUsername());
        if (appUserOptional.isEmpty()) {
            return Optional.empty();
        }

        AppUser appUser = appUserOptional.get();

        // Password incorrect
        if (!passwordValidator.isValidPassword(request.getPassword(), appUser.getId())) {
            // Save login attempt
            LoginAttempt attempt = new LoginAttempt();
            attempt.setAppUser(appUser);
            attempt.setDateTime(time);
            attempt.setLoginStatus(AttemptStatus.FAILURE);
            securityEventService.saveLoginAttempt(attempt);
            return Optional.empty();
        }

        // Generate token
        String token = jwtUtil.generateToken(appUser.getUsername());
        appUser.setToken(token);
        UserDto userDto = UserDto.from(appUser);



        // Save login history
        LoginHistory history = new LoginHistory();
        history.setDevice(request.getDevice());
        history.setLocation(request.getLocation());
        history.setAppUser(appUser);
        history.setDateTime(time);
        history.setLoginStatus(LoginStatus.LOGGED_IN);
        securityEventService.saveLoginHistory(history);

        return Optional.of(userDto);
    }



    // Logout communication facade
//    public void logout(LogoutRequest request) {
//        securityEventService.logSuccessfulLogout(request.getUsername());
//    }





    public Optional<UserDto> signup(SignupRequest request) {
        // Signup datetime
        LocalDateTime time = LocalDateTime.now();

        // Username taken
        Optional<AppUser> usernameExist = userService.getByUsername(request.getUsername());
        if (usernameExist.isPresent()) {
            return Optional.empty();
        }

        // Email taken
        Optional<AppUser> emailExist = userService.findByEmail(request.getEmail());
        if (emailExist.isPresent()) {
            return Optional.empty();
        }

        // Generate token
        String token = jwtUtil.generateToken(request.getUsername());

        // Password encoding
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        AppUser appUser = new AppUser();
        appUser.setUsername(request.getUsername());
        appUser.setEmail(request.getEmail());
        appUser.setCreatedAt(time);
        appUser.setUpdatedAt(time);
        appUser.setToken(token);
        appUser.setPassword(encodedPassword);

        Optional<AppUser> savedUser = userService.save(appUser);

        if (savedUser.isEmpty()) {
            return Optional.empty();
        }

        UserDto userDto = UserDto.from(savedUser.get());

        return Optional.of(userDto);

    }


}
