package pfe.digitalWallet.core.appuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfe.digitalWallet.core.appuser.dto.UserDto;
import pfe.digitalWallet.core.appuser.mapper.UserMapper;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository appUserRepository;

    @Autowired
    private UserMapper userMapper;

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

    public Optional<UserDto> getByUsername(String username) {
        return appUserRepository.findByUsername(username)
                .map(userMapper::toDto);
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
