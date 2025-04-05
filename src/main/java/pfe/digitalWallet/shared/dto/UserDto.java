package pfe.digitalWallet.shared.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import pfe.digitalWallet.core.appuser.AppUser;

import java.sql.Date;
import java.time.LocalDateTime;

@Getter @Setter
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private SessionDto session;

    private String token;

    public static UserDto from(AppUser user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

}
