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

        // Iterate through all the field errors
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
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

    @ExceptionHandler(TooManyRequestsException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(TooManyRequestsException ex) {


        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "Too Many requests, try again later");
        errorResponse.put("cause", ex.getMessage());
        errorResponse.put("zonedatetime", ZonedDateTime.now().toString());
        errorResponse.put("httpstatus", HttpStatus.TOO_MANY_REQUESTS.value());

        // Return the response with status 429
        return new ResponseEntity<>(errorResponse, HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(UnauthorizedException ex) {


        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "Unauthorized");
        errorResponse.put("cause", ex.getMessage());
        errorResponse.put("zonedatetime", ZonedDateTime.now().toString());
        errorResponse.put("httpstatus", HttpStatus.UNAUTHORIZED.value());

        // Return the response with status 401
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
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

//MethodArgumentNotValidException
//
//🔹 When to Use: When a method argument fails validation (e.g., using @Valid or @Validated).
//
//🔹 Example Scenario: The user submits a form with an invalid email format and the server returns an error.
//
//ConstraintViolationException
//
//🔹 When to Use: When a validation constraint (e.g., @NotNull, @Size) fails.
//
//🔹 Example Scenario: Trying to create a user account without providing a required field like email.
//
//DataIntegrityViolationException
//
//🔹 When to Use: When a database constraint (e.g., unique constraint) is violated.
//
//🔹 Example Scenario: Trying to insert a duplicate email address into the user table where emails must be unique.
//
//HttpMessageNotReadableException
//
//🔹 When to Use: When the HTTP request body is not readable (e.g., invalid JSON format).
//
//🔹 Example Scenario: Sending an incomplete or malformed JSON body in a POST request.
//
//HttpRequestMethodNotSupportedException
//
//🔹 When to Use: When the HTTP method (e.g., GET, POST, etc.) used is not supported by the endpoint.
//
//🔹 Example Scenario: A client sends a POST request to an endpoint that only accepts GET.
//
//UnsupportedMediaTypeException
//
//🔹 When to Use: When the content type in the request is not supported (e.g., Content-Type header).
//
//🔹 Example Scenario: A client sends a request with an unsupported content type such as text/xml when the server only supports application/json.
//
//NotFoundException
//
//🔹 When to Use: When the requested resource could not be found (e.g., 404 error).
//
//🔹 Example Scenario: A client requests a non-existent user profile by ID.
//
//UnauthorizedException
//
//🔹 When to Use: When a client is unauthorized to access the requested resource (e.g., missing or invalid authentication).
//
//🔹 Example Scenario: A user tries to access a protected endpoint without providing valid credentials.
//
//BadRequestException
//
//🔹 When to Use: When the request is malformed or missing required data (e.g., 400 error).
//
//🔹 Example Scenario: A client submits a POST request with missing or invalid parameters.
//
//TooManyRequestsException
//
//🔹 When to Use: When a client has exceeded the rate limit of requests.
//
//🔹 Example Scenario: A client makes too many requests to the API in a short period and is throttled.
//
//InternalServerErrorException
//
//🔹 When to Use: When an unexpected error occurs on the server side (e.g., 500 error).
//
//🔹 Example Scenario: A bug or unhandled exception occurs during the processing of a request.
//
//TransactionSystemException
//
//🔹 When to Use: When a transaction fails during commit or rollback in Spring transactions.
//
//🔹 Example Scenario: A database transaction fails to commit due to an error.
//
//HttpClientErrorException
//
//🔹 When to Use: When a client-side error (e.g., 4xx status code) occurs while making an HTTP request.
//
//🔹 Example Scenario: A client makes an invalid API request resulting in a 400 Bad Request.
//
//HttpServerErrorException
//
//🔹 When to Use: When a server-side error (e.g., 5xx status code) occurs while making an HTTP request.
//
//🔹 Example Scenario: A client makes a valid request, but the server encounters an error and returns a 500 Internal Server Error.
//
//AccessDeniedException
//
//🔹 When to Use: When a user is not authorized to access a resource.
//
//🔹 Example Scenario: A user tries to access a resource without the necessary permissions.
//
//NoSuchElementException
//
//🔹 When to Use: When trying to access an element that doesn't exist in a collection or Optional.
//
//🔹 Example Scenario: Trying to fetch an item from an Optional that is empty.
//
//BadSqlGrammarException
//
//🔹 When to Use: When an SQL query has bad syntax or is invalid.
//
//🔹 Example Scenario: Trying to execute an invalid SQL query that results in a syntax error.
//
//ResourceNotFoundException
//
//🔹 When to Use: When a resource is not found, often used in REST APIs for a missing entity.
//
//🔹 Example Scenario: A client requests a non-existent resource, such as a user with a nonexistent ID.
//
//ServiceException
//
//🔹 When to Use: When an error occurs in the service layer of the application.
//
//🔹 Example Scenario: An internal service fails to process a business logic request due to unexpected conditions.
//
//TimeoutException
//
//🔹 When to Use: When an operation takes longer than expected or exceeds a timeout limit.
//
//🔹 Example Scenario: A network request or database query takes too long and exceeds the specified timeout.
//
//ExecutionException
//
//🔹 When to Use: When an exception is thrown while executing a task in a concurrent context.
//
//🔹 Example Scenario: A task executed by an ExecutorService fails due to an underlying exception.
//
//InterruptedException
//
//🔹 When to Use: When a thread is interrupted while performing a blocking operation (e.g., waiting, sleeping).
//
//🔹 Example Scenario: A thread performing a task is interrupted by another thread before completion.