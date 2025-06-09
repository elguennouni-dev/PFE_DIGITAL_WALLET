package pfe.digitalWallet.core.document.dao;


import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DocumentUploadRequest {
    private String documentTitle;
    private String username;
    private MultipartFile file;
    private String fileType;
}
