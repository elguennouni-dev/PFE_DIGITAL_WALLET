package pfe.digitalWallet.core.document.dto;

import java.time.LocalDateTime;

public record DocumentDto(
        Long id,
        LocalDateTime createdAt,
        byte[] documentFile,
        String documentTitle,
        Integer downloadCount,
        String rsaKey,
        Integer viewCount,
        Long appUserId
) {
    public DocumentDto withId(Long id) {
        return new DocumentDto(id, this.createdAt, this.documentFile, this.documentTitle, this.downloadCount, this.rsaKey, this.viewCount, this.appUserId);
    }

    public DocumentDto withCreatedAt(LocalDateTime createdAt) {
        return new DocumentDto(this.id, createdAt, this.documentFile, this.documentTitle, this.downloadCount, this.rsaKey, this.viewCount, this.appUserId);
    }

    public DocumentDto withDocumentFile(byte[] documentFile) {
        return new DocumentDto(this.id, this.createdAt, documentFile, this.documentTitle, this.downloadCount, this.rsaKey, this.viewCount, this.appUserId);
    }

    public DocumentDto withDocumentTitle(String documentTitle) {
        return new DocumentDto(this.id, this.createdAt, this.documentFile, documentTitle, this.downloadCount, this.rsaKey, this.viewCount, this.appUserId);
    }

    public DocumentDto withDownloadCount(Integer downloadCount) {
        return new DocumentDto(this.id, this.createdAt, this.documentFile, this.documentTitle, downloadCount, this.rsaKey, this.viewCount, this.appUserId);
    }

    public DocumentDto withRsaKey(String rsaKey) {
        return new DocumentDto(this.id, this.createdAt, this.documentFile, this.documentTitle, this.downloadCount, rsaKey, this.viewCount, this.appUserId);
    }

    public DocumentDto withViewCount(Integer viewCount) {
        return new DocumentDto(this.id, this.createdAt, this.documentFile, this.documentTitle, this.downloadCount, this.rsaKey, viewCount, this.appUserId);
    }

    public DocumentDto withAppUserId(Long appUserId) {
        return new DocumentDto(this.id, this.createdAt, this.documentFile, this.documentTitle, this.downloadCount, this.rsaKey, this.viewCount, appUserId);
    }
}
