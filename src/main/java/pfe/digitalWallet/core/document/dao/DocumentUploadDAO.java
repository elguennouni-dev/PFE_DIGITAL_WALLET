package pfe.digitalWallet.core.document.dao;


import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DocumentUploadDAO {

    @NotBlank(message = "File name cannot be blank")
    @Size(min = 5, max = 32, message = "File name must be between 5 and 32 characters")
    private String documentTitle;

    @NotBlank(message = "RSA Key cannot be blank")
    @Size(min = 5, max = 55, message = "RSA Key must be between 5 and 55 characters")
    private String rsaKey;

    @NotNull(message = "App user ID is required")
    private Long appUserId;

    @NotNull(message = "File must not be null")
    private MultipartFile file;
}

