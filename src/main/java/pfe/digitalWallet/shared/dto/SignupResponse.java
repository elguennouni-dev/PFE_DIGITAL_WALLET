package pfe.digitalWallet.shared.dto;

import java.time.LocalDateTime;

public record SignupResponse(
        Long id,
        String username,
        String email,
        String token,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean isLocked,
        LocalDateTime lockUntil
) {}