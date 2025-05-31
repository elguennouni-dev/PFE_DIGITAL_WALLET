package pfe.digitalWallet.core.document;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pfe.digitalWallet.core.document.dao.DocumentUploadDAO;

import java.io.IOException;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadDocument(@Valid @ModelAttribute DocumentUploadDAO dao) throws Exception {
        return ResponseEntity.ok(documentService.uploadDocument(dao));
    }

    @GetMapping
    public ResponseEntity<?> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDocumentById(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getDocumentById(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateDocument(@PathVariable Long id, @Valid @ModelAttribute DocumentUploadDAO dao) throws Exception {
        return ResponseEntity.ok(documentService.updateDocument(id, dao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.deleteDocument(id));
    }
}

