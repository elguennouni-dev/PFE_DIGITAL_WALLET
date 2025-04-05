package pfe.digitalWallet.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfe.digitalWallet.shared.dto.ApiResponse;
import pfe.digitalWallet.shared.dto.LoginRequest;
import pfe.digitalWallet.shared.dto.UserDto;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserDto>> login(@RequestBody LoginRequest loginRequest) {

    }


}