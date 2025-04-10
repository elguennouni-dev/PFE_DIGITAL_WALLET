package pfe.digitalWallet.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pfe.digitalWallet.auth.jwt.JwtBlacklistService;
import pfe.digitalWallet.auth.jwt.JwtUtil;
import pfe.digitalWallet.core.appuser.AppUser;
import pfe.digitalWallet.core.appuser.UserService;
import pfe.digitalWallet.core.appuser.dao.LoginRequest;
import pfe.digitalWallet.core.appuser.dto.UserDto;
import pfe.digitalWallet.core.loginattempt.LoginAttempt;
import pfe.digitalWallet.core.loginattempt.LoginAttemptService;
import pfe.digitalWallet.core.loginhistory.LoginHistory;
import pfe.digitalWallet.core.rsaKey.RSAKey;
import pfe.digitalWallet.core.rsaKey.RSAKeyService;
import pfe.digitalWallet.core.rsaKey.util.RSAUtil;
import pfe.digitalWallet.shared.dto.LogoutRequest;
import pfe.digitalWallet.shared.dto.SignupRequest;
import pfe.digitalWallet.shared.enums.attempt.AttemptStatus;
import pfe.digitalWallet.shared.enums.login.LoginStatus;
import pfe.digitalWallet.core.appuser.mapper.UserMapper;
import pfe.digitalWallet.shared.validation.PasswordValidator;

import java.security.KeyPair;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordValidator passwordValidator;
    @Autowired
    private SecurityEventService securityEventService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtBlacklistService jwtBlacklistService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LoginAttemptService loginAttemptService;
    @Autowired
    private RSAKeyService rsaKeyService;


    // Handle login
    public Optional<UserDto> login(LoginRequest request) {
        LocalDateTime time = LocalDateTime.now();
        Optional<UserDto> optionalUserDto = userService.getByUsername(request.username());
        if(optionalUserDto.isEmpty()) {
            return Optional.empty();
        }

        UserDto userDto = optionalUserDto.get();
        AppUser userEntity = userMapper.toEntity(userDto);
        LoginAttempt attempt = loginAttemptService.getByUser(userEntity);

        // Check login attempts
        if(attempt != null && attempt.getFailedAttempts() >= 5 && attempt.getLastFailedAttempt().isAfter(LocalDateTime.now().minusMinutes(15))) {
            throw new IllegalArgumentException("Your account is locked due too many login attempts. Please tru again later.");
        }

        // Verify password
        if(!passwordValidator.isValidPassword(request.password(), userDto.id())) {
            logLoginAttempt(userEntity, time, AttemptStatus.FAILURE);
            return Optional.empty();
        }

        loginAttemptService.resetFailedAttempts(userEntity);

        try {
            String token = jwtUtil.generateToken(userDto.username());
            UserDto updatedUser = userDto.withToken(token);

            // Save login history and login attempt
            logLoginHistory(request, userEntity, time, LoginStatus.LOGGED_IN);
            logLoginAttempt(userEntity, time, AttemptStatus.SUCCESS);
            return Optional.of(updatedUser);

        } catch (Exception e) {
            handleException("Error during token generation", e);
            return Optional.empty();
        }
    }


    // Handle signup
    public Optional<UserDto> signup(SignupRequest request) {
        LocalDateTime time = LocalDateTime.now();

        // Check if username or email already exists
        if(userService.getByUsername(request.username()).isPresent() || userService.findByEmail(request.email()).isPresent()) {
            return Optional.empty();
        }

        try {
            String encodedPassword = passwordEncoder.encode(request.password());
            AppUser appUser = new AppUser();
            appUser.setUsername(request.username());
            appUser.setEmail(request.email());
            appUser.setCreatedAt(time);
            appUser.setUpdatedAt(time);
            appUser.setPassword(encodedPassword);

            Optional<AppUser> savedUser = userService.save(appUser);
            if(savedUser.isEmpty()) {
                return Optional.empty();
            }

            String token = jwtUtil.generateToken(request.username());
            UserDto userDto = userMapper.toDto(savedUser.get()).withToken(token);

            // RSAKey
            KeyPair keyPair = RSAUtil.generateKeyPair();
            String publicKey = RSAUtil.encodeKey(keyPair.getPublic());
            String privateKey = RSAUtil.encodeKey(keyPair.getPrivate());

            // Store RSAKey
            RSAKey rsaKey = new RSAKey();
            rsaKey.setUser(savedUser.get());
            rsaKey.setPublicKey(publicKey);
            rsaKey.setPrivateKeyEncrypted(privateKey);
            rsaKey.setCreatedAt(time);

            rsaKeyService.save(rsaKey);

            return Optional.of(userDto);

        } catch (Exception e) {
            handleException("Error during signup process", e);
        }

        return Optional.empty();

    }



    // Handle logput
    public void logout(LogoutRequest request) {
        LocalDateTime time = LocalDateTime.now();
        String token = request.token();

        if(token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token is empty or invalid");
        }

        if (!jwtUtil.isValidToken(token)) {
            throw new IllegalArgumentException("Token is invalid or expired");
        }

        String username = jwtUtil.getUsernameFromToken(token);
        UserDto userDto = userService.getByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if(!jwtUtil.validateToken(token, username)) {
            throw new IllegalArgumentException("Token does not match the username");
        }

        AppUser user = userMapper.toEntity(userDto);

        // Log logout history and blacklist token
        logLogoutHistory(request, user, time, LoginStatus.LOGGED_OUT);
        jwtBlacklistService.blacklistToken(token);

    }


    // Loggin functions
    private void logLoginAttempt(AppUser appUser, LocalDateTime time, AttemptStatus status) {
        try {
            LoginAttempt attempt = new LoginAttempt();
            attempt.setAppUser(appUser);
            attempt.setDateTime(time);
            attempt.setLoginStatus(status);

            if(status == AttemptStatus.FAILURE) {
                // Increment failed attempts
                attempt.setLastFailedAttempt(LocalDateTime.now());
                loginAttemptService.incrementFailedAttempts(appUser);
            } else {
                attempt.setLastFailedAttempt(LocalDateTime.now());
            }

            securityEventService.saveLoginAttempt(attempt);
        } catch (Exception e) {
            handleException("Error during login attempt loggin", e);
        }
    }

    private void logLoginHistory(LoginRequest request, AppUser appUser, LocalDateTime time, LoginStatus status) {
        try {
            LoginHistory history = new LoginHistory();
            history.setDevice(request.device());
            history.setLocation(request.location());
            history.setAppUser(appUser);
            history.setDateTime(time);
            history.setLoginStatus(status);
            securityEventService.saveLoginHistory(history);
        } catch (Exception e) {
            handleException("Error during login history logging", e);
        }
    }

    private void logLogoutHistory(LogoutRequest request, AppUser appUser, LocalDateTime time, LoginStatus status) {
        try {
            LoginHistory history = new LoginHistory();
            history.setDevice(request.device());
            history.setLocation(request.location());
            history.setAppUser(appUser);
            history.setDateTime(time);
            history.setLoginStatus(status);
            securityEventService.saveLogoutHistory(history);
        } catch (Exception e) {
            handleException("Error during logout history logging", e);
        }
    }

    // Handle exceptions
    private void handleException(String message, Exception e) {
        e.printStackTrace();
        throw new RuntimeException(message, e);
    }
}
