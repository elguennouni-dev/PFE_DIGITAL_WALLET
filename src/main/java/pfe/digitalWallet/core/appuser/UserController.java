package pfe.digitalWallet.core.appuser;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfe.digitalWallet.auth.jwt.JwtUtil;
import pfe.digitalWallet.shared.dto.ApiResponse;
import pfe.digitalWallet.core.appuser.dao.LoginRequest;
import pfe.digitalWallet.core.appuser.dto.UserDto;
import pfe.digitalWallet.shared.mapper.UserMapper;

import java.util.Optional;

@RestController
@RequestMapping("/appuser")
@RequiredArgsConstructor
public class UserController {

    private final UserService appUserService;
    private final UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/login")
    public ResponseEntity<ApiResponse<UserDto>> login(@RequestBody LoginRequest request) {
        Optional<AppUser> optionalUser = appUserService.login(request.username(), request.password());

        if (optionalUser.isPresent()) {
            UserDto dto = userMapper.toDto(optionalUser.get());
            String token = jwtUtil.generateToken(dto.username());
            dto.withToken(token);

            return ResponseEntity.ok(new ApiResponse<>(
                    true,"Login successful",dto)
            );
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(
                false,"Invalid username or password",null)
        );
    }

}
