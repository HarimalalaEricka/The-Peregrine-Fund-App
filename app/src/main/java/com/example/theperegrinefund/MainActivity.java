package com.example.theperegrinefund;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.example.theperegrinefund.dao.UserDao;
import com.example.theperegrinefund.dao.MessageDao;
import com.example.theperegrinefund.dao.AlerteDao;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        // 🔹 Initialiser les DAO
        UserDao userDao = new UserDao(this);
        MessageDao messageDao = new MessageDao(this);
        AlerteDao alerteDao = new AlerteDao(this);

        // ---------- INSERT ----------
        long userId = userDao.insertUser("Fenitra");  // Ajoute un utilisateur
        long messageId = messageDao.insertMessage(
                "2025-08-18",   // dateCommencement
                "2025-08-19",   // dateSignalement
                "CODE123",      // contenuCode
                "Arbre du centre", // pointRepere
                "Incendie observé", // description
                45.5,           // surface
                "Nord",         // direction
                "Fumée visible",// elementsVisibles
                "Forêt brûlée", // degats
                userId          // foreign key vers user
        );
        alerteDao.insertAlerte(messageId, "Feu de forêt"); // Ajoute une alerte

        // ---------- SELECT ----------
        List<String> users = userDao.getAllUsers();
        List<String> messages = messageDao.getAllMessages();
        List<String> alertes = alerteDao.getAllAlertes();

        // ---------- AFFICHER ----------
        StringBuilder builder = new StringBuilder();
        builder.append("👤 USERS:\n");
        for (String u : users) builder.append("- ").append(u).append("\n");

        builder.append("\n📩 MESSAGES:\n");
        for (String m : messages) builder.append("- ").append(m).append("\n");

        builder.append("\n🚨 ALERTES:\n");
        for (String a : alertes) builder.append("- ").append(a).append("\n");

        textView.setText(builder.toString());
    }
}
