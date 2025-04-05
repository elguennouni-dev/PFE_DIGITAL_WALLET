package pfe.digitalWallet.auth;

import org.springframework.stereotype.Service;
import pfe.digitalWallet.auth.jwt.JwtUtil;
import pfe.digitalWallet.core.appuser.AppUser;
import pfe.digitalWallet.core.appuser.UserService;
import pfe.digitalWallet.core.loginattempt.LoginAttempt;
import pfe.digitalWallet.core.loginhistory.LoginHistory;
import pfe.digitalWallet.core.loginhistory.LoginHistoryService;
import pfe.digitalWallet.shared.dto.UserDto;
import pfe.digitalWallet.shared.enums.attempt.AttemptStatus;
import pfe.digitalWallet.shared.enums.login.LoginStatus;
import pfe.digitalWallet.shared.validation.PasswordValidator;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PasswordValidator passwordValidator;
    private final SecurityEventService securityEventService;

    public AuthService(JwtUtil jwtUtil,UserService userService, PasswordValidator passwordValidator, SecurityEventService securityEventService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.passwordValidator = passwordValidator;
        this.securityEventService = securityEventService;
    }

    public Optional<UserDto> login(String username, String password, String device, String location) {
        // Login datetime
        LocalDateTime time = LocalDateTime.now();

        Optional<AppUser> appUserOptional = userService.findByUsername(username);
        if (appUserOptional.isEmpty()) {
            return Optional.empty();
        }

        AppUser appUser = appUserOptional.get();
        if (!passwordValidator.isValidPassword(password, appUser.getId())) {
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
        UserDto userDto = UserDto.from(appUser);
        userDto.setToken(token);


        // Save login history
        LoginHistory history = new LoginHistory();
        history.setDevice(device);
        history.setLocation(location);
        history.setAppUser(appUser);
        history.setDateTime(time);
        history.setLoginStatus(LoginStatus.LOGGED_IN);
        securityEventService.saveLoginHistory(history);

        return Optional.of(userDto);
    }



    // Logout communication facade
    public void logout(String username) {
        securityEventService.logSuccessfulLogout(username);
    }

}
