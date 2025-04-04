package pfe.digitalWallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//🔹 When to Use: If an API rate limit is exceeded.
//🔹 Example Scenario: A user repeatedly tries to log in after failed attempts.

@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException(String message) {
        super(message);
    }
}