package com.example.theperegrinefund;

import android.content.Intent;
import android.widget.Button;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    
    // Déclaration des composants UI
    private TextInputEditText editName;
    private TextInputEditText editPassword;
    private MaterialButton btnSignIn;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button signInButton = findViewById(R.id.btn_sign_in);
        signInButton.setOnClickListener(v -> {
            // Simplement ouvrir le dashboard
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
        });
        
        // // Initialisation des composants UI
        // initializeViews();
        
        // // Configuration des événements
        // setupClickListeners();
    }
    
    // /**
    //  * Initialise les références vers les composants UI
    //  */
    // private void initializeViews() {
    //     editName = findViewById(R.id.edit_name);
    //     editPassword = findViewById(R.id.edit_password);
    //     btnSignIn = findViewById(R.id.btn_sign_in);
    // }
    
    // /**
    //  * Configure les listeners pour les événements de clic
    //  */
    // private void setupClickListeners() {
    //     btnSignIn.setOnClickListener(new View.OnClickListener() {
    //         @Override
    //         public void onClick(View v) {
    //             handleSignIn();
    //         }
    //     });
    // }
    
    // /**
    //  * Gère la logique de connexion
    //  */
    // private void handleSignIn() {
    //     String name = editName.getText().toString().trim();
    //     String password = editPassword.getText().toString().trim();
        
    //     // Validation des champs
    //     if (name.isEmpty()) {
    //         editName.setError("Le nom est requis");
    //         editName.requestFocus();
    //         return;
    //     }
        
    //     if (password.isEmpty()) {
    //         editPassword.setError("Le mot de passe est requis");
    //         editPassword.requestFocus();
    //         return;
    //     }
        
    //     // Simulation de connexion (remplacez par votre logique)
    //     if (validateCredentials(name, password)) {
    //         // Connexion réussie
    //         Toast.makeText(this, "Connexion réussie!", Toast.LENGTH_SHORT).show();
            
    //         // Redirection vers MainActivity
    //         Intent intent = new Intent(LoginActivity.this, MainActivity.class);
    //         intent.putExtra("user_name", name);
    //         startActivity(intent);
    //         finish(); // Ferme l'activité de connexion
    //     } else {
    //         // Échec de la connexion
    //         Toast.makeText(this, "Nom d'utilisateur ou mot de passe incorrect", Toast.LENGTH_LONG).show();
    //     }
    // }
    
    // /**
    //  * Valide les informations d'identification
    //  * @param name Nom d'utilisateur
    //  * @param password Mot de passe
    //  * @return true si les informations sont valides, false sinon
    //  */
    // private boolean validateCredentials(String name, String password) {
    //     // Logique de validation simple (à remplacer par votre système d'authentification)
    //     // Exemple : vérification avec une base de données, API, etc.
    //     return name.equals("admin") && password.equals("password123");
    // }
    
    // @Override
    // public void onBackPressed() {
    //     // Optionnel: confirmer avant de quitter l'application
    //     super.onBackPressed();
    //     finishAffinity(); // Ferme complètement l'application
    // }
}