package pfe.digitalWallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//🔹 When to Use: If a resource has expired and can no longer be used.
//🔹 Example Scenario: A QR code session has expired.

@ResponseStatus(HttpStatus.GONE)
public class ResourceExpiredException extends RuntimeException {
    public ResourceExpiredException(String message) {
        super(message);
    }
}