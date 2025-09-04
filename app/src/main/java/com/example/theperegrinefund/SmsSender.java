package com.example.theperegrinefund;

import java.net.ContentHandler;

import android.telephony.SmsManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.util.Date;
import com.example.theperegrinefund.security.ConfigLoader;


public class SmsSender {
    private Context context;  
    private String SECRET_KEY;

    public SmsSender(Context context) {
        this.context = context;
        try {
            SECRET_KEY = ConfigLoader.getSecretKey(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(Message message, String status) throws Exception {
        if (!message.isValid()) {
            throw new Exception("Numéro ou message invalide");
        }
        String content = formatMessage(message, status);
        String encryptedContent = message.chiffrer(SECRET_KEY, content);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(
                message.getPhoneNumber(),
                null,
                encryptedContent,
                null,
                null
        );
        int newId = message.save(context);
        
    }
    public String formatMessage(Message message, String status) {
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
        int idStatus = dbHelper.getIdStatusMessage(status);
        if (message == null) return "";
        

        return message.getDateCommencement() + "/" +
               message.getDateSignalement() + "/" +
               message.getIntervention().getIdIntervention() + "/" +
               message.isRenfort() + "/" +
               message.getDirection() + "/" +
               message.getSurfaceApproximative() + "/" +
               message.getPointRepere() + "/" +
               message.getDescription() + "/" +
               message.getUser() +  "/" + // exemple pour UserApp
               message.getLongitude() + "/" +
               message.getLatitude() + "/" +
               idStatus;
    }
}
