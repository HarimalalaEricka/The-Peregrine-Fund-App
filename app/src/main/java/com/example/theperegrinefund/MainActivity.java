package com.example.theperegrinefund;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.example.theperegrinefund.dao.MessageDao;
import com.example.theperegrinefund.dao.AlerteDao;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView textView;

    // ID fixe pour l'utilisateur unique
    private static final int FIXED_USER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        // Initialiser les DAO
        MessageDao messageDao = new MessageDao(this);
        AlerteDao alerteDao = new AlerteDao(this);

        // ---------- INSÉRER UN UTILISATEUR UNIQUE SI NON EXISTANT ----------
        // Ici on suppose que l'utilisateur FIXED_USER_ID existe déjà dans la table users
        // Sinon tu peux faire un insert manuel via SQLiteDatabase

        // ---------- INSERT LOCAL TEST ----------
        long messageId = messageDao.insertMessage(
                "2025-08-18",        // dateCommencement
                "2025-08-19",        // dateSignalement
                "CODE123",           // contenuCode
                "Arbre du centre",   // pointRepere
                "Incendie observé",  // description
                45.5,                // surface
                "Nord",              // direction
                "Fumée visible",     // elementsVisibles
                "Forêt brûlée",      // degats
                FIXED_USER_ID        // idUserApp fixe
        );

        alerteDao.insertAlerte(messageId, "Feu de forêt");

        // ---------- SYNCHRONISATION ----------
        SyncService syncService = new SyncService(this);
        syncService.downloadMessages(FIXED_USER_ID);  // récupérer messages du serveur

        // ---------- AFFICHER LES DONNÉES LOCALES ----------
        refreshUI(messageDao, alerteDao);
    }

    private void refreshUI(MessageDao messageDao, AlerteDao alerteDao) {
        List<String> messages = messageDao.getAllMessages();
        List<String> alertes = alerteDao.getAllAlertes();

        StringBuilder builder = new StringBuilder();
        builder.append("📩 MESSAGES:\n");
        for (String m : messages) builder.append("- ").append(m).append("\n");

        builder.append("\n🚨 ALERTES:\n");
        for (String a : alertes) builder.append("- ").append(a).append("\n");

        runOnUiThread(() -> textView.setText(builder.toString()));
    }
}
