package pfe.digitalWallet.core.qrcode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/qr")
public class QrCodeController {
    @Autowired
    private QrCodeService qrCodeService;

    @PostMapping("/init-session")
    public ResponseEntity<?> initSession(@RequestBody String seed){
        return ResponseEntity.ok(qrCodeService.initSession(seed));
    }

    @PostMapping("/confirm-login")
    public ResponseEntity<?> confirmLogin(@RequestBody String qrToken, @RequestBody String jwt){
        return ResponseEntity.ok(qrCodeService.confirmLogin(qrToken, jwt));
    }

    @PostMapping("/web/poll-session")
    public ResponseEntity<?> pollSession(@RequestBody Long sessionId){
        return ResponseEntity.ok(qrCodeService.pollSession(sessionId));
    }

    @PostMapping("/session/expire")
    public ResponseEntity<?> expire(@RequestBody Long sessionId){
        return ResponseEntity.ok(qrCodeService.expire(sessionId));
    }
}
