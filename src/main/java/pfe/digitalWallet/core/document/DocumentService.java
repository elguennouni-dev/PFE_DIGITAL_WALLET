package pfe.digitalWallet.core.document;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepo documentRepo;

    public Optional<List<Document>> getAllByUserId(Long id) {
        List<Document> documents = documentRepo.findAllByAppUserId(id);
        return documents.isEmpty() ? Optional.empty() : Optional.of(documents);
    }

    public Optional<List<Document>> getAllByTitle(String title) {
        List<Document> documents = documentRepo.findAllByDocumentTitleContainingIgnoreCase(title);
        return documents.isEmpty() ? Optional.empty() : Optional.of(documents);
    }

    // id / created_at / document_file / document_title / download_count / rsa_key / view_count / app_user_id

}
