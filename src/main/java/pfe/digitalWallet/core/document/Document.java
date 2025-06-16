package pfe.digitalWallet.core.document;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import pfe.digitalWallet.core.appuser.AppUser;

import java.time.LocalDateTime;

@Table(name = "document")
@Entity
@Data
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "File name cannot be Null")
    @NotEmpty(message = "File name cannot be Empty")
    @NotBlank(message = "File name cannot be Blank")
    @Size(min = 1, max = 180, message = "File name must be between 1 and 180 characters")
    private String documentTitle;


    private String fileType;

    @NotNull(message = "RSAKEY cannot be Null")
    @NotEmpty(message = "RSAKEY cannot be Empty")
    @NotBlank(message = "RSAKEY cannot be Blank")
    @Size(min = 10, max = 512, message = "RSAKEY must be at least 10 to 55 characters")
    private String rsaKey;

    @NotNull(message = "Document creation date-time cannot be Null")
    @PastOrPresent(message = "Document creation date-time cannot be in the future")
    private LocalDateTime createdAt;

    @NotNull(message = "View count cannot be Null")
    @Min(value = 0, message = "View count cannot be less than 0")
    private Integer viewCount;

    @NotNull(message = "Download count cannot be Null")
    @Min(value = 0, message = "Download count cannot be less than 0")
    private Integer downloadCount;

    @Column(name = "document_file", nullable = false)
    private byte[] documentFile;

    @Valid
    @ManyToOne
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUser appUser;
}
