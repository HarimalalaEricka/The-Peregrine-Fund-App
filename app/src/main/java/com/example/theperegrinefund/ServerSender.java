package com.example.theperegrinefund;

import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.example.theperegrinefund.security.ConfigLoader;

import java.util.Date;

public class ServerSender {

    private final ApiService apiService;
    private final SmsSender smsSender;
    private final Context context;
    private String SECRET_KEY;

    public ServerSender(ApiService apiService, SmsSender smsSender, Context context) {
        this.apiService = apiService;
        this.smsSender = smsSender;
        this.context = context;
        try {
            SECRET_KEY = ConfigLoader.getSecretKey(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(Message message, String status) {
        String content = formatMessage(message, status);

        final String encryptedContent;
        try {
            encryptedContent = message.chiffrer(SECRET_KEY, content);
        } catch (Exception e) {
            Toast.makeText(context, "Erreur lors du chiffrement : " + e.getMessage(), Toast.LENGTH_LONG).show();
            return; // on stoppe ici si le chiffrement échoue
        }

        Call<Void> call = apiService.sendEncryptedMessage(encryptedContent);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    int newId = message.save(context);
                    // message.setIdMessage(newId);
                    // MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
                    // int idStatus = dbHelper.getIdStatusMessage(status);
                    // StatusMessage statusMessage = new StatusMessage(idStatus, status);
                    // HistoriqueMessageStatus historique = new HistoriqueMessageStatus(new Date(), statusMessage, message);
                    // historique.save(context);
                    Toast.makeText(context, "Message envoyé au serveur!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Erreur serveur", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Impossible de joindre le serveur. Envoi par SMS...", Toast.LENGTH_SHORT).show();
                try {
                    smsSender.send(message, status);
                    Toast.makeText(context, "Message envoyé par SMS!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(context, "Erreur SMS : " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
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
