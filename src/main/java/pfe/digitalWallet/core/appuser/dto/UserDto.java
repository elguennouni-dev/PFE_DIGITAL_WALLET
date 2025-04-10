package pfe.digitalWallet.core.appuser.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pfe.digitalWallet.core.appuser.AppUser;

import java.time.LocalDateTime;

import java.time.LocalDateTime;

public record UserDto (
        Long id,
        String username,
        String email,
        String token,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean isLocked,
        LocalDateTime lockUntil
) {
    public UserDto withUsername(String newUsername) {
        return new UserDto(this.id, newUsername, this.email, this.token, this.createdAt, this.updatedAt, this.isLocked, this.lockUntil);
    }

    public UserDto withEmail(String newEmail) {
        return new UserDto(this.id, this.username, newEmail, this.token, this.createdAt, this.updatedAt, this.isLocked, this.lockUntil);
    }

    public UserDto withToken(String newToken) {
        return new UserDto(this.id, this.username, this.email, newToken, this.createdAt, this.updatedAt, this.isLocked, this.lockUntil);
    }

    public UserDto withCreatedAt(LocalDateTime newCreatedAt) {
        return new UserDto(this.id, this.username, this.email, this.token, newCreatedAt, this.updatedAt, this.isLocked, this.lockUntil);
    }

    public UserDto withUpdatedAt(LocalDateTime newUpdatedAt) {
        return new UserDto(this.id, this.username, this.email, this.token, this.createdAt, newUpdatedAt, this.isLocked, this.lockUntil);
    }

    public UserDto withIsLocked(boolean newIsLocked) {
        return new UserDto(this.id, this.username, this.email, this.token, this.createdAt, this.updatedAt, newIsLocked, this.lockUntil);
    }

    public UserDto withLockUntil(LocalDateTime newLockUntil) {
        return new UserDto(this.id, this.username, this.email, this.token, this.createdAt, this.updatedAt, this.isLocked, newLockUntil);
    }
}