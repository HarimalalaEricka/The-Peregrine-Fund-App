package com.example.theperegrinefund.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.security.ConfigLoader;
import com.example.security.CryptoUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText editName, editPassword;
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // ton layout XML

        // Récupération des champs
        editName = findViewById(R.id.edit_name);
        editPassword = findViewById(R.id.edit_password);
        btnSignIn = findViewById(R.id.btn_sign_in);

        // Action sur le bouton
        btnSignIn.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            try {
                // Charger la clé depuis le fichier de configuration
                String cle = ConfigLoader.getSecretKey();

                // Utiliser CryptoUtils pour chiffrer le mot de passe
                CryptoUtils crypto = new CryptoUtils(password);
                String chiffre = crypto.chiffrer(cle);

                // Démonstration : déchiffrer à nouveau
                String clair = CryptoUtils.dechiffrer(cle, chiffre);

                // Affichage avec Toast
                Toast.makeText(
                        this,
                        "Nom: " + name +
                                "\nMot de passe saisi: " + password +
                                "\nMot de passe chiffré: " + chiffre +
                                "\nMot de passe déchiffré: " + clair,
                        Toast.LENGTH_LONG
                ).show();

                // Ici ensuite tu pourras envoyer "chiffre" à ta base de données ou API

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Erreur chiffrement: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
