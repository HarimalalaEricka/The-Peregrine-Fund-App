package com.example.theperegrinefund;

import java.net.ContentHandler;

import android.telephony.SmsManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.util.Date;
import com.example.theperegrinefund.security.ConfigLoader;
import android.util.Log;
import java.util.ArrayList;


public class SmsSender {
    private Context context;  
    private String SECRET_KEY;
    private String num;

    public SmsSender(Context context) {
        this.context = context;
        try {
            SECRET_KEY = ConfigLoader.getSecretKey(context);
            num =   ConfigLoader.getFixedNumber(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  public void send(Message message, String status) throws Exception {
    try {
        if (!message.isValid()) {
            throw new Exception("Numéro invalide: " + message.getPhoneNumber());
        }
        
        String content = formatMessage(message, status);
        Log.d("SMS", "Message original: " + content);
        
        String encryptedContent = message.chiffrer(SECRET_KEY, content);
        Log.d("SMS", "Message chiffré: " + encryptedContent);

        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> parts = smsManager.divideMessage(encryptedContent);
        
        if(parts.size() > 1) {
            smsManager.sendMultipartTextMessage(
                message.getPhoneNumber(),
                null,
                parts,
                null,
                null
            );
        } else {
            smsManager.sendTextMessage(
                message.getPhoneNumber(),
                null,
                encryptedContent,
                null,
                null
            );
        }
        
       // int newId = message.save(context);
      //  long a = historique.insertHistorique
       // Log.d("SMS", "Message sauvegardé avec ID: " + newId);
        
    } catch (Exception e) {
        Log.e("SMS", "Erreur envoi: " + e.getMessage(), e);
        throw e;
    }
}
    public void sendUser(String message) throws Exception {
        if (message == null || message.trim().isEmpty()) {
            throw new Exception("Contenu invalide");
        }
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(
                num,
                null,
                message,
                null,
                null
        );
    }
    public String formatMessage(Message message, String status) {
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
        int idStatus = dbHelper.getIdStatusMessage(status);
        if (message == null) return "";
        

        return message.getDateCommencement() + "/" +
               message.getDateSignalement() + "/" +
               message.getIdIntervention() + "/" +
               message.isRenfort() + "/" +
               message.getDirection() + "/" +
               message.getSurfaceApproximative() + "/" +
               message.getPointRepere() + "/" +
               message.getDescription() + "/" +
               message.getIdUserApp() +  "/" + // exemple pour UserApp
               message.getLongitude() + "/" +
               message.getLatitude() + "/" +
               idStatus;
    }
}
