package pfe.digitalWallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errorDetails = new HashMap<>();

        // Iterate through all the field errors
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            // Here fieldError.getField() gives the name of the field, and fieldError.getDefaultMessage() gives the error message
            errorDetails.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "Validation error occurred");
        errorResponse.put("cause", errorDetails);
        errorResponse.put("zonedatetime", ZonedDateTime.now().toString());
        errorResponse.put("httpstatus", HttpStatus.BAD_REQUEST.value());

        // Return the response with status 400
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "An unexpected error occurred. Please try again later.");
        errorResponse.put("cause", ex.getMessage());
        errorResponse.put("zonedatetime", ZonedDateTime.now().toString());
        errorResponse.put("httpstatus", HttpStatus.INTERNAL_SERVER_ERROR.value());

        // Return the response with status 500
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
