package pfe.digitalWallet.core.document;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import pfe.digitalWallet.core.appuser.AppUser;
import pfe.digitalWallet.core.appuser.UserRepository;
import pfe.digitalWallet.core.document.dao.DocumentUpdateRequest;
import pfe.digitalWallet.core.document.dao.DocumentUploadRequest;
import pfe.digitalWallet.core.document.dto.DocumentDto;
import pfe.digitalWallet.core.document.dto.ExtendedDocumentDto;
import pfe.digitalWallet.core.document.dto.FullDcoumentDto;
import pfe.digitalWallet.core.document.mapper.DocumentMapper;
import pfe.digitalWallet.core.rsaKey.RSAKey;
import pfe.digitalWallet.core.rsaKey.RSAKeyRepo;
import pfe.digitalWallet.encryption.AesEncryptionUtils;
import pfe.digitalWallet.encryption.RsaEncryptionUtils;
import pfe.digitalWallet.exception.NotFoundException;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {

    @Autowired
    private DocumentRepo documentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RSAKeyRepo rsaKeyRepo;
    @Autowired
    private DocumentMapper documentMapper;
    @Autowired
    private Validator validator;



    @Transactional
    public ResponseEntity<?> uploadDocument(@Valid DocumentUploadRequest request) {

        try {
            String title = request.getDocumentTitle();
            MultipartFile file = request.getFile();
            String username = request.getUsername();
            String fileType = request.getFileType();

            AppUser user = userRepository.findByUsername(username);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message","User not found or not authenticated"));
            }

            Optional<RSAKey> rsaKeyOptional = rsaKeyRepo.findByUser(user);

            if (rsaKeyOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message","User RSA Key not found"));
            }

            RSAKey rsaKey = rsaKeyOptional.get();
            SecretKey aesKey = AesEncryptionUtils.generateAESKey(256);

            byte[] originalFileBytes = file.getBytes();
            byte[] encryptedFileBytes = AesEncryptionUtils.encrypt(originalFileBytes, aesKey);

            PublicKey publicKey = RsaEncryptionUtils.getPublicKeyFromBase64(rsaKey.getPublicKey());
            byte[] encryptedAESKey = RsaEncryptionUtils.encrypt(aesKey.getEncoded(), publicKey);

            Document document = new Document();
            document.setDocumentTitle(title);
            document.setDocumentFile(encryptedFileBytes);
            document.setAppUser(user);
            document.setRsaKey(Base64.getEncoder().encodeToString(encryptedAESKey));
            document.setCreatedAt(LocalDateTime.now());
            document.setDownloadCount(0);
            document.setViewCount(0);
            document.setFileType(fileType);

            Document saved = documentRepository.save(document);

            return ResponseEntity.status(HttpStatus.CREATED).body(documentMapper.toDto(saved));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message","Error while uploading document: " + e.getMessage()));
        }
    }



    @Transactional
    public ResponseEntity<?> getAllDocumentsByUserId(String username) {

        AppUser user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "User not found or not authenticated"));
        }


        try {

            RSAKey rsaKey = rsaKeyRepo.findByUser(user)
                    .orElseThrow(() -> new NotFoundException("RSA key not found"));

            PrivateKey privateKey = RsaEncryptionUtils.getPrivateKeyFromBase64(rsaKey.getPrivateKeyEncrypted());

            List<Document> documentsList = documentRepository.findAllByAppUserId(user.getId());

            if (documentsList.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","No documents found"));

            List<DocumentDto> documentDtoList = new ArrayList<>();

            for (Document document : documentsList) {
                byte[] encryptedAESKey = Base64.getDecoder().decode(document.getRsaKey());
                byte[] aesKeyBytes = RsaEncryptionUtils.decrypt(encryptedAESKey, privateKey);
                SecretKey aesKey = AesEncryptionUtils.restoreAESKey(aesKeyBytes);

                byte[] encryptedFileBytes = document.getDocumentFile();
                byte[] decryptedFileBytes = AesEncryptionUtils.decrypt(encryptedFileBytes, aesKey);
                String decryptedFileContent = Base64.getEncoder().encodeToString(decryptedFileBytes);

                DocumentDto fullDto = documentMapper.toDto(document);

                documentDtoList.add(fullDto);
            }

            return ResponseEntity.ok(documentDtoList);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message","Error while getting documents: " + e.getMessage()));
        }
    }


    // Get document by ID
//    public ResponseEntity<?> getDocumentById(Long id) {
//
//        if (id == 0) {
//            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
//                    .body(Map.of("message","Document ID must not be 0"));
//        }
//
//        Optional<Document> documentOptional = documentRepository.findById(id);
//        if (documentOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(Map.of("message","No document found with ID: " + id));
//        }
//
//        Document document = documentOptional.get();
//        return ResponseEntity.status(HttpStatus.FOUND)
//                .body(documentMapper.toDto(document));
//
//    }


    // Update document
    public ResponseEntity<?> updateDocument(Long documentId, DocumentUpdateRequest updateRequest) {

        AppUser currentUser = userRepository.findByUsername(updateRequest.getUsername());
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "User not found or not authenticated"));
        }

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document with ID: " + documentId + " not found"));

        if (!document.getAppUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "You are not allowed to update this document"));
        }

        if (StringUtils.hasText(updateRequest.getNewDocumentTitle())) {
            document.setDocumentTitle(updateRequest.getNewDocumentTitle());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(Map.of("message", "New document title must not be empty"));
        }

        Set<ConstraintViolation<Document>> violations = validator.validateProperty(document, "documentTitle");
        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(Map.of("message", "Invalid document title: " + errorMessage));
        }

        return ResponseEntity.ok(documentMapper.toDto(documentRepository.save(document)));
    }



    // Delete document
    public ResponseEntity<?> deleteDocument(Long id) {

        if (id == 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(Map.of("message","Document ID must not be 0"));
        }

        Optional<Document> documentOptional = documentRepository.findById(id);

        if(documentOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message","Document with ID: " + id + " not found"));
        }

        documentRepository.delete(documentOptional.get());

        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message","Document " + documentOptional.get().getDocumentTitle() + ", deleted successfully"));

    }



    // Get full document with ID
    @Transactional(readOnly = true)
    public ResponseEntity<?> getDocumentByID(Long documentID, String username) {
        AppUser user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "User not found or not authenticated"));
        }

            Optional<Document> documentOptional = documentRepository.findById(documentID);

            if (documentOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message","Document with ID: " + documentID + " not found"));
            }
            Document document = documentOptional.get();
            if (!document.getAppUser().getId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message","You are not allowed to view this document"));
            }

            Optional<RSAKey> rsaKeyOptional = rsaKeyRepo.findByUser(user);

            if (rsaKeyOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message","User RSA Key not found"));
            }

            try {
                RSAKey rsaKey = rsaKeyOptional.get();
                PrivateKey privateKey = RsaEncryptionUtils.getPrivateKeyFromBase64(rsaKey.getPrivateKeyEncrypted());

                byte[] encryptedAESKey = Base64.getDecoder().decode(document.getRsaKey());
                byte[] aesKeyBytes = RsaEncryptionUtils.decrypt(encryptedAESKey, privateKey);
                SecretKey aesKey = AesEncryptionUtils.restoreAESKey(aesKeyBytes);

                byte[] decryptedFile = AesEncryptionUtils.decrypt(document.getDocumentFile(), aesKey);

                String decryptedContent = Base64.getEncoder().encodeToString(decryptedFile);

                FullDcoumentDto dto = new FullDcoumentDto(
                        document.getId(),
                        document.getDocumentTitle(),
                        document.getCreatedAt(),
                        document.getFileType(),
                        decryptedContent
                );

                return ResponseEntity.ok(dto);

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message","Error while decrypting document: " + e.getMessage()));
            }


    }



    // id / created_at / document_file / document_title / download_count / rsa_key / view_count / app_user_i
}
