package pfe.digitalWallet.shared.enums.session;

public enum SessionStatus {
    AUTHENTICATED,
    PENDING,
    UNAUTHENTICATED;

    public String toString(){
        return switch (this){
            case AUTHENTICATED -> "Authenticated";
            case PENDING -> "Pending";
            case UNAUTHENTICATED -> "Unauthenticated";
        };
    }

}
