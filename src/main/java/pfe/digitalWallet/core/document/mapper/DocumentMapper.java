package pfe.digitalWallet.core.document.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Qualifier;
import pfe.digitalWallet.core.document.Document;
import pfe.digitalWallet.core.document.dto.DocumentDto;

import java.util.List;

@Mapper(componentModel = "spring")
@Qualifier("coreMapper")
public interface DocumentMapper {
    DocumentDto toDocumentDto(Document document);
    Document toDocument(DocumentDto documentDto);
    List<DocumentDto> toDocumentDtoList(List<Document> documents);
}
