package pfe.digitalWallet.shared.dto;

import lombok.Getter;
import lombok.Setter;

public record ApiResponse<T>(
    private boolean success,
    private String message,
    private T data
    )
{}
