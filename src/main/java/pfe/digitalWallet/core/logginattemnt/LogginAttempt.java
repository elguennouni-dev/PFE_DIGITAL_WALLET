package pfe.digitalWallet.core.logginattemnt;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfe.digitalWallet.core.user.User;
import pfe.digitalWallet.shared.enums.attempt.AttemptStatus;

import java.time.LocalDateTime;

@Table(name = "loggin_attempts")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogginAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attempt_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime timestamp;
    private AttemptStatus status;

}
