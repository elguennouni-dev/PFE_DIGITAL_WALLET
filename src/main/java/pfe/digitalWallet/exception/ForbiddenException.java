package pfe.digitalWallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//🔹 When to Use: If a user is authenticated but lacks the necessary permissions.
//🔹 Example Scenario: A normal user tries to delete another user's session.

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}

