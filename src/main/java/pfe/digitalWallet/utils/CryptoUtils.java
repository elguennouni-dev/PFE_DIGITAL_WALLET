package pfe.digitalWallet.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

public class CryptoUtils {

    // --- AES methods ---

    // Generate a new AES secret key (128 bits)
    public static SecretKey generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // 128-bit AES
        return keyGen.generateKey();
    }

    // Encrypt data with AES key, returns encrypted bytes
    public static byte[] aesEncrypt(byte[] data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    // Decrypt AES encrypted bytes, returns original bytes
    public static byte[] aesDecrypt(byte[] encryptedData, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(encryptedData);
    }

    // Convert AES key to Base64 encoded string
    public static String encodeAESKeyToBase64(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    // Convert Base64 encoded AES key string back to SecretKey
    public static SecretKey decodeAESKeyFromBase64(String base64Key) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }


    // --- RSA methods ---

    // Encrypt data using RSA public key (input bytes), returns encrypted bytes
    public static byte[] rsaEncrypt(byte[] data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    // Decrypt data using RSA private key (input encrypted bytes), returns decrypted bytes
    public static byte[] rsaDecrypt(byte[] encryptedData, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encryptedData);
    }

    // Convert Base64 encoded RSA public key string to PublicKey object
    public static PublicKey getPublicKeyFromBase64(String base64PublicKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64PublicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    // Convert Base64 encoded RSA private key string to PrivateKey object
    public static PrivateKey getPrivateKeyFromBase64(String base64PrivateKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64PrivateKey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

/*
                        AES--------------------usage
    SecretKey aesKey = CryptoUtils.generateAESKey(); // must be encrypted with RSA
    byte[] encrypted = CryptoUtils.aesEncrypt(originalData, aesKey);
    byte[] decrypted = CryptoUtils.aesDecrypt(encrypted, aesKey);


                        RSA--------------------usage
    PublicKey pubKey = CryptoUtils.getPublicKeyFromBase64(base64PublicKeyString);
    PrivateKey privKey = CryptoUtils.getPrivateKeyFromBase64(base64PrivateKeyString);

    byte[] encrypted = CryptoUtils.rsaEncrypt(data, pubKey);
    byte[] decrypted = CryptoUtils.rsaDecrypt(encrypted, privKey);
*/
}
