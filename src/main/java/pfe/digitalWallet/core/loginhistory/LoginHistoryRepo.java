package pfe.digitalWallet.core.loginhistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginHistoryRepo extends JpaRepository<LoginHistory, Long> {
    Optional<LoginHistory> findByAppUser_Username(String username);
}
