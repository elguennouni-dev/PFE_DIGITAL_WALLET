package pfe.digitalWallet.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

        Optional<UserDto> userDtoOptional = authService.login(loginRequest);

        if(userDtoOptional.isEmpty()) {
            return buildResponse(false, "Invalid username or password", null, HttpStatus.UNAUTHORIZED);
        }
        return buildResponse(true, "Login successful", userDtoOptional.get(), HttpStatus.OK);
    }


    // Signup handling
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserDto>> signup(@RequestBody SignupRequest signupRequest) {

        Optional<UserDto> userDtoOptional = authService.signup(signupRequest);

        if(userDtoOptional.isEmpty()) {
            return buildResponse(false, "Username or email already taken", null, HttpStatus.CONFLICT);
        }
        return buildResponse(true, "Signup successful", userDtoOptional.get(), HttpStatus.CREATED);
    }


    // Logout handling
//    @PostMapping("/logout")
//    public ResponseEntity<ApiResponse<?>> logout(@RequestBody LogoutRequest logoutRequest) {
//
//    }


    // Private helper methods
    private ResponseEntity<ApiResponse<UserDto>> buildResponse(boolean success, String message, UserDto data, HttpStatus status) {
        ApiResponse<UserDto> response = new ApiResponse<>();
        response.setSuccess(success);
        response.setMessage(message);
        response.setData(data);
        return ResponseEntity.status(status).body(response);
    }

}