package pfe.digitalWallet.core.loginattempt;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfe.digitalWallet.core.appuser.AppUser;
import pfe.digitalWallet.shared.enums.attempt.AttemptStatus;

import java.time.LocalDateTime;

@Table(name = "login_attempt")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Login attempt date-time cannot be Null")
    @PastOrPresent(message = "Login attempt date-time cannot be in the future")
    private LocalDateTime dateTime;

    @NotNull(message = "Attempt status cannot be Null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttemptStatus loginStatus;

    @Valid
    @ManyToOne
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUser appUser;
}
