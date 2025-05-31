package pfe.digitalWallet.core.qrcode.dto;

import lombok.Builder;
import lombok.Data;
import pfe.digitalWallet.core.appuser.dto.UserDto;
import pfe.digitalWallet.core.session.dto.SessionDto;

@Data
@Builder
public class QrConfirmationResponse {
    UserDto userDto;
    SessionDto sessionDto;
}
