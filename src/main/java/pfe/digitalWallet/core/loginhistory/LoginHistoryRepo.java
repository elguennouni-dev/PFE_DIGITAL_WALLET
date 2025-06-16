package pfe.digitalWallet.core.loginhistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoginHistoryRepo extends JpaRepository<LoginHistory, Long> {
    Optional<LoginHistory> findByAppUser_Username(String username);
    List<LoginHistory> findAllByAppUserIdOrderByDateTimeDesc(Long id);
    Optional<LoginHistory> findFirstByAppUserIdOrderByDateTimeDesc(Long id);

    List<LoginHistory> findAllByAppUser_Username(String username);


}
