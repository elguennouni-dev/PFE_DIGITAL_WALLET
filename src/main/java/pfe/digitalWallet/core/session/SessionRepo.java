package pfe.digitalWallet.core.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepo extends JpaRepository<Session, Long> {

    Optional<Session> findByAppUser_Id(Long id);

    Optional<Session> save(Session session);
    Optional<Session> findById(Long id);
    Optional<Session> findByAppUser_Username(String username);
    Optional<Session> findByToken(String token);
}
