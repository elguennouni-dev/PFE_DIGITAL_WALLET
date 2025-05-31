package pfe.digitalWallet.core.session.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pfe.digitalWallet.core.session.Session;
import pfe.digitalWallet.core.session.dto.SessionDto;

@Mapper(componentModel = "spring")
public interface SessionMapper {

    SessionMapper INSTANCE = Mappers.getMapper(SessionMapper.class);

    SessionDto toDto(Session session);

    // You can add more mappings if needed
}

