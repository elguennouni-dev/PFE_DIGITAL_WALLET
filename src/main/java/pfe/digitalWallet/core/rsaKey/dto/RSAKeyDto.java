package pfe.digitalWallet.core.rsaKey.dto;

import java.time.LocalDateTime;

public record RSAKeyDto(
        String publicKey,
        String privateKeyEncrypted,
        LocalDateTime createdAt
) {}
