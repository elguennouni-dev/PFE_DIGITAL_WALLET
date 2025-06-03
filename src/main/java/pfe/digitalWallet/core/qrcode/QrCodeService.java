package pfe.digitalWallet.core.qrcode;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.digitalWallet.auth.jwt.JwtUtil;
import pfe.digitalWallet.configuration.WebSocketConfig;
import pfe.digitalWallet.core.appuser.AppUser;
import pfe.digitalWallet.core.appuser.UserRepository;
import pfe.digitalWallet.core.appuser.dto.UserDto;
import pfe.digitalWallet.core.appuser.mapper.UserMapper;
import pfe.digitalWallet.core.qrcode.dao.QrConfirmationDAO;
import pfe.digitalWallet.core.qrcode.dto.QrCodeDTO;
import pfe.digitalWallet.core.qrcode.dto.QrConfirmationResponse;
import pfe.digitalWallet.core.qrcode.mapper.QrCodeMapper;
import pfe.digitalWallet.core.session.Session;
import pfe.digitalWallet.core.session.SessionRepo;
import pfe.digitalWallet.core.session.dto.SessionDto;
import pfe.digitalWallet.core.session.mapper.SessionMapper;
import pfe.digitalWallet.exception.NotFoundException;
import pfe.digitalWallet.shared.enums.session.SessionStatus;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QrCodeService {

    @Autowired
    private final QrCodeRepo qrCodeRepo;
    @Autowired
    private final SessionRepo sessionRepo;
    @Autowired
    private final QrCodeMapper qrCodeMapper;
    @Autowired
    private final UserRepository userRepo;
    @Autowired
    private final JwtUtil jwtUtil;
    @Autowired
    private final UserMapper userMapper;
    @Autowired
    private final SessionMapper sessionMapper;

    @Autowired
    private WebSocketConfig webSocketConfig;


    public QrCode generateQrCode() {
        Session session = Session.builder()
                .sessionToken(UUID.randomUUID().toString())
                .sessionStatus(SessionStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .appUser(null)
                .build();

        sessionRepo.save(session);


        QrCode qrCode = QrCode.builder()
                .qrCodeData(UUID.randomUUID().toString())
                .generatedAt(LocalDateTime.now())
                .session(session)
                .build();

        qrCodeRepo.save(qrCode);

        return qrCode;

    }



//    public void confirmQrCodeLogin(String qrCodeData, AppUser appUser) {
//        QrCode qrCode = qrCodeRepo.findByQrCodeData(qrCodeData)
//                .orElseThrow(() -> new RuntimeException("QR code not found"));
//
//        Session session = qrCode.getSession();
//        session.setAppUser(appUser);
//        session.setSessionStatus(SessionStatus.AUTHENTICATED);
//
//        sessionRepo.save(session);
//    }

    public void confirmQrCodeLogin(String qrCodeData, AppUser appUser) {
        QrCode qrCode = qrCodeRepo.findByQrCodeData(qrCodeData)
                .orElseThrow(() -> new RuntimeException("QR code not found"));

        Session session = qrCode.getSession();
        session.setAppUser(appUser);
        session.setSessionStatus(SessionStatus.AUTHENTICATED);

        sessionRepo.save(session);

        String jwt = jwtUtil.generateToken(appUser.getUsername());
        webSocketConfig.confirmQrSession(qrCodeData, jwt);
    }




    public Optional<String> getJwtIfAuthenticated(String qrCodeData) {
        QrCode qrCode = qrCodeRepo.findByQrCodeData(qrCodeData)
                .orElseThrow(() -> new RuntimeException("QR code not found"));

        Session session = qrCode.getSession();

        if (session.getSessionStatus() == SessionStatus.AUTHENTICATED && session.getAppUser() != null) {
            return Optional.of(jwtUtil.generateToken(session.getAppUser().getUsername()));
        }
        return Optional.empty();
    }


}
