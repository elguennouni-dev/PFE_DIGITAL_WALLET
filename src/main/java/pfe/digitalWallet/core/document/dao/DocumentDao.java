package pfe.digitalWallet.core.document.dao;

import org.springframework.web.multipart.MultipartFile;

public record DocumentDao(
        MultipartFile file,
        String documentTitle,
        Long appUserId
) {}
