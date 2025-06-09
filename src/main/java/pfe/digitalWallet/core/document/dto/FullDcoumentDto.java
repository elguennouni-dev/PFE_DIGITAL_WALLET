package pfe.digitalWallet.core.document.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullDcoumentDto {
    private Long id;
    private String documentTitle;
    private LocalDateTime createdAt;
    private String fileType;
    private String decryptedFileContent; // base64-encoded or plain text
}
