package pfe.digitalWallet.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfe.digitalWallet.shared.dto.ApiResponse;
import pfe.digitalWallet.shared.dto.LoginRequest;
import pfe.digitalWallet.shared.dto.UserDto;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserDto>> login(@RequestBody LoginRequest loginRequest) {

        Optional<UserDto> userDtoOptional = authService.login(loginRequest);

        if(userDtoOptional.isEmpty()) {
            return buildResponse(false, "Invalid username or password", null, HttpStatus.UNAUTHORIZED);
        }
        return buildResponse(true, "Login successful", userDtoOptional.get(), HttpStatus.OK);
    }


    // Private helper methods
    private ResponseEntity<ApiResponse<UserDto>> buildResponse(boolean success, String message, UserDto data, HttpStatus status) {
        ApiResponse<UserDto> response = new ApiResponse<>();
        response.setSuccess(success);
        response.setMessage(message);
        response.setData(data);
        return ResponseEntity.status(status).body(response);
    }

}