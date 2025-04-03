package pfe.digitalWallet.core.loginhistory;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfe.digitalWallet.core.user.User;

import java.time.LocalDateTime;

@Table(name = "loggin_history")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogginHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long login_history_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime timestamp;
    private String status;
    private String device;
    private String location;

}
