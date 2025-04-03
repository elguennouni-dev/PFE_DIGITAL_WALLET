package pfe.digitalWallet.core.qrcode;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfe.digitalWallet.core.session.Session;

import java.time.LocalDateTime;

@Table(name = "qr_codes")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QRcode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qr_code_id;

    @JoinColumn(name = "session_id")
    private Session session;

    private String qr_code_data;
    private LocalDateTime generated_at;

}
