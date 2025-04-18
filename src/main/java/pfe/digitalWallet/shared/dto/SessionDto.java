package pfe.digitalWallet.shared.dto;

import lombok.Getter;
import lombok.Setter;
import pfe.digitalWallet.shared.enums.session.SessionStatus;

import java.time.LocalDateTime;

@Getter @Setter
public class SessionDto {
    private Long id;
    private UserDto user;
    private String token;
    private SessionStatus status;
    private LocalDateTime createdAt;
}
