package pfe.digitalWallet.core.session;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfe.digitalWallet.core.qrcode.QRcode;
import pfe.digitalWallet.shared.enums.session.SessionStatus;

import java.time.LocalDateTime;

@Table(name = "sessions")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long session_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Long user_id;

    private String session_token;
    private SessionStatus status;
    private LocalDateTime created_at;
    private LocalDateTime expires_at;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private QRcode qRcode;

}
