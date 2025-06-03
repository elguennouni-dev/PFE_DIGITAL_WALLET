package pfe.digitalWallet.core.loginhistory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pfe.digitalWallet.core.loginhistory.dto.LoginHistoryDto;
import pfe.digitalWallet.core.loginhistory.mapper.LoginHistoryMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoginHistoryService {

    @Autowired
    private LoginHistoryRepo loginHistoryRepo;
    @Autowired
    private LoginHistoryMapper loginHistoryMapper;

    public void save(LoginHistory loginHistory) {
        loginHistoryRepo.save(loginHistory);
    }

    public List<LoginHistoryDto> getAllByUserId(Long id) {
        return loginHistoryRepo.findAllByAppUserIdOrderByDateTimeDesc(id)
                .stream()
                .map(loginHistoryMapper::toDto)
                .collect(Collectors.toList());
    }

    public LoginHistory findLastLoginHistory(Long id) {
        Optional<LoginHistory> loginHistory = loginHistoryRepo.findFirstByAppUserIdOrderByDateTimeDesc(id);
        return loginHistory.get();
    }



}
