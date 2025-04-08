package pfe.digitalWallet.core.appuser.mapper;

import org.mapstruct.Mapper;
import pfe.digitalWallet.core.appuser.AppUser;
import pfe.digitalWallet.core.appuser.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(AppUser user);
    AppUser toEntity(UserDto dto);
}
