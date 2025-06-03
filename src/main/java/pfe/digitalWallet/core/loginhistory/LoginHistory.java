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

    @Column(nullable = true)
    private String ipAddress;

//    @NotNull(message = "Login History date-time cannot be Null")
    @PastOrPresent(message = "Login History date-time cannot be in the future")
    private LocalDateTime dateTime;

    private String device;
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoginStatus loginStatus;

    private String reason;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;
}
