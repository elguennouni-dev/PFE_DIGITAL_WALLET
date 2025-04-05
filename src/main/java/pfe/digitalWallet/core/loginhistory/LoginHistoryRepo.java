package pfe.digitalWallet.core.loginhistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginHistoryRepo extends JpaRepository<LoginHistory, Long> {

}
