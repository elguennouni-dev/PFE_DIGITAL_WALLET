package pfe.digitalWallet.core.appuser;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfe.digitalWallet.auth.jwt.JwtUtil;
import pfe.digitalWallet.core.appuser.dto.UserDto;
import pfe.digitalWallet.core.appuser.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService appUserService;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;


    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = appUserService.getAll()
                .stream()
                .filter(user -> user instanceof AppUser)
                .map(user -> userMapper.toDto((AppUser) user))
                .collect(Collectors.toList());

        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(users);
        } else {
            return ResponseEntity.ok(users);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return appUserService.findById(id)
                .map(userMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        return (appUserService.getByUsername(username).isPresent()) ?
                ResponseEntity.ok(userMapper.toDto(appUserService.getByUsername(username).get())) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        return appUserService.findByEmail(email)
                .map(userMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

}
