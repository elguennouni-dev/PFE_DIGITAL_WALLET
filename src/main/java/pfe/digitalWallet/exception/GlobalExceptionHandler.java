package pfe.digitalWallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<Map<String, Object>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<org.springframework.validation.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("messagetoshowinui", "Validation error occurred");
        errorResponse.put("cause", fieldErrors.get(0).getDefaultMessage());  // Cause from the validation error message
        errorResponse.put("zonedatetime", ZonedDateTime.now().toString());
        errorResponse.put("httpstatus", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(List.of(errorResponse), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<List<Map<String, Object>>> handleAllExceptions(Exception ex) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("messagetoshowinui", "An unexpected error occurred");
        errorResponse.put("cause", ex.getMessage());
        errorResponse.put("zonedatetime", ZonedDateTime.now().toString());
        errorResponse.put("httpstatus", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(List.of(errorResponse), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
