package pfe.digitalWallet.core.qrcode;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import pfe.digitalWallet.core.session.Session;

import java.time.LocalDateTime;

@Table(name = "qr_code")
@Entity
@Data
@Builder
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
