package com.example.theperegrinefund;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.theperegrinefund.security.ConfigLoader;
import com.example.theperegrinefund.security.CryptoUtils;
import com.example.theperegrinefund.security.CredentialUtil;
import com.example.theperegrinefund.UserApp;
import com.example.theperegrinefund.SmsSender;

public class LoginActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private final String FIXED_NUMBER = "0341638587"; // destinataire fixe

    private EditText editName, editPassword;
    private Button btnSignIn;
    private SmsSender smsSender;

    private String lastChiffre; // on garde le dernier message chiffré

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Récupération des champs
        editName = findViewById(R.id.edit_name);
        editPassword = findViewById(R.id.edit_password);
        btnSignIn = findViewById(R.id.btn_sign_in);

        smsSender = new SmsSender();

        // Action sur le bouton
        btnSignIn.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            try {
                // Charger la clé depuis le fichier de configuration
               String cle = ConfigLoader.getSecretKey(this);  // 'this' = contexte de l'activité

                String combined = CredentialUtil.combineNamePassword(name, password);

                // Utiliser CryptoUtils pour chiffrer
                CryptoUtils crypto = new CryptoUtils(combined);
                lastChiffre = crypto.chiffrer(cle);

                // Vérifier permission avant d’envoyer
                if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(LoginActivity.this,
                            new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);
                } else {
                    sendMessage();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Erreur chiffrement: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage() {
        if (lastChiffre == null || lastChiffre.isEmpty()) {
            Toast.makeText(this, "Aucun message chiffré à envoyer", Toast.LENGTH_SHORT).show();
            return;
        }

        UserApp message = new UserApp(FIXED_NUMBER, lastChiffre);

        try {
            smsSender.send(message);
            Toast.makeText(this, "Message chiffré envoyé", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Erreur envoi SMS : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendMessage();
            } else {
                Toast.makeText(this, "Permission SMS refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
