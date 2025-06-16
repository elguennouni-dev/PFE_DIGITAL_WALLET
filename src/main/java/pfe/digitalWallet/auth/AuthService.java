package pfe.digitalWallet.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import pfe.digitalWallet.core.rsaKey.dto.RSAKeyDto;
import pfe.digitalWallet.core.rsaKey.mapper.RSAKeyMapper;
import pfe.digitalWallet.core.rsaKey.util.RSAUtil;
import pfe.digitalWallet.shared.dto.LoginResponse;
import pfe.digitalWallet.shared.dto.LogoutRequest;
import pfe.digitalWallet.shared.dto.SignupRequest;
import pfe.digitalWallet.shared.dto.SignupResponse;
import pfe.digitalWallet.shared.enums.attempt.AttemptStatus;
import pfe.digitalWallet.shared.enums.login.LoginStatus;
import pfe.digitalWallet.core.appuser.mapper.UserMapper;
import pfe.digitalWallet.shared.validation.PasswordValidator;

import java.security.KeyPair;
import java.time.LocalDateTime;
import java.util.Objects;
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
    @Autowired
    private RSAKeyMapper rsaKeyMapper;



    public Optional<LoginResponse> login(LoginRequest request) {
        LocalDateTime currentTime = LocalDateTime.now();
        Objects.requireNonNull(request, "LoginRequest cannot be null");

        Optional<AppUser> userOptional = userService.findByUsernameOrEmail(request.username());

        if (userOptional.isEmpty()) {
            logLoginHistory(request, null, currentTime, LoginStatus.FAILURE, "Login Failure");
            return Optional.empty();
        }

        AppUser appUser = userOptional.get();
        UserDto userDto = userMapper.toDto(appUser);

        if (!passwordEncoder.matches(request.password(), appUser.getPassword())) {
            logLoginHistory(request, appUser, currentTime, LoginStatus.FAILURE, "Login Failure");
            return Optional.empty();
        }

        String token = jwtUtil.generateToken(userDto.username());

        LoginResponse response = new LoginResponse(userDto.id(), userDto.username(), userDto.email(), token);
        logLoginHistory(request,appUser,currentTime,LoginStatus.LOGGED_IN, "Login Success");

        return Optional.of(response);
    }



    @Transactional
    public Optional<SignupResponse> signup(SignupRequest request) {
        Objects.requireNonNull(request, "SignupRequest cannot be null");
        LocalDateTime time = LocalDateTime.now();

        if (userService.getByUsername(request.username()).isPresent()) {
            return Optional.empty();
        }

        if (userService.findByEmail(request.email()).isPresent()) {
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

            KeyPair keyPair = RSAUtil.generateKeyPair();
            String publicKey = RSAUtil.encodeKey(keyPair.getPublic());
            String privateKey = RSAUtil.encodeKey(keyPair.getPrivate());

            RSAKey rsaKey = new RSAKey();
            rsaKey.setPublicKey(publicKey);
            rsaKey.setPrivateKeyEncrypted(privateKey);
            rsaKey.setCreatedAt(time);

            rsaKey.setUser(appUser);

            AppUser savedUser = userService.save(appUser)
                    .orElseThrow(() -> new IllegalStateException("Failed to save user"));

            RSAKey savedRsaKey = Optional.ofNullable(rsaKeyService.save(rsaKey))
                    .orElseGet(() -> Optional.ofNullable(rsaKeyService.save(rsaKey))
                            .orElseThrow(() -> new IllegalStateException("Failed to save RSA key after multiple attempts")));

            String token = jwtUtil.generateToken(savedUser.getUsername());

            UserDto userDto = userMapper.toDto(savedUser);
            RSAKeyDto rsaKeyDto = rsaKeyMapper.toRSAKeyDTO(savedRsaKey);

            if (userDto == null || rsaKeyDto == null) {
                throw new IllegalStateException("DTO mapping failed");
            }

            return Optional.of(new SignupResponse(
                    userDto.id(),
                    userDto.username(),
                    userDto.email(),
                    token,
                    userDto.createdAt(),
                    userDto.updatedAt(),
                    userDto.isLocked(),
                    userDto.lockUntil()
            ));

        } catch (Exception e) {
            throw new RuntimeException("Signup failed", e);
        }
    }


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
        UserDto userDto = userMapper.toDto(userService.getByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found")));

        if(!jwtUtil.validateToken(token, username)) {
            throw new IllegalArgumentException("Token does not match the username");
        }

        AppUser user = userMapper.toEntity(userDto);

        logLogoutHistory(request, user, time, LoginStatus.LOGGED_OUT, "Logged Out");
        jwtBlacklistService.blacklistToken(token);

    }


    private void logLoginAttempt(AppUser appUser, LocalDateTime time, AttemptStatus status) {
        try {
            LoginAttempt attempt = new LoginAttempt();
            attempt.setAppUser(appUser);
            attempt.setDateTime(time);
            attempt.setLoginStatus(status);

            if(status == AttemptStatus.FAILURE) {
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

    private void logLoginHistory(LoginRequest request, AppUser appUser, LocalDateTime time, LoginStatus status, String reason) {
        try {
            LoginHistory history = new LoginHistory();
            history.setDevice(request.device());
            history.setLocation(request.location());
            history.setAppUser(appUser);
            history.setDateTime(time);
            history.setLoginStatus(status);
            history.setReason(reason);
            history.setIpAddress(request.ipAddress());
            securityEventService.saveLoginHistory(history);
        } catch (Exception e) {
            handleException("Error during login history logging", e);
        }
    }

    private void logLogoutHistory(LogoutRequest request, AppUser appUser, LocalDateTime time, LoginStatus status, String reason) {
        try {
            LoginHistory history = new LoginHistory();
            history.setDevice(request.device());
            history.setLocation(request.location());
            history.setAppUser(appUser);
            history.setDateTime(time);
            history.setLoginStatus(status);
            history.setReason(reason);
            securityEventService.saveLogoutHistory(history);
        } catch (Exception e) {
            handleException("Error during logout history logging", e);
        }
    }

    private void handleException(String message, Exception e) {
        e.printStackTrace();
        throw new RuntimeException(message, e);
    }
}