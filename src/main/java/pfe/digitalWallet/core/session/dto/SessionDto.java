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
{}
