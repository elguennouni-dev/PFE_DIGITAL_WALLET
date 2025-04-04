package pfe.digitalWallet.core.document;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfe.digitalWallet.core.appuser.AppUser;

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

    @NotNull(message = "File name cannot be Null")
    @NotEmpty(message = "File name cannot be Empty")
    @NotBlank(message = "File name cannot be Blank")
    @Size(min = 5, max = 32, message = "File name must be between 5 and 32 characters")
    private String documentTitle;

    @NotNull(message = "RSAKEY cannot be Null")
    @NotEmpty(message = "RSAKEY cannot be Empty")
    @NotBlank(message = "RSAKEY cannot be Blank")
    @Size(min = 5, max = 55, message = "RSAKEY must be at least 10 to 55 characters")
    private String rsaKey;

    @NotNull(message = "Document creation date-time cannot be Null")
    @PastOrPresent(message = "Document creation date-time cannot be in the future")
    private LocalDateTime createdAt;

    @NotNull(message = "View count cannot be Null")
    @Size(min = 0, message = "View count cannot be less than 0")
    private Integer viewCount;

    @NotNull(message = "View count cannot be Null")
    @Size(min = 0, message = "View count cannot be less than 0")
    private Integer downloadCount;

    @NotNull(message = "File cannot be null")
    @Lob
    @Column(columnDefinition = "BYTEA")
    private Byte[] documentFile;

    @Valid
    @ManyToOne
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUser appUser;
}
