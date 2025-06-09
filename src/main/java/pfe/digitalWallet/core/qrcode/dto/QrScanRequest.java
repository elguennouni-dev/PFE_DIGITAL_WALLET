package pfe.digitalWallet.core.qrcode.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QrScanRequest {
        private String qrCodeData;
        private String username;
}