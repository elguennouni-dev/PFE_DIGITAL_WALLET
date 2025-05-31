package pfe.digitalWallet.core.qrcode.dao;

import lombok.Data;

@Data
public class QrConfirmationDAO {
    String qrToken;
    String jwt;
}
