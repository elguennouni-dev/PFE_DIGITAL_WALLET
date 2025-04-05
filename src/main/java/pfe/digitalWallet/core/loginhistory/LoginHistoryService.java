package pfe.digitalWallet.core.loginhistory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginHistoryService {

    @Autowired
    private LoginHistoryRepo loginHistoryRepo;

    public void save(LoginHistory loginHistory) {
        loginHistoryRepo.save(loginHistory);
    }

}
