package pfe.digitalWallet.core.loginattempt.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Qualifier;
import pfe.digitalWallet.core.loginattempt.LoginAttempt;
import pfe.digitalWallet.core.loginattempt.dto.LoginAttemptDto;

@Mapper(componentModel = "spring")
@Qualifier("coreMapper")
public interface LoginAttemptMapper {
    LoginAttemptDto toDto(LoginAttempt loginAttempt);
    LoginAttempt toEntity(LoginAttemptDto loginAttemptDto);
}
