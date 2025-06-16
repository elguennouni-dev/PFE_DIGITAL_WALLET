package pfe.digitalWallet.core.qrcode;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfe.digitalWallet.auth.jwt.JwtUtil;
import pfe.digitalWallet.core.appuser.AppUser;
import pfe.digitalWallet.core.appuser.UserService;
import pfe.digitalWallet.core.qrcode.dao.QrConfirmationDAO;
import pfe.digitalWallet.core.qrcode.dto.QrScanRequest;
import pfe.digitalWallet.exception.UnauthorizedException;
import pfe.digitalWallet.shared.dto.LoginResponse;

import java.util.Map;
import java.util.Optional;


@Slf4j
@RestController
@RequestMapping("/qr")
public class QrCodeController {
    @Autowired
    private QrCodeService qrCodeService;
    @Autowired
    private UserService userService;
    @Autowired private JwtUtil jwtUtil;

    @GetMapping("/create")
    public ResponseEntity<?> initSession(){
        return ResponseEntity.ok(qrCodeService.generateQrCode());
    }


    @PostMapping("/scan")
    public ResponseEntity<?> scanQrCode(@RequestBody QrScanRequest request) {
        return qrCodeService.confirmQrCodeLogin(request);
    }

    @GetMapping("/status/{qrCodeData}")
    public ResponseEntity<?> checkStatus(@PathVariable String qrCodeData) {
        Optional<String> jwt = qrCodeService.getJwtIfAuthenticated(qrCodeData);
        if (jwt.isPresent()) {
            AppUser user = userService.getByUsername(jwtUtil.getUsernameFromToken(jwt.get()))
                    .orElseThrow(() -> new UnauthorizedException("JWT username not found"));
            return ResponseEntity.ok(Map.of(
                    "data", Map.of(
                    "id",Long.toString(user.getId()),
                    "username",user.getUsername(),
                    "email",user.getEmail(),
                    "token",jwt.get())
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
