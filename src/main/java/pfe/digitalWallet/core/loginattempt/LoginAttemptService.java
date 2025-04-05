package pfe.digitalWallet.core.loginattempt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {

    @Autowired
    private LoginAttemptRepo loginAttemptRepo;

    public void save(LoginAttempt loginAttempt) {
        loginAttemptRepo.save(loginAttempt);
    }

}
