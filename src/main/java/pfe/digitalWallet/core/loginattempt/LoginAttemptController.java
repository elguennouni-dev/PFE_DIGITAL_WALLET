package pfe.digitalWallet.core.loginattempt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login-att")
public class LoginAttemptController {

    @Autowired private LoginAttemptService loginAttemptService;



    @GetMapping("/{userId}")
    public ResponseEntity<?> getAllLoginAttemptsById(@PathVariable Long userId) {
        return loginAttemptService.getAllById(userId);
    }


}
