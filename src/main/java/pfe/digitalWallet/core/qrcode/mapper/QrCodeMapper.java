package pfe.digitalWallet.core.qrcode.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pfe.digitalWallet.core.qrcode.QrCode;
import pfe.digitalWallet.core.qrcode.dto.QrCodeDTO;

@Mapper(componentModel = "spring")
public interface QrCodeMapper {

    @Mappings({
            @Mapping(source = "session.id", target = "sessionId"),
            @Mapping(source = "qrCodeData", target = "qrToken"),
            @Mapping(source = "session.expiresAt", target = "expiresAt")
    })
    QrCodeDTO toDto(QrCode qrCode);
}
