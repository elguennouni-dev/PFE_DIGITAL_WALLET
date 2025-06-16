package pfe.digitalWallet.core.loginattempt;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pfe.digitalWallet.core.appuser.AppUser;
import pfe.digitalWallet.core.appuser.UserRepository;
import pfe.digitalWallet.core.loginattempt.dto.LoginAttemptDto;
import pfe.digitalWallet.core.loginattempt.mapper.LoginAttemptMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoginAttemptService {

    @Autowired
    private LoginAttemptRepo loginAttemptRepo;

    @Autowired private LoginAttemptMapper loginAttemptMapper;

    @Autowired private UserRepository userRepository;

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
        return loginAttemptRepo.findTopByAppUserOrderByDateTimeDesc(user);
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







    // REAL REAL REAL FUNCTIONS
    public ResponseEntity<?> getAllById(Long userId) {
        if(userRepository.findById(userId).isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No user found with this ID: " + userId));

        List<LoginAttempt> loginAttemptList = loginAttemptRepo.findAllByAppUser_Username(userRepository.findById(userId).get().getUsername());

        if(loginAttemptList.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No login attempts at the moment"));

        List<LoginAttemptDto> loginAttemptDtos = loginAttemptList.stream()
                .map(loginAttemptMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(loginAttemptDtos);
    }

}
