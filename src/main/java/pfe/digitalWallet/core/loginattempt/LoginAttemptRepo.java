package pfe.digitalWallet.core.loginattempt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginAttemptRepo extends JpaRepository<LoginAttempt, Long> {
}
