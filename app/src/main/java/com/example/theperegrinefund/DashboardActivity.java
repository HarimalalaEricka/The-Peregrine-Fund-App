package com.example.theperegrinefund;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.theperegrinefund.dao.MessageDao;
import com.example.theperegrinefund.Message;
import android.util.Log;
import com.example.theperegrinefund.service.SyncService;
import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;
import com.example.theperegrinefund.HistoryItem;
import com.example.theperegrinefund.HistoryAdapter;
import com.example.theperegrinefund.Intervention;
import com.example.theperegrinefund.dao.InterventionDao;
import com.example.theperegrinefund.StatusMessage;
import com.example.theperegrinefund.dao.StatusMessageDao;
import com.example.theperegrinefund.HistoriqueMessageStatus;
import com.example.theperegrinefund.dao.HistoriqueMessageStatusDao;

public class DashboardActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private RecyclerView historyRecyclerView;
    private HistoryAdapter historyAdapter;
    private List<HistoryItem> historyItems;
    private ImageView menuIcon;
    private ImageView newIcon;
    private ImageView infoIcon;
    private static final int FIXED_USER_ID = 1;

    @Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dashboard);

    // Initialisation du Drawer et du contenu principal
    DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
    LinearLayout mainContent = findViewById(R.id.main_content); // LinearLayout contenant tout le contenu principal

    // Décalage du contenu principal quand le drawer s'ouvre
    drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            mainContent.setTranslationX(slideOffset * drawerView.getWidth());
        }
    });

    // Initialisation des autres vues (menuIcon, RecyclerView, etc.)
    menuIcon = findViewById(R.id.menu_icon);
    historyRecyclerView = findViewById(R.id.history_recycler_view);

    // Clic sur le bouton menu
    menuIcon.setOnClickListener(v -> {
        if (drawerLayout.isDrawerOpen(findViewById(R.id.history_drawer))) {
            drawerLayout.closeDrawer(findViewById(R.id.history_drawer));
        } else {
            drawerLayout.openDrawer(findViewById(R.id.history_drawer));
        }
    });

    // Initialisation des autres vues (newIcon, RecyclerView, etc.)
    newIcon = findViewById(R.id.new_icon);
    infoIcon = findViewById(R.id.info_icon);
        
    // Synchronisation des données au démarrage
    MessageDao messageDao = new MessageDao(this);
   // Dans votre onCreate, modifiez la séquence de synchronisation
    SyncService syncService = new SyncService(this);

    // Synchronisez d'abord les statuts
    syncService.downloadStatus(new SyncService.StatusCallback() {
        @Override
        public void onComplete(List<StatusMessage> statusMessages) {
            Log.d("SYNC", "Statuts synchronisés : " + statusMessages.size());
            
            // Ensuite, synchronisez les interventions
            syncService.downloadIntervention(new SyncService.InterventionCallback() {
                @Override
                public void onComplete(List<Intervention> interventions) {
                    Log.d("SYNC", "Interventions synchronisées : " + interventions.size());
                    
                    // Puis synchronisez les messages
            syncService.downloadMessages(FIXED_USER_ID, new SyncService.MessageCallback() {
                        @Override
                        public void onComplete(List<Message> messages) {
                            Log.d("SYNC", "Messages synchronisés : " + messages.size());
                            
                            // Enfin, synchronisez l'historique
            syncService.downloadHistorique(FIXED_USER_ID, new SyncService.HistoriqueCallback() {
                                @Override
                                public void onComplete(List<HistoriqueMessageStatus> historiques) {
                                    Log.d("SYNC", "Historique synchronisé : " + historiques.size());
                                    runOnUiThread(() -> {
                                        Toast.makeText(DashboardActivity.this, "Synchronisation terminée", Toast.LENGTH_SHORT).show();
                                        loadSampleData();
                                    });
                                }

                                @Override
                                public void onError(Exception e) {
                                    Log.e("SYNC", "Erreur de synchronisation historique", e);
                                }
                            });
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("SYNC", "Erreur de synchronisation messages", e);
                        }
                    });
                }

                @Override
                public void onError(Exception e) {
                    Log.e("SYNC", "Erreur de synchronisation interventions", e);
                }
            });
        }

        @Override
        public void onError(Exception e) {
            Log.e("SYNC", "Erreur de synchronisation statuts", e);
        }
    });


            // Clic sur le bouton new
            newIcon.setOnClickListener(v -> {
                Intent intent = new Intent(DashboardActivity.this, BaseActivity.class);
                startActivity(intent);
            });

            infoIcon.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, StatActivity.class)));

            // Reste du code : RecyclerView, chargement des données, etc.
            historyItems = new ArrayList<>();
            historyAdapter = new HistoryAdapter(historyItems, this::onHistoryItemClick);
            historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            historyRecyclerView.setAdapter(historyAdapter);
            loadSampleData();
        }


    private void loadSampleData() {
        historyItems.clear();

        MessageDao messageDao = new MessageDao(this);
        List<Message> messages = messageDao.getAllMessages();

        for (Message msg : messages) {
            historyItems.add(new HistoryItem(
                    msg.getDescription() + " (" + msg.getDateCommencement() + ")",
                    false,
                    msg.getIdMessage() // Ajoutez le troisième paramètre
            ));
        }

        historyAdapter.notifyDataSetChanged();
    }

    private void showMessageDetails(int messageId) {
    MessageDao messageDao = new MessageDao(this);
    HistoriqueMessageStatusDao historiqueDao = new HistoriqueMessageStatusDao(this);
    Message message = messageDao.getMessageById(messageId);

    if (message != null) {
        // Trouvez les vues dans votre CardView
        TextView tvDateTime = findViewById(R.id.tv_datetime);
        TextView tvSignalement = findViewById(R.id.tv_signalement);
        TextView tvSurface = findViewById(R.id.tv_surface);
        TextView tvDescription = findViewById(R.id.tv_description);
        TextView tvHistoryContent = findViewById(R.id.tv_history_content);
        Button btnEnCours = findViewById(R.id.btn_en_cours);
        Button btnMaitrise = findViewById(R.id.btn_maitrise);

        // Mettez à jour les vues avec les données du message (seulement les valeurs)
        tvDateTime.setText(message.getDateCommencement());
        tvSignalement.setText(message.getDateSignalement());
        tvSurface.setText(message.getSurfaceApproximative() + " ha");
        tvDescription.setText(message.getDescription());

        // Récupérez et affichez l'historique des statuts
        List<HistoriqueMessageStatus> statusHistory = historiqueDao.getStatusHistoryForMessage(messageId);
        StringBuilder historyText = new StringBuilder();
        
        if (statusHistory.isEmpty()) {
            historyText.append("Aucun historique disponible");
        } else {
            for (HistoriqueMessageStatus history : statusHistory) {
                // Formatez la date pour n'afficher que la partie date
                String dateOnly = history.getDateChangement().split(" ")[0];
                
                historyText.append("• ")
                          .append(dateOnly)
                          .append(" - ")
                          .append(history.getStatus().getStatus())
                          .append("\n\n");
            }
        }
        
        tvHistoryContent.setText(historyText.toString());
        
        // Déterminez le statut actuel et configurez les boutons en conséquence
        String currentStatus = getCurrentStatus(statusHistory); // Méthode à implémenter
        
        if ("Début".equals(currentStatus)) {
            btnEnCours.setVisibility(View.VISIBLE);
            btnMaitrise.setVisibility(View.VISIBLE);
        } else if ("En cours".equals(currentStatus)) {
            btnEnCours.setVisibility(View.GONE);
            btnMaitrise.setVisibility(View.VISIBLE);
        } else {
            btnEnCours.setVisibility(View.GONE);
            btnMaitrise.setVisibility(View.GONE);
        }
        
        // Configurez les écouteurs de clic pour les boutons
        btnEnCours.setOnClickListener(v -> updateStatus(messageId, "En cours"));
        btnMaitrise.setOnClickListener(v -> updateStatus(messageId, "Maîtrisé"));
    }
}

// Méthode pour déterminer le statut actuel
private String getCurrentStatus(List<HistoriqueMessageStatus> statusHistory) {
    if (statusHistory == null || statusHistory.isEmpty()) {
        return "Début";
    }
    // Retourne le statut le plus récent
    return statusHistory.get(0).getStatus().getStatus();
}

// Méthode pour mettre à jour le statut
private void updateStatus(int messageId, String newStatus) {
    // Implémentez la logique de mise à jour du statut ici
    // Cela peut inclure :
    // 1. Envoyer la mise à jour au serveur
    // 2. Mettre à jour la base de données locale
    // 3. Rafraîchir l'affichage
    
    Toast.makeText(this, "Statut changé en: " + newStatus, Toast.LENGTH_SHORT).show();
    
    // Rafraîchir l'affichage
    showMessageDetails(messageId);
}
    private void onHistoryItemClick(HistoryItem item, int position) {
        for (HistoryItem historyItem : historyItems) {
            historyItem.setSelected(false);
        }
        item.setSelected(true);
        historyAdapter.notifyDataSetChanged();
        
        // Affichez les détails du message
        showMessageDetails(item.getMessageId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (id == R.id.action_logout) {
            logout();
            return true;
        } else if (id == R.id.action_refresh) {
            refreshData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshData() {
        loadSampleData();
    }

    private void logout() {
        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(findViewById(R.id.history_drawer))) {
            drawerLayout.closeDrawer(findViewById(R.id.history_drawer));
        } else {
            super.onBackPressed();
        }
    }
}
