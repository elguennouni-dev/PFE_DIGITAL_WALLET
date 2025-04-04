package pfe.digitalWallet.core.appuser;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    Optional<AppUser> getByID(Long id) {
        return Optional.of(appUserRepository.findById(id)).orElse(null);
    }

    Optional<List<AppUser>> getAll() {
        return Optional.of(appUserRepository.findAll());
    }

    Optional<AppUser> getByUsername(String username) {
        return Optional.of(appUserRepository.findByUsername(username));
    }


}
