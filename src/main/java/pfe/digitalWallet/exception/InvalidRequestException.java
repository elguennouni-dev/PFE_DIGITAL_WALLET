package pfe.digitalWallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//🔹 When to Use: If the request parameters/body contain invalid data but pass validation.
//🔹 Example Scenario: A user tries to update a document with an invalid RSA key format.

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}

