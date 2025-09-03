package com.example.serveur.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncryptionUtil {

    private static final String ALGORITHM = "AES";
    private final String secretKey;

    // Le constructeur reçoit la clé depuis la configuration
    public EncryptionUtil(String secretKey) {
        this.secretKey = secretKey;
    }

    private boolean estBase64Valide(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        
        // Nettoyer la chaîne des caractères problématiques
        String cleaned = str.trim()
                           .replace(" ", "")
                           .replace("+", "-")  // Remplace + par - pour URL safety
                           .replace("/", "_"); // Remplace / par _ pour URL safety
        
        // Vérifier la longueur (doit être multiple de 4)
        if (cleaned.length() % 4 != 0) {
            // Ajouter le padding manquant
            cleaned = cleaned + "=".repeat((4 - cleaned.length() % 4) % 4);
        }
        
        try {
            Base64.getDecoder().decode(cleaned);
            return true;
        } catch (IllegalArgumentException e) {
            // Essayer avec URL decoder
            try {
                Base64.getUrlDecoder().decode(cleaned);
                return true;
            } catch (IllegalArgumentException ex) {
                return false;
            }
        }
    }

    public String chiffrer(String texteClair) throws Exception {
        SecretKeySpec key = new SecretKeySpec(secretKey.getBytes("UTF-8"), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(texteClair.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String dechiffrer(String texteChiffre) throws Exception {
        try {
            // Nettoyer et préparer le texte chiffré
            String texteNettoye = texteChiffre.trim()
                                            .replace(" ", "")
                                            .replace("-", "+")  // Rétablir les caractères Base64 standard
                                            .replace("_", "/");
            
            // Ajouter le padding manquant si nécessaire
            if (texteNettoye.length() % 4 != 0) {
                texteNettoye = texteNettoye + "=".repeat((4 - texteNettoye.length() % 4) % 4);
            }
            
            // Vérifier si c'est du Base64 valide
            if (!estBase64Valide(texteNettoye)) {
                throw new Exception("Le texte ne semble pas être au format Base64 valide: " + texteChiffre);
            }
            
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decoded = Base64.getDecoder().decode(texteNettoye);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted, StandardCharsets.UTF_8);
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors du déchiffrement: " + e.getMessage());
            System.err.println("❌ Texte à déchiffrer: '" + texteChiffre + "'");
            throw new Exception("Échec du déchiffrement: " + e.getMessage(), e);
        }
    }
}