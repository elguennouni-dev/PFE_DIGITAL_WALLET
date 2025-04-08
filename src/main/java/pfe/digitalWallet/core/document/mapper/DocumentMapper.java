package pfe.digitalWallet.core.document.mapper;

import org.mapstruct.Mapper;
import pfe.digitalWallet.core.document.Document;
import pfe.digitalWallet.core.document.dto.DocumentDto;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    DocumentDto toDto(Document document);
    Document toEntity(DocumentDto documentDto);
}
