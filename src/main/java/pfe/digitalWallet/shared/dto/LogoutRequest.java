package pfe.digitalWallet.shared.dto;


public record LogoutRequest (
    String token,
    String device,
    String location
)
{}