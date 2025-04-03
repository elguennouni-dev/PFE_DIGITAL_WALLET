package pfe.digitalWallet.shared.enums.session;

public enum SessionStatus {
    AUTHENTICATED,
    PENDING;

    public String toString(){
        return switch (this){
            case AUTHENTICATED -> "Authenticated";
            case PENDING -> "Pending";
        };
    }

}
