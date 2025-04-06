package pfe.digitalWallet.shared.validation;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pfe.digitalWallet.core.appuser.UserService;

@Component
public class PasswordValidator {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public PasswordValidator(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isValidPassword(String rawPassword, Long userId) {
        return userService.findById(userId)
                .map(appUser -> passwordEncoder.matches(rawPassword, appUser.getPassword()))
                .orElse(false);
    }
}
