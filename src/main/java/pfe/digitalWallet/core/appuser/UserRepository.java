package pfe.digitalWallet.core.appuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    @Query("SELECT u FROM AppUser u WHERE u.username = :username")
    AppUser findByUsername(@Param("username") String username);


    AppUser findByEmail(String email);
    AppUser findByUsernameAndPassword(String username, String password);
}
