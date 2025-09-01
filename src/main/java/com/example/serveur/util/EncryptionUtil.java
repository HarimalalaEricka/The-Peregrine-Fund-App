package com.example.serveur.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtil {

    private static final String ALGORITHM = "AES";
    private final String secretKey;

    // Le constructeur reçoit la clé depuis la configuration
    public EncryptionUtil(String secretKey) {
        this.secretKey = secretKey;
    }

    public String chiffrer(String texteClair) throws Exception {
        SecretKeySpec key = new SecretKeySpec(secretKey.getBytes("UTF-8"), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(texteClair.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String dechiffrer(String texteChiffre) throws Exception {
        SecretKeySpec key = new SecretKeySpec(secretKey.getBytes("UTF-8"), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decoded = Base64.getDecoder().decode(texteChiffre);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted, "UTF-8");
    }
}