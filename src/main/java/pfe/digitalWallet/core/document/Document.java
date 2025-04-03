package pfe.digitalWallet.core.document;

import jakarta.persistence.*;
import pfe.digitalWallet.core.user.User;

import java.time.LocalDateTime;

public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long document_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String document_title;
    private String document_file;
    private String rsa_key;
    private LocalDateTime created_at;
    private Long view_count;
    private Long download_count;

}
