package pfe.digitalWallet.core.document.mapper;

import org.mapstruct.Mapper;
import pfe.digitalWallet.core.document.Document;
import pfe.digitalWallet.core.document.dto.DocumentDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    DocumentDto toDocumentDto(Document document);
    Document toDocument(DocumentDto documentDto);
    List<DocumentDto> toDocumentDtoList(List<Document> documents);
}
