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

    @NotNull(message = "QRcode data cannot be Null")
    @NotEmpty(message = "QRcode data cannot be Empty")
    @NotBlank(message = "QRcode data cannot be Blank")
    @Size(min = 2, message = "QRcode data must be at least 2 characters long")
    private String qrCodeData;

    @NotNull(message = "QRcode generation date-time cannot be Null")
    @PastOrPresent(message = "QRcode generation date-time cannot be in the future")
    private LocalDateTime generatedAt;

    @Valid
    @OneToOne
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

}
