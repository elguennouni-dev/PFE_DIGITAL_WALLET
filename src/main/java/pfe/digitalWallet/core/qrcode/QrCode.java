package pfe.digitalWallet.core.qrcode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import pfe.digitalWallet.core.session.Session;

import java.time.LocalDateTime;

@Table(name = "qr_code")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QrCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String qrCodeData;

    private LocalDateTime generatedAt;

    @OneToOne
    @JoinColumn(name = "session_id", nullable = false)
    @JsonIgnore
    private Session session;

}
