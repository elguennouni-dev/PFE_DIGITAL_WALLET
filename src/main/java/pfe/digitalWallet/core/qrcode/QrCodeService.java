package pfe.digitalWallet.core.qrcode;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.digitalWallet.auth.jwt.JwtUtil;
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

    public QrCodeDTO initSession(String seed) {

        UUID uuid = UUID.nameUUIDFromBytes(seed.getBytes(StandardCharsets.UTF_8));

        Session session = Session.builder()
                .sessionToken(null)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(1))
                .sessionStatus(SessionStatus.PENDING)
                .appUser(null)
                .build();

            QrCode qrCode = QrCode.builder()
                    .qrCodeData(uuid.toString())
                    .generatedAt(LocalDateTime.now())
                    .session(session)
                    .build();

            session.setQrCode(qrCode);

            session = sessionRepo.save(session);

            return QrCodeDTO.builder()
                    .sessionId(session.getId())
                    .qrToken(qrCode.getQrCodeData())
                    .expiresAt(session.getExpiresAt())
                    .build();
    }




    public QrConfirmationResponse confirmLogin(QrConfirmationDAO dao) {

        QrCode qrCode = qrCodeRepo.findByQrCodeData(dao.getQrToken())
                .orElseThrow(() -> new NotFoundException("QR code not found"));

        Session session = sessionRepo.findByQrCodeId(qrCode.getId())
                .orElseThrow(() -> new NotFoundException("Session not found"));

        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Session expired");
        }

        String username = jwtUtil.getUsernameFromToken(dao.getJwt());
        AppUser appUser = Optional.ofNullable(userRepo.findByUsername(username))
                .orElseThrow(() -> new NotFoundException("User not found"));
        // Update session using a fluent builder-style update (see below)
        session = Session.builder()
                .appUser(appUser)
                .sessionStatus(SessionStatus.AUTHENTICATED)
                .sessionToken(dao.getJwt())
                .build();

        sessionRepo.save(session);

        return QrConfirmationResponse.builder()
                .userDto(userMapper.toDto(appUser))
                .sessionDto(sessionMapper.toDto(session))
                .build();
    }


    public Object pollSession(Long sessionId) {
        return null;
    }

    public Object expire(Long sessionId) {
        return null;
    }
}
