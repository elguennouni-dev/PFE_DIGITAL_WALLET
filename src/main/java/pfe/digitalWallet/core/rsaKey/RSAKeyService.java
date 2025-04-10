package pfe.digitalWallet.core.rsaKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public KeyPair generateKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(KEY_SIZE);
            return generator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException("Error generating RSA Key pair",e);
        }
    }

    public String encodeKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

}
