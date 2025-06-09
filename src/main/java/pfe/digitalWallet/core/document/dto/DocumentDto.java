package pfe.digitalWallet.core.document.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pfe.digitalWallet.core.appuser.AppUser;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDto {

    private Long id;
    private String documentTitle;
    private LocalDateTime createdAt;
    private String fileType;

}