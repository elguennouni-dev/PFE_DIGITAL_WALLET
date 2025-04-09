package pfe.digitalWallet.core.document;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.digitalWallet.core.document.dto.DocumentDto;
import pfe.digitalWallet.core.document.mapper.DocumentMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {

    @Autowired
    private DocumentRepo documentRepository;
    @Autowired
    private DocumentMapper documentMapper;

    public List<DocumentDto> getAllByUserId(Long userId) {
        List<Document> documents = documentRepository.findAllByAppUserId(userId);
        return documentMapper.toDocumentDtoList(documents); // Mapping Entity to DTO
    }

    public Optional<DocumentDto> getDocumentById(Long id) {
        return documentRepository.findById(id)
                .map(documentMapper::toDocumentDto); // Optional handling
    }

    public DocumentDto createDocument(DocumentDto documentDto) {
        Document document = documentMapper.toDocument(documentDto);
        Document savedDocument = documentRepository.save(document);
        return documentMapper.toDocumentDto(savedDocument); // Return saved document as DTO
    }

    public Optional<DocumentDto> updateDocument(Long id, DocumentDto documentDto) {
        if (!documentRepository.existsById(id)) {
            return Optional.empty(); // Document not found
        }
        Document document = documentMapper.toDocument(documentDto);
        document.setId(id);
        Document updatedDocument = documentRepository.save(document);
        return Optional.of(documentMapper.toDocumentDto(updatedDocument)); // Return updated document
    }

    public Optional<DocumentDto> incrementViewCount(Long id) {
        Optional<Document> document = documentRepository.findById(id);
        document.ifPresent(doc -> {
            doc.setViewCount(doc.getViewCount() + 1);
            documentRepository.save(doc);
        });
        return document.map(documentMapper::toDocumentDto); // Return updated document
    }

    public Optional<DocumentDto> incrementDownloadCount(Long id) {
        Optional<Document> document = documentRepository.findById(id);
        document.ifPresent(doc -> {
            doc.setDownloadCount(doc.getDownloadCount() + 1);
            documentRepository.save(doc);
        });
        return document.map(documentMapper::toDocumentDto); // Return updated document
    }

    public boolean deleteDocument(Long id) {
        if (!documentRepository.existsById(id)) {
            return false; // If not found, return false
        }
        documentRepository.deleteById(id);
        return true; // Successfully deleted
    }




    // id / created_at / document_file / document_title / download_count / rsa_key / view_count / app_user_id

}
