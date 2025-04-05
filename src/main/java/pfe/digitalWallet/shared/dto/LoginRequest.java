package pfe.digitalWallet.shared.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginRequest {
    private String username;
    private String password;

    private String device;
    private String location;

}
