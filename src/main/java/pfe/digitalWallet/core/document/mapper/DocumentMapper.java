package pfe.digitalWallet.core.document.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pfe.digitalWallet.core.document.Document;
import pfe.digitalWallet.core.document.dao.DocumentUploadDAO;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    DocumentMapper INSTANCE = Mappers.getMapper(DocumentMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "viewCount", constant = "0")
    @Mapping(target = "downloadCount", constant = "0")
    @Mapping(target = "documentFile", ignore = true)  // handle file bytes in service
    @Mapping(target = "appUser", ignore = true)       // handle user entity in service
    Document toEntity(DocumentUploadDAO dao);
}
