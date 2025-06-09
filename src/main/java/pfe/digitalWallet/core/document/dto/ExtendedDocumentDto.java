package pfe.digitalWallet.core.document.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExtendedDocumentDto extends DocumentDto {

    private String decryptedFileContent;

    public ExtendedDocumentDto(Long id, String documentTitle, LocalDateTime createdAt, String fileType, String decryptedFileContent) {
        super(id, documentTitle, createdAt, fileType);
        this.decryptedFileContent = decryptedFileContent;
    }
}