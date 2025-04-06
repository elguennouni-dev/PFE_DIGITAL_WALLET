package pfe.digitalWallet.auth;

import org.springframework.stereotype.Service;
import pfe.digitalWallet.core.loginattempt.LoginAttempt;
import pfe.digitalWallet.core.loginattempt.LoginAttemptService;
import pfe.digitalWallet.core.loginhistory.LoginHistory;
import pfe.digitalWallet.core.loginhistory.LoginHistoryService;
import pfe.digitalWallet.shared.enums.login.LoginStatus;

@Service
public class SecurityEventService {

    private final LoginAttemptService loginAttemptService;
    private final LoginHistoryService loginHistoryService;

    public SecurityEventService(LoginAttemptService loginAttemptService, LoginHistoryService loginHistoryService) {
        this.loginAttemptService = loginAttemptService;
        this.loginHistoryService = loginHistoryService;
    }

    // Handle attempts
    public void saveLoginAttempt(LoginAttempt attempt) {
        this.loginAttemptService.save(attempt);
    }

    // Handle histories
    public void saveLoginHistory(LoginHistory history) {
        this.loginHistoryService.save(history);
    }

    // Handle logouts
    public void saveLogoutHistory(LoginHistory history) {
        this.loginHistoryService.save(history);
    }


//    public void logSuccessfulLogout(String username) {
//        LoginHistory history = loginHistoryService.getByUser_Username(username)
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//        history.setLoginStatus(LoginStatus.LOGGED_OUT);
//        loginHistoryService.save(history);
//    }


}
