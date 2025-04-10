package pfe.digitalWallet.core.document;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfe.digitalWallet.core.document.dao.DocumentDao;
import pfe.digitalWallet.core.document.dto.DocumentDto;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;


    @PostMapping("/upload")
    public ResponseEntity<DocumentDto> uploadDocument(@RequestBody DocumentDao documentDao) {
        try {
            return ResponseEntity.ok(documentService.saveDocument(documentDao));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }


    // Get all documents by user id
    @GetMapping("/by-user-id/{app_user_id}")
    public ResponseEntity<List<DocumentDto>> getAllDocumentsByUserId(@PathVariable Long app_user_id) {
        List<DocumentDto> documents = documentService.getAllByUserId(app_user_id);
        if (documents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(documents);
    }

    // Get a document by id
//    @GetMapping("/{id}")
//    public ResponseEntity<DocumentDto> getDocumentById(@PathVariable Long id) {
//        Optional<DocumentDto> document = documentService.getDocumentById(id);
//        return document.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    // Create a new document
//    @PostMapping
//    public ResponseEntity<DocumentDto> createDocument(@RequestBody @Valid DocumentDto documentDto) {
//        DocumentDto createdDocument = documentService.createDocument(documentDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdDocument);
//    }
//
//    // Update an existing document
//    @PutMapping("/{id}")
//    public ResponseEntity<DocumentDto> updateDocument(@PathVariable Long id, @RequestBody @Valid DocumentDto documentDto) {
//        Optional<DocumentDto> updatedDocument = documentService.updateDocument(id, documentDto);
//        return updatedDocument.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    // Increment view count
//    @PatchMapping("/{id}/increment-view")
//    public ResponseEntity<DocumentDto> incrementViewCount(@PathVariable Long id) {
//        Optional<DocumentDto> updatedDocument = documentService.incrementViewCount(id);
//        return updatedDocument.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    // Increment download count
//    @PatchMapping("/{id}/increment-download")
//    public ResponseEntity<DocumentDto> incrementDownloadCount(@PathVariable Long id) {
//        Optional<DocumentDto> updatedDocument = documentService.incrementDownloadCount(id);
//        return updatedDocument.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    // Delete a document
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
//        boolean isDeleted = documentService.deleteDocument(id);
//        return isDeleted ? ResponseEntity.noContent().build()
//                : ResponseEntity.notFound().build();
//    }

}

