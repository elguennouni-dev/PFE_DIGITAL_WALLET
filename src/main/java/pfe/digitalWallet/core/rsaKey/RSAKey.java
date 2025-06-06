package pfe.digitalWallet.core.rsaKey;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pfe.digitalWallet.core.appuser.AppUser;

import java.time.LocalDateTime;

@Entity
@Table(name = "rsa_key")
@Getter
@Setter
public class RSAKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private AppUser user;

    @Lob
    @Column(name = "public_key", nullable = false)
    @JsonIgnore
    private String publicKey;

    @Lob
    @Column(name = "private_key", nullable = false)
    @JsonIgnore
    private String privateKeyEncrypted;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}