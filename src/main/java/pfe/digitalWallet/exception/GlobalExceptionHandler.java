package pfe.digitalWallet.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.nio.file.AccessDeniedException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errorDetails = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorDetails.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "Validation error occurred");
        errorResponse.put("cause", errorDetails);
        errorResponse.put("zonedatetime", ZonedDateTime.now().toString());
        errorResponse.put("httpstatus", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TooManyRequestsException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ResponseEntity<Map<String, Object>> handleTooManyRequestsException(TooManyRequestsException ex) {


        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "Too Many requests, try again later");
        errorResponse.put("cause", ex.getMessage());
        errorResponse.put("zonedatetime", ZonedDateTime.now().toString());
        errorResponse.put("httpstatus", HttpStatus.TOO_MANY_REQUESTS.value());

        return new ResponseEntity<>(errorResponse, HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedException(UnauthorizedException ex) {


        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "Unauthorized");
        errorResponse.put("cause", ex.getMessage());
        errorResponse.put("zonedatetime", ZonedDateTime.now().toString());
        errorResponse.put("httpstatus", HttpStatus.UNAUTHORIZED.value());

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException ex) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "Resource not found");
        errorResponse.put("cause", ex.getMessage());
        errorResponse.put("zonedatetime", ZonedDateTime.now().toString());
        errorResponse.put("httpstatus", HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }



    @ExceptionHandler({
            ConstraintViolationException.class,
            DataIntegrityViolationException.class,
            HttpMessageNotReadableException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            AccessDeniedException.class,
            HttpClientErrorException.class,
            HttpServerErrorException.class
    })
    public ResponseEntity<Map<String, Object>> handleSpringWebAndUnusualExceptions(Exception ex) {
        HttpStatus status;
        String message;

        if (ex instanceof ConstraintViolationException) {
            status = HttpStatus.BAD_REQUEST;
            message = "Constraint violation error";
        } else if (ex instanceof DataIntegrityViolationException) {
            status = HttpStatus.CONFLICT;
            message = "Database constraint violated";
        } else if (ex instanceof HttpMessageNotReadableException) {
            status = HttpStatus.BAD_REQUEST;
            message = "Malformed JSON request";
        } else if (ex instanceof HttpRequestMethodNotSupportedException) {
            status = HttpStatus.METHOD_NOT_ALLOWED;
            message = "Request method not supported";
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
            message = "Unsupported media type";
        } else if (ex instanceof AccessDeniedException) {
            status = HttpStatus.FORBIDDEN;
            message = "Access denied";
        } else if (ex instanceof HttpClientErrorException) {
            status = HttpStatus.BAD_REQUEST;
            message = "Client error";
        } else if (ex instanceof HttpServerErrorException) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "Server error";
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "An unexpected web error occurred.";
        }

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", message);
        errorResponse.put("cause", ex.getMessage());
        errorResponse.put("zonedatetime", ZonedDateTime.now().toString());
        errorResponse.put("httpstatus", status.value());

        return new ResponseEntity<>(errorResponse, status);
    }
}
