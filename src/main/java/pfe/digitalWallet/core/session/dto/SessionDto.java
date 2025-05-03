package pfe.digitalWallet.core.session.dto;

import pfe.digitalWallet.core.appuser.dto.UserDto;
import pfe.digitalWallet.shared.enums.session.SessionStatus;

import java.time.LocalDateTime;


public record SessionDto(
        Long id,
        UserDto user,
        String token,
        SessionStatus status,
        LocalDateTime createdAt
)
{
    public SessionDto withStatus(SessionStatus newStatus) {
        return new SessionDto(id, user, token, newStatus, createdAt);
    }

    public SessionDto withId(Long newId) {
        return new SessionDto(newId, user, token, status, createdAt);
    }

    public SessionDto withUser(UserDto newUser) {
        return new SessionDto(id, newUser, token, status, createdAt);
    }

    public SessionDto withToken(String newToken) {
        return new SessionDto(id, user, newToken, status, createdAt);
    }

    public SessionDto withCreatedAt(LocalDateTime newCreatedAt) {
        return new SessionDto(id, user, token, status, newCreatedAt);
    }
}