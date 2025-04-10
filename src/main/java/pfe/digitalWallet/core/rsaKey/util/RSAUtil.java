package pfe.digitalWallet.core.rsaKey.util;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

// This class used in AuthService (SignUp Method)
public class RSAUtil {

    public static final int KEY_SIZE = 2048;

    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(KEY_SIZE);
            return generator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException("Error generating RSA Key pair", e);
        }
    }
    public static String encodeKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static PublicKey decodePublicKey(String encoded) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(encoded);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Error decoding public key", e);
        }
    }

    public static PrivateKey decodePrivateKey(String encoded) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(encoded);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Error decoding private key", e);
        }
    }


}
