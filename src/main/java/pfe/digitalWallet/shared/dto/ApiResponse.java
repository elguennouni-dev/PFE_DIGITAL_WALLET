package pfe.digitalWallet.shared.dto;


public record ApiResponse<T>(
     boolean success,
     String message,
     T data
    )
{}
