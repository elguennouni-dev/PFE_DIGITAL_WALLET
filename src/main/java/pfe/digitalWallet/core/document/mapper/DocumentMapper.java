package pfe.digitalWallet.core.document.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pfe.digitalWallet.core.document.Document;
import pfe.digitalWallet.core.document.dao.DocumentUploadRequest;
import pfe.digitalWallet.core.document.dto.DocumentDto;
import pfe.digitalWallet.core.document.dto.ExtendedDocumentDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DocumentMapper {

    // @Mapping(source = "id", target = "id")
    DocumentDto toDto(Document document);
    Document toEntity(DocumentDto dto);

    ExtendedDocumentDto toExtendedDto(Document document);
    Document toEntity(ExtendedDocumentDto extendedDto);

}
