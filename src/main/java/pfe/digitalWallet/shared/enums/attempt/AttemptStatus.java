package pfe.digitalWallet.shared.enums.attempt;

public enum AttemptStatus {
    SUCCESS, FAILURE;

    public String toString() {
        return switch (this){
            case SUCCESS -> "Success";
            case FAILURE -> "Failure";
        };
    }

}
