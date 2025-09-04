package com.example.theperegrinefund;

import java.util.Date;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Message {

    private int idMessage;
    private Date dateCommencement;
    private Date dateSignalement;
    private Intervention intervention;
    private boolean renfort;
    private String direction;
    private double surfaceApproximative;
    private String pointRepere;
    private String description;
    private int user;
    private String phoneNumber;
    private double longitude;
    private double latitude;

    public Message(Date dateCommencement, Date dateSignalement,
                   Intervention intervention, boolean renfort, String direction,
                   double surfaceApproximative, String pointRepere, String description,
                   String phoneNumber, double longitude, double latitude, int user) {
        // this.idMessage = idMessage;
        this.dateCommencement = dateCommencement;
        this.dateSignalement = dateSignalement;
        this.intervention = intervention;
        this.renfort = renfort;
        this.direction = direction;
        this.surfaceApproximative = surfaceApproximative;
        this.pointRepere = pointRepere;
        this.description = description;
        this.user = user;
        this.phoneNumber = phoneNumber;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String chiffrer(String cleSecrete, String mess) throws Exception {
        SecretKeySpec key = new SecretKeySpec(cleSecrete.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(mess.getBytes("UTF-8"));
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

    public int save(Context context) {
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Date_Commencement", dateCommencement.getTime()); // stocké en timestamp
        values.put("Date_signalement", dateSignalement.getTime());
        values.put("PointRepere", pointRepere);
        values.put("Surface_Approximative", surfaceApproximative);
        values.put("Description", description);
        values.put("Direction", direction);
        values.put("renfort", renfort);
        values.put("longitude", longitude);
        values.put("latitude", latitude);

        if (intervention != null) {
            values.put("Id_Intervention", intervention.getIdIntervention());
        }
        if (user != 0) {
            values.put("IdUserApp", user);
        }

        int newId = (int) db.insert("Message", null, values);
        db.close();
        return newId; // retourne l’ID inséré ou -1 en cas d’erreur
    }

    public int getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(int idMessage) {
        this.idMessage = idMessage;
    }

    public Date getDateCommencement() {
        return dateCommencement;
    }

    public void setDateCommencement(Date dateCommencement) {
        this.dateCommencement = dateCommencement;
    }

    public Date getDateSignalement() {
        return dateSignalement;
    }

    public void setDateSignalement(Date dateSignalement) {
        this.dateSignalement = dateSignalement;
    }

    public Intervention getIntervention() {
        return intervention;
    }

    public void setIntervention(Intervention intervention) {
        this.intervention = intervention;
    }

    public boolean isRenfort() {
        return renfort;
    }

    public void setRenfort(boolean renfort) {
        this.renfort = renfort;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public double getSurfaceApproximative() {
        return surfaceApproximative;
    }

    public void setSurfaceApproximative(double surfaceApproximative) {
        this.surfaceApproximative = surfaceApproximative;
    }

    public String getPointRepere() {
        return pointRepere;
    }

    public void setPointRepere(String pointRepere) {
        this.pointRepere = pointRepere;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public boolean isValid() {
        return phoneNumber != null && !phoneNumber.isEmpty();
    }
    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

}
