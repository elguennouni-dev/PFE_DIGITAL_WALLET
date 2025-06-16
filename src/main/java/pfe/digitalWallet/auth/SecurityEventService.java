package pfe.digitalWallet.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.digitalWallet.core.loginattempt.LoginAttempt;
import pfe.digitalWallet.core.loginattempt.LoginAttemptService;
import pfe.digitalWallet.core.loginhistory.LoginHistory;
import pfe.digitalWallet.core.loginhistory.LoginHistoryService;

@Service
public class SecurityEventService {

    @Autowired
    private LoginAttemptService loginAttemptService;
    @Autowired
    private LoginHistoryService loginHistoryService;


    public void saveLoginAttempt(LoginAttempt attempt) {
        if (attempt == null || attempt.getAppUser() == null) {
            throw new IllegalArgumentException("Invalid LoginAttempt data");
        }
        this.loginAttemptService.save(attempt);
    }

    public void saveLoginHistory(LoginHistory history) {
        validateLoginHistory(history);
        try {
            this.loginHistoryService.save(history);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while saving login history for user: " + history.getAppUser().getUsername(), e);
        }
    }

    public void saveLogoutHistory(LoginHistory history) {
        validateLoginHistory(history);
        try {
            this.loginHistoryService.save(history);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while saving logout history for user: " + history.getAppUser().getUsername(), e);
        }
    }

    private void validateLoginHistory(LoginHistory history) {
        if (history == null || history.getAppUser() == null || history.getLoginStatus() == null) {
            throw new IllegalArgumentException("Invalid LoginHistory data");
        }
    }
}