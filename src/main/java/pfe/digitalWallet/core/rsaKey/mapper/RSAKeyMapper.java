package pfe.digitalWallet.core.rsaKey.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Qualifier;
import pfe.digitalWallet.core.rsaKey.RSAKey;
import pfe.digitalWallet.core.rsaKey.dto.RSAKeyDto;

@Mapper(componentModel = "spring")
@Qualifier("coreMapper")
public interface RSAKeyMapper {
    RSAKey toRSAKeyEntity(RSAKeyDto rsaKeyDto);
    RSAKeyDto toRSAKeyDTO(RSAKey key);
}
