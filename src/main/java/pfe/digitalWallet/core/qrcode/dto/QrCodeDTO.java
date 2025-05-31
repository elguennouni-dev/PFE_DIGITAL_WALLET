package pfe.digitalWallet.core.qrcode.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class QrCodeDTO {
    private Long sessionId;
    private String qrToken;
    private LocalDateTime expiresAt;
}
