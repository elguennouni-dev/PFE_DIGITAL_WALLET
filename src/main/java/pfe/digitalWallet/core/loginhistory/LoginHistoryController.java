package pfe.digitalWallet.core.loginhistory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pfe.digitalWallet.core.loginhistory.dto.LoginHistoryDto;
import pfe.digitalWallet.core.loginhistory.mapper.LoginHistoryMapper;

import java.util.List;

@RestController
@RequestMapping("/login-history")
public class LoginHistoryController {

    @Autowired
    private LoginHistoryService loginHistoryService;
    @Autowired
    private LoginHistoryMapper loginHistoryMapper;


    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserLoginHistory(@PathVariable Long userId) {
        return loginHistoryService.getAllByUserId(userId);
    }

//    @GetMapping("/user/id/{userId}/last")
//    public ResponseEntity<LoginHistoryDto> getLastLoginAttempt(@PathVariable Long userId) {
//        return ResponseEntity.ok(loginHistoryMapper.toDto(loginHistoryService.findLastLoginHistory(userId)));
//    }

}
