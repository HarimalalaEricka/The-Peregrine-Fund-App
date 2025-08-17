package com.example.theperegrinefund;

import java.util.Date;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;

public class Message {

    private int idMessage;
    private Date dateSignalement;
    private String description;
    private String pointRepere;
    private UserApp user;

    // Champs optionnels
    private Double surfaceApproximative;
    private String direction;
    private String elementsVisibles;
    private String degats;
    private String contenuCode;

    // Constructeur principal
    public Message(int idMessage, Date dateSignalement, String description, String pointRepere, UserApp user) {
        this.idMessage = idMessage;
        this.dateSignalement = dateSignalement;
        this.description = description;
        this.pointRepere = pointRepere;
        this.user = user;
    }

    public String chiffrer(String cleSecrete) throws Exception {
        SecretKeySpec key = new SecretKeySpec(cleSecrete.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(description.getBytes("UTF-8"));
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    }

    public String dechiffrer(String cleSecrete, String texteChiffre) throws Exception {
        SecretKeySpec key = new SecretKeySpec(cleSecrete.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decoded = Base64.decode(texteChiffre, Base64.DEFAULT);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted, "UTF-8");
    }

    public int getIdMessage() { return idMessage; }
    public void setIdMessage(int idMessage) { this.idMessage = idMessage; }
    public Date getDateSignalement() { return dateSignalement; }
    public void setDateSignalement(Date dateSignalement) { this.dateSignalement = dateSignalement; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPointRepere() { return pointRepere; }
    public void setPointRepere(String pointRepere) { this.pointRepere = pointRepere; }
    public UserApp getUser() { return user; }
    public void setUser(UserApp user) { this.user = user; }
    public Double getSurfaceApproximative() { return surfaceApproximative; }
    public void setSurfaceApproximative(Double surfaceApproximative) { this.surfaceApproximative = surfaceApproximative; }
    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }
    public String getElementsVisibles() { return elementsVisibles; }
    public void setElementsVisibles(String elementsVisibles) { this.elementsVisibles = elementsVisibles; }
    public String getDegats() { return degats; }
    public void setDegats(String degats) { this.degats = degats; }
    public String getContenuCode() { return contenuCode; }
    public void setContenuCode(String contenuCode) { this.contenuCode = contenuCode; }
}
