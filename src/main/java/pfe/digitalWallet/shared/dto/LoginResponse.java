package pfe.digitalWallet.shared.dto;

public record LoginResponse(
        Long id,
        String username,
        String email,
        String token
) {}
