package pfe.digitalWallet.core.qrcode;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.digitalWallet.core.appuser.UserRepository;
import pfe.digitalWallet.core.qrcode.dto.QrCodeDTO;
import pfe.digitalWallet.core.qrcode.mapper.QrCodeMapper;
import pfe.digitalWallet.core.session.Session;
import pfe.digitalWallet.core.session.SessionRepo;
import pfe.digitalWallet.shared.enums.session.SessionStatus;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QrCodeService {

    @Autowired
    private final QrCodeRepo qrCodeRepository;
    @Autowired
    private final SessionRepo sessionRepository;
    @Autowired
    private final QrCodeMapper qrCodeMapper;
    @Autowired
    private final UserRepository userRepository;

    public QrCodeDTO initSession(String seed) {

            Session session = Session.builder()
                    .sessionToken(String.valueOf(UUID.nameUUIDFromBytes(seed.getBytes(StandardCharsets.UTF_8))))
                    .createdAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusHours(1))
                    .sessionStatus(SessionStatus.PENDING)
                    .appUser(null)
                    .build();

            QrCode qrCode = QrCode.builder()
                    .qrCodeData(UUID.randomUUID().toString())
                    .generatedAt(LocalDateTime.now())
                    .session(session)
                    .build();

            session.setQrCode(qrCode);

            session = sessionRepository.save(session);

            return QrCodeDTO.builder()
                    .sessionId(session.getId())
                    .qrToken(qrCode.getQrCodeData())
                    .expiresAt(session.getExpiresAt())
                    .build();
    }




    public Object confirmLogin(String qrTocken) {
        return null;
    }

    public Object pollSession(Long sessionId) {
        return null;
    }

    public Object expire(Long sessionId) {
        return null;
    }
}
