package pfe.digitalWallet.core.appuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.digitalWallet.shared.dto.UserDto;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository appUserRepository;

    public Optional<AppUser> findByUsername(String username) {
        AppUser user = appUserRepository.findByUsername(username);
        return Optional.ofNullable(user);
    }

    public String getPasswordByUserId(Long id) {
        AppUser appUser = appUserRepository.findById(id).orElse(null);
        if(appUser == null) {
            return null;
        }
        return appUser.getPassword();
    }


    public Optional<AppUser> findById(Long id) {
        return appUserRepository.findById(id);
    }
}
