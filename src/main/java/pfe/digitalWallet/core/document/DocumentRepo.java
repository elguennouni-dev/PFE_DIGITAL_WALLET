package pfe.digitalWallet.core.document;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepo extends JpaRepository<Document, Long> {
    List<Document> findAllByAppUserId(Long appUserId);
    List<Document> findAllByDocumentTitleContainingIgnoreCase(String title);
}
