package pfe.digitalWallet.shared.enums.login;

public enum LoginStatus {
    LOGGED_IN,
    LOGGED_OUT,
    FAILURE;

    public String toString() {
        return switch(this) {
            case LOGGED_IN -> "Logged In";
            case LOGGED_OUT -> "Logged Out";
            case FAILURE -> "Failure";
        };
    }

}
