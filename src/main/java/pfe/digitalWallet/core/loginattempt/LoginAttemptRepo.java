package pfe.digitalWallet.core.loginattempt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pfe.digitalWallet.core.appuser.AppUser;

import java.util.Optional;

@Repository
public interface LoginAttemptRepo extends JpaRepository<LoginAttempt, Long> {
    LoginAttempt findByAppUser(AppUser appUser);
}
