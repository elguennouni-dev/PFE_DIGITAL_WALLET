package pfe.digitalWallet.core.qrcode;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QrCodeRepo extends JpaRepository<QrCode, Long> {
    Optional<QrCode> findByQrCodeData(String qrToken);
}