package pfe.digitalWallet.auth;

import org.springframework.stereotype.Service;
import pfe.digitalWallet.core.appuser.UserService;
import pfe.digitalWallet.core.loginhistory.LoginHistoryService;
import pfe.digitalWallet.shared.validation.PasswordValidator;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordValidator passwordValidator;
    private final SecurityEventService securityEventService;
    private final LoginHistoryService loginHistoryService;

    public AuthService(UserService userService, PasswordValidator passwordValidator, SecurityEventService securityEventService, LoginHistoryService loginHistoryService) {
        this.userService = userService;
        this.passwordValidator = passwordValidator;
        this.securityEventService = securityEventService;
        this.loginHistoryService = loginHistoryService;
    }


    // Login communication facade
    public boolean login(String username, String password) {
        if(!passwordValidator.isValid(password)) {
            securityEventService.logFailedLogin(username);
            return false;
        }

        securityEventService.logSuccessfulLogin(username);
        loginHistoryService.recordLogin(username);
        return true;
    }


    // Logout communication facade
    public void logout(String username) {
        securityEventService.logSuccessfulLogout(username);
        loginHistoryService.recordLogout(username);
    }

}
