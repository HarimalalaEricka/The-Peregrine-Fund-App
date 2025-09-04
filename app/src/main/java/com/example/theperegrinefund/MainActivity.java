package com.example.theperegrinefund;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    
    private TextView welcomeText;
    private String userName;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // // Configuration de la toolbar
        // Toolbar toolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        
        // // Récupération du nom d'utilisateur depuis l'Intent
        // userName = getIntent().getStringExtra("user_name");
        
        // // Initialisation des vues
        // initializeViews();
        
        // // Affichage du message de bienvenue
        // displayWelcomeMessage();
    }
    
    /**
     * Initialise les composants UI
     */
    // private void initializeViews() {
    //     welcomeText = findViewById(R.id.welcome_text);
    // }
    
    /**
     * Affiche le message de bienvenue personnalisé
     */
    // private void displayWelcomeMessage() {
    //     if (userName != null && !userName.isEmpty()) {
    //         welcomeText.setText("Bienvenue, " + userName + "!");
    //     } else {
    //         welcomeText.setText("Bienvenue dans Raptor App!");
    //     }
    // }
    
    // @Override
    // public boolean onCreateOptionsMenu(Menu menu) {
    //     getMenuInflater().inflate(R.menu.main_menu, menu);
    //     return true;
    // }
    
    // @Override
    // public boolean onOptionsItemSelected(MenuItem item) {
    //     int id = item.getItemId();
        
    //     if (id == R.id.action_logout) {
    //         logout();
    //         return true;
    //     }
        
    //     return super.onOptionsItemSelected(item);
    // }
    
    /**
     * Gère la déconnexion de l'utilisateur
     */
    // private void logout() {
    //     // Redirection vers l'écran de connexion
    //     Intent intent = new Intent(MainActivity.this, LoginActivity.class);
    //     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    //     startActivity(intent);
    //     finish();
    // }
}