package pfe.digitalWallet.core.appuser.dto;

import lombok.Data;

@Data
public class UpdateUsernameRequest {
    private Long userId;
    private String newUsername;

}