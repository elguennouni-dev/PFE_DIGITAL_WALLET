package pfe.digitalWallet.core.loginhistory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pfe.digitalWallet.core.appuser.AppUser;
import pfe.digitalWallet.core.appuser.UserRepository;
import pfe.digitalWallet.core.loginhistory.dto.LoginHistoryDto;
import pfe.digitalWallet.core.loginhistory.mapper.LoginHistoryMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoginHistoryService {

    @Autowired
    private LoginHistoryRepo loginHistoryRepo;
    @Autowired
    private LoginHistoryMapper loginHistoryMapper;
    @Autowired
    private UserRepository userRepository;

    public void save(LoginHistory loginHistory) {
        loginHistoryRepo.save(loginHistory);
    }

    public ResponseEntity<?> getAllByUserId(Long id) {
        Optional<AppUser> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No user with this ID: " + id));
        }

        AppUser user = userOptional.get();

        List<LoginHistory> loginHistories = loginHistoryRepo.findAllByAppUser_Username(user.getUsername());

        if (loginHistories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No login history at the moment"));
        }

        List<LoginHistoryDto> loginHistoryDtos = loginHistories.stream()
                .map(loginHistoryMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(loginHistoryDtos);
    }







//    public LoginHistory findLastLoginHistory(Long id) {
//        Optional<LoginHistory> loginHistory = loginHistoryRepo.findFirstByAppUserIdOrderByDateTimeDesc(id);
//        return loginHistory.get();
//    }



}
