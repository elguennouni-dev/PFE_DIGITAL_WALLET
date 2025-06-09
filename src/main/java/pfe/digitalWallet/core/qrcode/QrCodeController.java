package pfe.digitalWallet.core.qrcode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfe.digitalWallet.core.appuser.AppUser;
import pfe.digitalWallet.core.appuser.UserService;
import pfe.digitalWallet.core.qrcode.dao.QrConfirmationDAO;
import pfe.digitalWallet.core.qrcode.dto.QrScanRequest;

import java.util.Map;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "http://localhost:5500")
@RequestMapping("/qr")
public class QrCodeController {
    @Autowired
    private QrCodeService qrCodeService;
    @Autowired
    private UserService userService;

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
            return ResponseEntity.ok(Map.of("token", jwt.get()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
