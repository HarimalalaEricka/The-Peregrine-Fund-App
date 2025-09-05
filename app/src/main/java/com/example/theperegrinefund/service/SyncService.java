package com.example.theperegrinefund.service;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.example.theperegrinefund.Message;
import com.example.theperegrinefund.dao.MessageDao;
import com.example.theperegrinefund.dao.InterventionDao;

import com.example.theperegrinefund.dao.StatusMessageDao;
import com.example.theperegrinefund.dao.HistoriqueMessageStatusDao;
import com.example.theperegrinefund.StatusMessage;
import com.example.theperegrinefund.Intervention;
import com.example.theperegrinefund.HistoriqueMessageStatus;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import com.example.theperegrinefund.security.ConfigLoader;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SyncService {
    private static final String TAG = "SyncService";
    // private static final String BASE_URL = "https://20a843da4ac4.ngrok-free.app/sync";

   
    private final OkHttpClient client;
    private final Gson gson;
    private final MessageDao messageDao;
    private InterventionDao interventionDao;
    private StatusMessageDao statusDao;
    private HistoriqueMessageStatusDao historiqueDao;
   private final String BASE_URL;
    
   public SyncService(Context context) {
        client = new OkHttpClient();
        gson = new Gson();
        messageDao = new MessageDao(context);
        interventionDao = new InterventionDao(context);
        statusDao = new StatusMessageDao(context);
        historiqueDao = new HistoriqueMessageStatusDao(context);

        String url;
        try {
            url = ConfigLoader.getServerUrl(context);
            Log.d(TAG, "✅ URL chargée depuis config.properties : " + url);
        } catch (Exception e) {
            Log.e(TAG, "❌ Impossible de charger l'URL du serveur, utilisation de fallback", e);
            url = "https://9f4616fb94ba.ngrok-free.app"; // fallback si jamais ton fichier est absent
        }

        BASE_URL = url + "/sync";
       
    }


    public interface MessageCallback {
        void onComplete(List<Message> messages);
        void onError(Exception e);
    }

    public interface InterventionCallback {
        void onComplete(List<Intervention> interventions);
        void onError(Exception e);
    }
    
    public interface StatusCallback {
    void onComplete(List<StatusMessage> statusMessages);
    void onError(Exception e);
}

public void downloadStatus(StatusCallback callback) {
    String url = BASE_URL + "/status";

    Request request = new Request.Builder().url(url).build();

    new Thread(() -> {
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                String json = response.body().string();
                Log.d("SYNC_DEBUG", "Réponse JSON Status: " + json);
                
                Type listType = new TypeToken<List<StatusMessage>>() {}.getType();
                List<StatusMessage> statusMessages = gson.fromJson(json, listType);

                for (StatusMessage status : statusMessages) {
                    statusDao.insertStatus(status);
                }

                Log.d("SYNC", "Status messages téléchargés : " + statusMessages.size());
                if (callback != null) callback.onComplete(statusMessages);

            } else {
                Log.e("SYNC", "Erreur HTTP : " + response.code());
                if (callback != null) callback.onError(new IOException("HTTP " + response.code()));
            }
        } catch (Exception e) {
            Log.e("SYNC", "Erreur lors du téléchargement des status", e);
            if (callback != null) callback.onError(e);
        }
    }).start();
}

        
  public void downloadMessages(int idUser, MessageCallback callback) {
    String url = BASE_URL + "/download/" + idUser;

    Request request = new Request.Builder().url(url).build();

    new Thread(() -> {
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                String json = response.body().string();
                Type listType = new TypeToken<List<Message>>() {}.getType();
                List<Message> messages = gson.fromJson(json, listType);

                int insertedCount = 0;
                for (Message msg : messages) {
                    msg.setIdUserApp(idUser); // lier à l'utilisateur local
                    // Vérifie si le message existe déjà
                 
                        messageDao.insertMessage(msg);
                        insertedCount++;
                   
                }

                Log.d("SYNC", "Messages téléchargés : " + insertedCount);
                if (callback != null) callback.onComplete(messages);

            } else {
                Log.e("SYNC", "Erreur HTTP : " + response.code());
                if (callback != null) callback.onError(new IOException("HTTP " + response.code()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) callback.onError(e);
        }
    }).start();
}


    

    public void downloadIntervention(InterventionCallback callback) {
    String url = BASE_URL + "/interventions";

    Request request = new Request.Builder().url(url).build();

    new Thread(() -> {
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                String json = response.body().string();
                Log.d("SYNC_DEBUG", "Réponse JSON: " + json); // Log pour déboguer
                
                Type listType = new TypeToken<List<Intervention>>() {}.getType();
                List<Intervention> interventions = gson.fromJson(json, listType);

                for (Intervention inter : interventions) {
                    interventionDao.insertIntervention(inter);
                }

                Log.d("SYNC", "Interventions téléchargées : " + interventions.size());
                if (callback != null) callback.onComplete(interventions);

            } else {
                Log.e("SYNC", "Erreur HTTP : " + response.code());
                if (callback != null) callback.onError(new IOException("HTTP " + response.code()));
            }
        } catch (Exception e) {
            Log.e("SYNC", "Erreur lors du téléchargement des interventions", e);
            if (callback != null) callback.onError(e);
        }
    }).start();
}
public interface HistoriqueCallback {
    void onComplete(List<HistoriqueMessageStatus> historiques);
    void onError(Exception e);
}
public void downloadHistorique(int idUser, HistoriqueCallback callback) {
    String url = BASE_URL + "/historique/" + idUser;

    Request request = new Request.Builder().url(url).build();

    new Thread(() -> {
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                String json = response.body().string();
                Log.d("SYNC_DEBUG", "Réponse JSON Historique: " + json);
                
                Type listType = new TypeToken<List<HistoriqueMessageStatus>>() {}.getType();
                List<HistoriqueMessageStatus> historiques = gson.fromJson(json, listType);

                for (HistoriqueMessageStatus h : historiques) {
                    // Extraire les IDs des objets imbriqués
                    if (h.getStatus() != null) {
                        h.setIdStatusMessage(h.getStatus().getIdStatusMessage());
                    }
                    if (h.getMessage() != null) {
                        h.setIdMessage(h.getMessage().getIdMessage());
                    }
                    historiqueDao.insertHistorique(h);
                }

                Log.d("SYNC", "Historiques téléchargés : " + historiques.size());
                if (callback != null) callback.onComplete(historiques);

            } else {
                Log.e("SYNC", "Erreur HTTP : " + response.code());
                if (callback != null) callback.onError(new IOException("HTTP " + response.code()));
            }
        } catch (Exception e) {
            Log.e("SYNC", "Erreur lors du téléchargement de l'historique", e);
            if (callback != null) callback.onError(e);
        }
    }).start();
}
}