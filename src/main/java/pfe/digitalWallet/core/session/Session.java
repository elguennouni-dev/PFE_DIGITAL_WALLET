package pfe.digitalWallet.core.session;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import pfe.digitalWallet.core.qrcode.QrCode;
import pfe.digitalWallet.core.appuser.AppUser;
import pfe.digitalWallet.shared.enums.session.SessionStatus;

import java.time.LocalDateTime;

@Table(name = "session")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Session tocken date-time cannot be Null")
    @NotEmpty(message = "Session tocken date-time cannot be Empty")
    @NotBlank(message = "Session tocken date-time cannot be Blank")
    private String sessionToken;

    @NotNull(message = "Session creation date-time cannot be Null")
    @NotEmpty(message = "Session creation date-time cannot be Empty")
    @NotBlank(message = "Session creation date-time cannot be Blank")
    @PastOrPresent(message = "Session creation date-time cannot be in the future")
    private LocalDateTime createdAt;

    @NotNull(message = "Session expiration date-time cannot be Null")
    @NotEmpty(message = "Session expiration date-time cannot be Empty")
    @NotBlank(message = "Session expiration date-time cannot be Blank")
    private LocalDateTime expiresAt;

    @NotNull(message = "Session status cannot be Null")
    @NotEmpty(message = "Session status cannot be Empty")
    @NotBlank(message = "Session status cannot be Blank")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus sessionStatus;

    @Valid
    @ManyToOne
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUser appUser;

    @Valid
    @OneToOne(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private QrCode qrCode;
}
