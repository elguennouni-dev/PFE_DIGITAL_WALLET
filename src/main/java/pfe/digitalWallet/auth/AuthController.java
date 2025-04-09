package pfe.digitalWallet.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfe.digitalWallet.core.appuser.dao.LoginRequest;
import pfe.digitalWallet.core.appuser.dto.UserDto;
import pfe.digitalWallet.shared.dto.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Login handling
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserDto>> login(@RequestBody LoginRequest loginRequest) {
        try {
            Optional<UserDto> userDtoOptional = authService.login(loginRequest);
            return userDtoOptional.map(userDto -> buildResponse(true, "Login successful", userDto, HttpStatus.OK))
                    .orElseGet(() -> buildResponse(false, "Invalid username or password", null, HttpStatus.UNAUTHORIZED));
        } catch (Exception e) {
            return buildResponse(false, "Internal Server Error: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Signup handling
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserDto>> signup(@RequestBody SignupRequest signupRequest) {
        try {
            Optional<UserDto> userDtoOptional = authService.signup(signupRequest);
            return userDtoOptional.map(userDto -> buildResponse(true, "Signup successful", userDto, HttpStatus.CREATED))
                    .orElseGet(() -> buildResponse(false, "Username or email already taken", null, HttpStatus.CONFLICT));
        } catch (Exception e) {
            return buildResponse(false, "Internal Server Error: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Logout handling
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(@RequestBody LogoutRequest logoutRequest) {
        try {
            authService.logout(logoutRequest);
            return ResponseEntity.ok(new ApiResponse<>(true, "Logout successful", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(false, "Error during logout: " + e.getMessage(), null)
            );
        }
    }

    // Private helper methods
    private ResponseEntity<ApiResponse<UserDto>> buildResponse(boolean success, String message, UserDto data, HttpStatus status) {
        return ResponseEntity.status(status).body(new ApiResponse<>(success, message, data));
    }

    // Exception Handling
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiResponse<>(false, "An unexpected error occurred: " + e.getMessage(), null)
        );
    }
}
