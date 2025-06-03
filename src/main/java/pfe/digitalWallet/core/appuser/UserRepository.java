package pfe.digitalWallet.core.appuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    // @Query("SELECT u FROM AppUser u WHERE u.username = :username")
    // @Query("SELECT u FROM AppUser u LEFT JOIN FETCH u.rsaKey r WHERE u.username = :username")
    // AppUser findByUsername(@Param("username") String username);


     @Query("SELECT u FROM AppUser u WHERE u.username = :username")
     AppUser findByUsername(String username);

    AppUser findByEmail(String email);

    @Query("SELECT u FROM AppUser u WHERE u.username = ?1 AND u.password = ?1")
    AppUser findByUsernameAndPassword(String username, String password);


//    @Query("SELECT u FROM AppUser u WHERE u.username = ?1 OR u.email = ?1")
    @Query(value = "SELECT u.* FROM app_user u WHERE u.username = ?1 OR u.email = ?1", nativeQuery = true)
    Optional<AppUser> findByUsernameOrEmail(String usernameOrEmail);

}
