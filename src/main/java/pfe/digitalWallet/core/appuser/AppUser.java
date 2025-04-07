package pfe.digitalWallet.core.appuser;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import pfe.digitalWallet.core.document.Document;
import pfe.digitalWallet.core.loginattempt.LoginAttempt;
import pfe.digitalWallet.core.loginhistory.LoginHistory;
import pfe.digitalWallet.core.session.Session;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "app_user")
@Entity
@Data
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Username cannot be Null")
    @NotEmpty(message = "Username cannot be Empty")
    @NotBlank(message = "Username cannot be Blank")
    @Size(min = 6, max = 20, message = "Username must be between 6 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username must be alphanumeric")
    private String username;

    @NotNull(message = "Password cannot be Null")
    @NotEmpty(message = "Password cannot be Empty")
    @NotBlank(message = "Password cannot be Blank")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    private String password;

    @NotNull(message = "Password cannot be Null")
    @NotEmpty(message = "Password cannot be Empty")
    @NotBlank(message = "Password cannot be Blank")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Creation date-time cannot be Null")
    @PastOrPresent(message = "Creation date-time cannot be in the future")
    private LocalDateTime createdAt;

    @NotNull(message = "Update date-time cannot be Null")
    @PastOrPresent(message = "Update date-time cannot be in the future")
    private LocalDateTime updatedAt;

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


    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", sessions=" + sessions +
                ", loginAttempts=" + loginAttempts +
                ", documents=" + documents +
                ", loginHistories=" + loginHistories +
                '}';
    }
}
