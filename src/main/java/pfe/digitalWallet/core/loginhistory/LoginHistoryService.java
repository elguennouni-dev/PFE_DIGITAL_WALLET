package pfe.digitalWallet.core.loginhistory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginHistoryService {

    @Autowired
    private LoginHistoryRepo loginHistoryRepo;

    public void save(LoginHistory loginHistory) {
        loginHistoryRepo.save(loginHistory);
    }

    public Optional<LoginHistory> getByUser_Username(String username) {
        Optional<LoginHistory> history = loginHistoryRepo.findByAppUser_Username(username);
        return history;
    }


}
