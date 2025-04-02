package pfe.digitalWallet.shared.enums.attempt;

public enum AttemptStatus {
    Success, Failure;

    public String toString() {
        return switch (this){
            case Success -> "Success";
            case Failure -> "Failure";
        };
    }

}
