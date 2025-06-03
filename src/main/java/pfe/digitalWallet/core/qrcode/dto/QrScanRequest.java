package pfe.digitalWallet.core.qrcode.dto;

public record QrScanRequest (
        String qrCodeData,
        String username
) {
}
