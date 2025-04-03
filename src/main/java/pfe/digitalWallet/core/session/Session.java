package pfe.digitalWallet.core.session;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfe.digitalWallet.core.qrcode.QrCode;
import pfe.digitalWallet.core.user.User;
import pfe.digitalWallet.shared.enums.session.SessionStatus;

import java.time.LocalDateTime;

@Table(name = "session")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sessionToken;
    private SessionStatus sessionStatus;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(mappedBy = "session", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private QrCode qrCode;
}
