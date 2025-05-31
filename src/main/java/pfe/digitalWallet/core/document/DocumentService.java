package pfe.digitalWallet.core.document;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pfe.digitalWallet.core.appuser.AppUser;
import pfe.digitalWallet.core.appuser.UserRepository;
import pfe.digitalWallet.core.appuser.UserService;
import pfe.digitalWallet.core.document.dao.DocumentUploadDAO;
import pfe.digitalWallet.core.document.mapper.DocumentMapper;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepo documentRepository;

    public Object uploadDocument(@Valid DocumentUploadDAO dao) {
        return null;
    }
    public Object getAllDocuments() {
        return null;
    }

    public Object getDocumentById(Long id) {
        return null;
    }

    public Object updateDocument(Long id, DocumentUploadDAO dao) {
        return null;
    }

    public Object deleteDocument(Long id) {
        return null;
    }

    // id / created_at / document_file / document_title / download_count / rsa_key / view_count / app_user_i
}
