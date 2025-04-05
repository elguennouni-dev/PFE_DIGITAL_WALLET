package pfe.digitalWallet.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfe.digitalWallet.core.appuser.AppUser;
import pfe.digitalWallet.core.appuser.AppUserService;
import pfe.digitalWallet.shared.dto.LoginRequest;
import pfe.digitalWallet.shared.dto.SignupRequest;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AppUserService appUserService;
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Optional<AppUser> user = appUserService.getByUsername(username);

        if(!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("This username does not exist.");
        }

        if(!user.get().getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Wrong password.");
        }

        String token = "SimpleTokenTest";



    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignupRequest signupRequest) {

        String username = signupRequest.getUsername();
        String password = signupRequest.getPassword();
        String email = signupRequest.getEmail();

        Optional<AppUser> existUserByUsername = appUserService.getByUsername(username);
        Optional<AppUser> existUserByEmail = appUserService.getByUsername(email);

        if (existUserByUsername.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Username is already taken.");
        }

        if(existUserByEmail.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email is already taken.");
        }

        LocalDateTime now = LocalDateTime.now();
        AppUser newUser = new AppUser();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setCreatedAt(now);
        newUser.setUpdatedAt(now);

        AppUser savedUser = appUserService.save(newUser).orElse(null);

        if(savedUser == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again later.");
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Signup successful for user: " + savedUser.getUsername());

    }

}