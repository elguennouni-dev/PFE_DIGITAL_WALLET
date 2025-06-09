package pfe.digitalWallet.core.document.dao;

import lombok.Data;

@Data
public class DocumentUpdateRequest {
    private String newDocumentTitle;
    private String username;
    // In future will add more details...
}
