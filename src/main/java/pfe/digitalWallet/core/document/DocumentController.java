package pfe.digitalWallet.core.document;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pfe.digitalWallet.core.document.dto.DocumentDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;



    @GetMapping("/by-user-id/{app_user_id}")
    public ResponseEntity<List<DocumentDto>> getAllDocumentsByUserId(@PathVariable Long app_user_id) {
        Optional<List<Document>> documents = documentService.getAllByUserId(app_user_id);
        return buildDocumentResponse(documents);
    }

    @GetMapping("/by-title/{title}")
    public ResponseEntity<List<DocumentDto>> getAllDocumentsByTitle(@PathVariable String title) {
        Optional<List<Document>> documents = documentService.getAllByTitle(title);
        return buildDocumentResponse(documents);
    }

    private ResponseEntity<List<DocumentDto>> buildDocumentResponse(Optional<List<Document>> documents) {
        if (documents.isPresent()) {
            List<DocumentDto> documentDtos = documents.get().stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(documentDtos);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

    private DocumentDto mapToDto(Document document) {
        return new DocumentDto(
                document.getId(),
                document.getCreatedAt(),
                document.getDocumentFile(),
                document.getDocumentTitle(),
                document.getDownloadCount(),
                document.getRsaKey(),
                document.getViewCount(),
                document.getAppUser().getId()
        );
    }
}

