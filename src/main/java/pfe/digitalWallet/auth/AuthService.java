package pfe.digitalWallet.auth;

import org.springframework.stereotype.Service;
import pfe.digitalWallet.auth.jwt.JwtUtil;
import pfe.digitalWallet.core.appuser.AppUser;
import pfe.digitalWallet.core.appuser.UserService;
import pfe.digitalWallet.core.loginhistory.LoginHistoryService;
import pfe.digitalWallet.shared.dto.UserDto;
import pfe.digitalWallet.shared.validation.PasswordValidator;

import java.util.Optional;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PasswordValidator passwordValidator;
    private final SecurityEventService securityEventService;
    private final LoginHistoryService loginHistoryService;

    public AuthService(JwtUtil jwtUtil,UserService userService, PasswordValidator passwordValidator, SecurityEventService securityEventService, LoginHistoryService loginHistoryService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.passwordValidator = passwordValidator;
        this.securityEventService = securityEventService;
        this.loginHistoryService = loginHistoryService;
    }


    // Login communication facade
//    public boolean login(String username, String password) {
//        if(!passwordValidator.isValid(password)) {
//            securityEventService.logFailedLogin(username);
//            return false;
//        }
//
//        securityEventService.logSuccessfulLogin(username);
//        loginHistoryService.recordLogin(username);
//        return true;
//    }

    public Optional<UserDto> login(String username, String password) {
        Optional<AppUser> appUserOptional = userService.findByUsername(username);
        if (appUserOptional.isEmpty()) {
            return Optional.empty();
        }

        AppUser appUser = appUserOptional.get();
        if (!passwordValidator.isValidPassword(password, appUser.getId())) {
            return Optional.empty();
        }

        String token = jwtUtil.generateToken(appUser.getUsername());
        UserDto userDto = UserDto.from(appUser);
        userDto.setToken(token);

        return Optional.of(userDto);
    }



    // Logout communication facade
    public void logout(String username) {
        securityEventService.logSuccessfulLogout(username);
        loginHistoryService.recordLogout(username);
    }

}
