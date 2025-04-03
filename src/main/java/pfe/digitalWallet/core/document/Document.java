package pfe.digitalWallet.core.document;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfe.digitalWallet.core.user.User;

import java.time.LocalDateTime;

@Table(name = "document")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String documentTitle;
    private String documentFile;
    private String rsaKey;
    private LocalDateTime createdAt;
    private Long viewCount;
    private Long downloadCount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
