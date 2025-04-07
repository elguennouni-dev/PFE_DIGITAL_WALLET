package pfe.digitalWallet.core.appuser.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pfe.digitalWallet.core.appuser.AppUser;

import java.time.LocalDateTime;

public record UserDto (
        Long id,
        String username,
        String email,
        String token,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public UserDto withUsername(String newUsername) {
        return new UserDto(this.id, newUsername, this.email, this.token, this.createdAt, this.updatedAt);
    }

    public UserDto withEmail(String newEmail) {
        return new UserDto(this.id, this.username, newEmail, this.token, this.createdAt, this.updatedAt);
    }

    public UserDto withCreatedAt(LocalDateTime newCreatedAt) {
        return new UserDto(this.id, this.username, this.email, this.token, newCreatedAt, this.updatedAt);
    }

    public UserDto withUpdatedAt(LocalDateTime newUpdatedAt) {
        return new UserDto(this.id, this.username, this.email, this.token, this.createdAt, newUpdatedAt);
    }

    public UserDto withToken(String newToken) {
        return new UserDto(this.id, this.username, this.email, newToken, this.createdAt, this.updatedAt);
    }

}