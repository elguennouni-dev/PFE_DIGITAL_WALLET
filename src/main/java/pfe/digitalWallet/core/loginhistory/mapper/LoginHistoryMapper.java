package pfe.digitalWallet.core.loginhistory.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Qualifier;
import pfe.digitalWallet.core.loginhistory.LoginHistory;
import pfe.digitalWallet.core.loginhistory.dto.LoginHistoryDto;

@Mapper(componentModel = "spring")
@Qualifier("coreMapper")
public interface LoginHistoryMapper {
    LoginHistoryDto toDto(LoginHistory loginHistory);
    LoginHistory toEntity(LoginHistoryDto loginHistoryDto);
}
