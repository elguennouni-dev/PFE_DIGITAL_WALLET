package pfe.digitalWallet.core.appuser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import pfe.digitalWallet.core.document.Document;
import pfe.digitalWallet.core.loginattempt.LoginAttempt;
import pfe.digitalWallet.core.loginhistory.LoginHistory;
import pfe.digitalWallet.core.rsaKey.RSAKey;
import pfe.digitalWallet.core.session.Session;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "app_user")
@Entity
@Getter
@Setter
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String username;

    private String password;

    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean isLocked;

    private LocalDateTime lockUntil;

    @Valid
    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions = new ArrayList<>();

    @Valid
    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LoginAttempt> loginAttempts = new ArrayList<>();

    @Valid
    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents = new ArrayList<>();

    @Valid
    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LoginHistory> loginHistories = new ArrayList<>();

}
