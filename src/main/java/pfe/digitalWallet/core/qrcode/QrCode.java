package pfe.digitalWallet.core.qrcode;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfe.digitalWallet.core.session.Session;

import java.time.LocalDateTime;

@Table(name = "qr_code")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String qrCodeData;
    private LocalDateTime generatedAt;

    @OneToOne
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

}
