package pfe.digitalWallet.core.rsaKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.digitalWallet.core.appuser.AppUser;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;
import java.util.Optional;

@Service
public class RSAKeyService {

    public static final int KEY_SIZE = 2048;

    @Autowired
    private RSAKeyRepo rsaKeyRepo;


    public void save(RSAKey rsaKey) {
        rsaKeyRepo.save(rsaKey);
    }

    public Optional<RSAKey> getKeyByUser(AppUser user) {
        return rsaKeyRepo.findByUser(user);
    }



}
