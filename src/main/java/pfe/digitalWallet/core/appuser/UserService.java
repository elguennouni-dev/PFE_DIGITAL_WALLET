package pfe.digitalWallet.core.appuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfe.digitalWallet.shared.dto.UserDto;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository appUserRepository;


    public Optional<AppUser> login(String username, String password) {
        AppUser user = appUserRepository.findByUsernameAndPassword(username,password);
        return Optional.ofNullable(user);
    }

    public Optional<List<AppUser>> getAll() {
        List<AppUser> appUsers = appUserRepository.findAll();
        return Optional.ofNullable(appUsers);
    }

    @Transactional
    public Optional<AppUser> save(AppUser appUser) {
        AppUser user = appUserRepository.save(appUser);
        return Optional.of(user);
    }

    public Optional<AppUser> getByUsername(String username) {
        AppUser user = appUserRepository.findByUsername(username);
        return Optional.ofNullable(user);
    }

    public Optional<AppUser> findByEmail(String email) {
        AppUser user = appUserRepository.findByEmail(email);
        return Optional.ofNullable(user);
    }

    public Optional<AppUser> findById(Long id) {
        return appUserRepository.findById(id);
    }

    public void deleteById(Long id) {
        appUserRepository.deleteById(id);
    }

}
