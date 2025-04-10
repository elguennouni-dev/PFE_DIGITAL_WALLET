package pfe.digitalWallet.core.rsaKey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pfe.digitalWallet.core.appuser.AppUser;

import java.util.Optional;

@Repository
public interface RSAKeyRepo extends JpaRepository<RSAKey, Long> {
    Optional<RSAKey> findByUser(AppUser user);
}
