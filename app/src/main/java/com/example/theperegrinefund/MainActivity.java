package com.example.theperegrinefund;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import android.util.Log;

import com.example.theperegrinefund.dao.MessageDao;
import com.example.theperegrinefund.dao.InterventionDao;
import com.example.theperegrinefund.dao.StatusMessageDao;
import com.example.theperegrinefund.dao.HistoriqueMessageStatusDao;
import com.example.theperegrinefund.service.SyncService;
import com.example.theperegrinefund.StatusMessage;
import com.example.theperegrinefund.Intervention;
import com.example.theperegrinefund.Message;
import com.example.theperegrinefund.HistoriqueMessageStatus;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private static final int FIXED_USER_ID = 2;

    private MessageDao messageDao;
    private InterventionDao interventionDao;
    private StatusMessageDao statusDao;
    private HistoriqueMessageStatusDao historiqueDao;

   // ...existing code...
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    textView = findViewById(R.id.textView);

    // Initialiser les DAO
    messageDao = new MessageDao(this);
    interventionDao = new InterventionDao(this);
    statusDao = new StatusMessageDao(this);
    historiqueDao = new HistoriqueMessageStatusDao(this);

    // ---------- INSÉRER UN MESSAGE LOCAL TEST ----------
    Message message = new Message();
    message.setUuidMessage("UUID-123");
    message.setDateCommencement("2025-08-18");
    message.setDateSignalement("2025-08-19");
    message.setContenuCode("CODE123");
    message.setPointRepere("Arbre du centre");
    message.setDescription("Incendie observé");
    message.setSurfaceApproximative(45.5);
    message.setDirection("Nord");
    message.setElementsVisibles("Fumée visible");
    message.setDegats("Forêt brûlée");
    message.setIdUserApp(FIXED_USER_ID);

    long messageId = messageDao.insertMessage(message);

    Log.d("MAIN", "Message local inséré avec ID: " + messageId);

    // ---------- SYNCHRONISATION ----------
    SyncService syncService = new SyncService(this);

    // Messages
    syncService.downloadMessages(FIXED_USER_ID);
    // Interventions
    syncService.downloadInterventions();
    // Status Messages
    syncService.downloadStatusMessages();
    // Historique Messages
    syncService.downloadHistoriqueMessages(FIXED_USER_ID);

    // Afficher d'abord les données locales
    refreshUI();

    // Actualiser l'UI après un petit délai pour laisser le temps aux téléchargements (simplification)
    textView.postDelayed(this::refreshUI, 3000);
}

private void refreshUI() {
    StringBuilder builder = new StringBuilder();

    // Messages
    List<Message> messages = messageDao.getAllMessages();
    builder.append("📩 MESSAGES:\n");
    for (Message m : messages) builder.append("- ").append(m.getContenuCode()).append("\n");

    // Interventions
    List<Intervention> interventions = interventionDao.getAllInterventions();
    builder.append("\n🛠 INTERVENTIONS:\n");
    for (Intervention i : interventions) builder.append("- ").append(i.getIntervention()).append("\n");

    // Status Messages
    List<StatusMessage> statuses = statusDao.getAllStatus();
    builder.append("\n⚡ STATUS MESSAGES:\n");
    for (StatusMessage s : statuses) builder.append("- ").append(s.getStatus()).append("\n");

    // Historique Messages
    List<HistoriqueMessageStatus> historiques = historiqueDao.getAllHistorique();
    builder.append("\n📜 HISTORIQUE MESSAGES:\n");
    for (HistoriqueMessageStatus h : historiques) builder.append("- ").append(h.getIdHistorique()).append("\n");

    runOnUiThread(() -> textView.setText(builder.toString()));
}
// ...existing code...
}
