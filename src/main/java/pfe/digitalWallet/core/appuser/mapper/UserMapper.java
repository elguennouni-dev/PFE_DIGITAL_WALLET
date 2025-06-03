package pfe.digitalWallet.core.appuser.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Qualifier;
import pfe.digitalWallet.core.appuser.AppUser;
import pfe.digitalWallet.core.appuser.dto.UserDto;

@Mapper(componentModel = "spring")
@Qualifier("coreMapper")
public interface UserMapper {
//    @Mapping(target = "token", ignore = true)
    UserDto toDto(AppUser user);
    AppUser toEntity(UserDto dto);
}
