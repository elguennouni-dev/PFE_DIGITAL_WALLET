package pfe.digitalWallet.core.appuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pfe.digitalWallet.auth.jwt.JwtUtil;
import pfe.digitalWallet.shared.dto.ApiResponse;
import pfe.digitalWallet.shared.dto.LoginRequest;
import pfe.digitalWallet.shared.dto.UserDto;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/appuser")
public class UserController {
    @Autowired
    private UserService appUserService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/login")
    public ResponseEntity<ApiResponse<UserDto>> login(@RequestBody LoginRequest request) {
        Optional<AppUser> optionalUser = appUserService.login(request.getUsername(), request.getPassword());

        if (optionalUser.isPresent()) {
            UserDto dto = UserDto.from(optionalUser.get());
            String token = jwtUtil.generateToken(dto.getUsername());
            dto.setToken(token);

            ApiResponse<UserDto> response = new ApiResponse<>();
            response.setSuccess(true);
            response.setMessage("Login successful");
            response.setData(dto);

            return ResponseEntity.ok(response);
        } else {
            ApiResponse<UserDto> response = new ApiResponse<>();
            response.setSuccess(false);
            response.setMessage("Invalid username or password");
            response.setData(null);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }


}
