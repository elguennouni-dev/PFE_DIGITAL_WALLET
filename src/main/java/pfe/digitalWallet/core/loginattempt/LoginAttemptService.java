package pfe.digitalWallet.core.loginattempt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pfe.digitalWallet.core.appuser.AppUser;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LoginAttemptService {

    @Autowired
    private LoginAttemptRepo loginAttemptRepo;

    // Future Updates and Features:
    /*
        1. Failed Attempts Storage:
            - To improve performance, we will use cashing solutions like Redis or NoSQL database (e.g., MongoDB)
            for temporarily storing failed login attempts instead of relying on traditional relational database.

        2. Temporary Account locking:
            - After a specified number of failed attempts (e.g., 5), implement temporary account locking.
            This can be done by adding a `lockedUntil` filed in the `AppUser` entity, which stores the
            timestamp of when the account should be unlocked. The login system should check this field before allowing further login attempts.
     */

    @Async
    public void incrementFailedAttempts(AppUser user) {
        LoginAttempt attempt = getByUser(user);
        int failedAttempts = attempt != null ? attempt.getFailedAttempts() : 0;

        long delay = Math.min(failedAttempts * 5, 60);

        if(failedAttempts > 0) {
            try {
                System.out.println("Waiting for " + delay + " seconds before retrying...");
                Thread.sleep(delay * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Lock the account after 5 failed attempts
        if(failedAttempts >= 5) {
            throw new RuntimeException("Your account is temporarily locked due to multiple failed login attempts.");
        }

        if(attempt == null) {
            attempt = new LoginAttempt();
            attempt.setAppUser(user);
            attempt.setFailedAttempts(1);
            attempt.setLastFailedAttempt(LocalDateTime.now());
        } else {
            attempt.setFailedAttempts(attempt.getFailedAttempts() + 1);
            attempt.setLastFailedAttempt(LocalDateTime.now());
        }

        save(attempt);
    }

    public LoginAttempt getByUser(AppUser user) {
        return loginAttemptRepo.findByAppUser(user);
    }

    public void resetFailedAttempts(AppUser user) {
        LoginAttempt attempt = getByUser(user);
        if(attempt != null ) {
            attempt.setFailedAttempts(0);
            attempt.setLastFailedAttempt(null);
            save(attempt);
        }
    }

    public void save(LoginAttempt loginAttempt) {
        loginAttemptRepo.save(loginAttempt);
    }

}
