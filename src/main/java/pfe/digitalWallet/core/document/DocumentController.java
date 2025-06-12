package pfe.digitalWallet.core.document;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfe.digitalWallet.core.document.dao.DocumentUpdateRequest;
import pfe.digitalWallet.core.document.dao.DocumentUploadRequest;
import pfe.digitalWallet.core.document.dto.DocumentDto;
import pfe.digitalWallet.core.document.dto.ExtendedDocumentDto;

import java.util.List;
import java.util.Map;

//@CrossOrigin(origins = "http://localhost:3000")
//@CrossOrigin(origins = "http://127.0.0.1:5500")
@CrossOrigin
@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    // Get all documents by user username
    @GetMapping("/user/{username}")
    public ResponseEntity<?> getAllDocuments(@PathVariable String username) {
        return documentService.getAllDocumentsByUserId(username);
    }

    // Upload document
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadDocument(@Valid @ModelAttribute DocumentUploadRequest request) {
        return documentService.uploadDocument(request);
    }

    // Get decrypted document by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getDocumentById(@PathVariable Long id, @RequestParam String username) {
        return documentService.getDocumentByID(id, username);
    }

    // Update document
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateDocument(@PathVariable Long id, @RequestBody DocumentUpdateRequest dao) {
        return documentService.updateDocument(id,dao);
    }

    // Delete document
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable Long id) {
        return documentService.deleteDocument(id);
    }


}

