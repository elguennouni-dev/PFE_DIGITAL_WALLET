package pfe.digitalWallet.shared.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import pfe.digitalWallet.core.appuser.AppUser;
import pfe.digitalWallet.core.appuser.UserService;
import pfe.digitalWallet.shared.dto.UserDto;

import java.util.Optional;

public class PasswordValidator {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public PasswordValidator(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    public boolean isValidPassword(String password, Long id) {
        AppUser appUser = userService.findById(id).orElse(null);
        if (appUser == null) {
            return false;
        }
        return passwordEncoder.matches(password, appUser.getPassword());
    }


}
