package pfe.digitalWallet.core.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.digitalWallet.core.appuser.AppUser;

import java.util.Optional;

@Service
public class SessionService {

    @Autowired
    private SessionRepo sessionRepo;

    public Optional<Session> createSession(Session session) {
        return Optional.of(sessionRepo.save(session));
    }

    public Optional<Session> getByUse_Id(AppUser appUser) {
        return sessionRepo.findByAppUser_Id(appUser.getId());
    }

    public Optional<Session> getByToken(String token) {
        return sessionRepo.findBySessionToken(token);
    }

    public Optional<Session> getById(Long id) {
        return sessionRepo.findById(id);
    }

    public Optional<Session> getByUser_Username(AppUser appUser) {
        return sessionRepo.findByAppUser_Username(appUser.getUsername());
    }

}
