package pfe.digitalWallet.core.loginhistory;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import pfe.digitalWallet.core.appuser.AppUser;
import pfe.digitalWallet.shared.enums.login.LoginStatus;

import java.time.LocalDateTime;

@Table(name = "login_history")
@Entity
@Data
public class LoginHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Login History date-time cannot be Null")
    @PastOrPresent(message = "Login History date-time cannot be in the future")
    private LocalDateTime dateTime;

    @NotNull(message = "device cannot be Null")
    @NotEmpty(message = "device cannot be Empty")
    @NotBlank(message = "device cannot be Blank")
    @Size(min = 2, message = "device must be at least 2 characters long")
    private String device; // Suggested to send (No-Device-found)

    @NotNull(message = "location cannot be Null")
    @NotEmpty(message = "location cannot be Empty")
    @NotBlank(message = "location cannot be Blank")
    @Size(min = 2, message = "location must be at least 2 characters long") // Suggested to send (No-location-found)
    private String location;

    @NotNull(message = "Login status status cannot be Null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoginStatus loginStatus;

    @Valid
    @ManyToOne
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUser appUser;
}
