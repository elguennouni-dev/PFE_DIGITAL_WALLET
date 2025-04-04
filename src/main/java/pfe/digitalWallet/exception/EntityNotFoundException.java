package pfe.digitalWallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//🔹 When to Use: If an entity is not found in the database.
//🔹 Example Scenario: A user requests a document that doesn’t exist.

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
