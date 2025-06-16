package pfe.digitalWallet.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfe.digitalWallet.core.appuser.dao.LoginRequest;
import pfe.digitalWallet.core.appuser.dto.UserDto;
import pfe.digitalWallet.shared.dto.*;

import java.util.Optional;

@CrossOrigin(origins = {"http://127.0.0.1:5500","https://aitoolsforteachers.site"})
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginRequest loginRequest) {
        try {
            Optional<LoginResponse> userDtoOptional = authService.login(loginRequest);
            return userDtoOptional.map(userDto -> buildResponse(true, "Login successful", userDto, HttpStatus.OK))
                    .orElseGet(() -> buildResponse(false, "Invalid username or password", null, HttpStatus.UNAUTHORIZED));
        } catch (Exception e) {
            return buildResponse(false, "Internal Server Error: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> signup(@RequestBody SignupRequest signupRequest) {
        try {
            Optional<SignupResponse> signupResponse = authService.signup(signupRequest);
            return signupResponse.map(response -> buildResponse(true, "Signup successful", response, HttpStatus.OK)).orElseGet(() -> buildResponse(false, "Username or email already exists", null, HttpStatus.CONFLICT));
        } catch (Exception e) {
            return buildResponse(false, "Internal Server Error: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(@RequestBody LogoutRequest logoutRequest) {
        try {
            authService.logout(logoutRequest);
            return buildResponse(true, "Logout successful", null, HttpStatus.OK);
        } catch (Exception e) {
            return buildResponse(false, "Error during logout: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<ApiResponse<?>> buildResponse(boolean success, String message, Object data, HttpStatus status) {
        return ResponseEntity.status(status).body(new ApiResponse<>(success, message, data));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        return buildResponse(false, "An unexpected error occurred: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}