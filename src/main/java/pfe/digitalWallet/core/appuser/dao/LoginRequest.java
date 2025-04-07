package pfe.digitalWallet.core.appuser.dao,

public record LoginRequest (
        String username,
        String password,
        String device,
        String location
){}
