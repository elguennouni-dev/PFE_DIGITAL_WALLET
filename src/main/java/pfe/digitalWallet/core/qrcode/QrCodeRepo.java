package pfe.digitalWallet.core.qrcode;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QrCodeRepo extends JpaRepository<QrCode, Long> {
}
