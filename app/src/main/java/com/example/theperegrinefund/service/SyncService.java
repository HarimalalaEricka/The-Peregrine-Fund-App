package com.example.theperegrinefund.service;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class SyncService {
   private static final String BASE_URL = "https://e560f407f8a4.ngrok-free.app/sync";
    private OkHttpClient client;
    private Gson gson;
    private MessageDao messageDao;

    public SyncService(Context context) {
        client = new OkHttpClient();
        gson = new Gson();
        messageDao = new MessageDao(context);
    }

    // Récupérer les messages du serveur
    public void downloadMessages(int idUser) {
        String url = BASE_URL + "/download/" + idUser;

        Request request = new Request.Builder()
                .url(url)
                .build();

        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful() && response.body() != null) {
                    String json = response.body().string();
                    Type listType = new TypeToken<List<Message>>() {}.getType();
                    List<Message> messages = gson.fromJson(json, listType);

                    for (Message msg : messages) {
                        messageDao.insertMessage(msg);
                    }

                    Log.d("SYNC", "Messages téléchargés et insérés : " + messages.size());
                } else {
                    Log.e("SYNC", "Erreur HTTP : " + response.code());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
