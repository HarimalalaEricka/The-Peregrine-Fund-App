package com.example.theperegrinefund;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MainActivity extends AppCompatActivity {

    private EditText editMessage;
    private final String DEST_EMAIL = "bakomalalafenitra@gmail.com";
    private final String USERNAME = "harimalalaerickarandria@gmail.com";
    private final String PASSWORD = "mxoj bulo aybw hydl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editMessage = findViewById(R.id.editMessage);
        Button btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(v -> {
            String message = editMessage.getText().toString();
            if (message.isEmpty()) {
                Toast.makeText(this, "Veuillez écrire un message", Toast.LENGTH_SHORT).show();
            } else {
                sendEmail(message);
            }
        });
    }

    private void sendEmail(String content) {
        new Thread(() -> {
            try {
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
                props.put("mail.smtp.ssl.protocols", "TLSv1.2");

                Session session = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(USERNAME));
                message.setRecipients(Message.RecipientType.TO, 
                        InternetAddress.parse(DEST_EMAIL));
                message.setSubject("Nouveau message de l'application");
                message.setText(content);

                Transport.send(message);

                runOnUiThread(() -> {
                    editMessage.setText("");
                    Toast.makeText(MainActivity.this, 
                            "Email envoyé avec succès", 
                            Toast.LENGTH_LONG).show();
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, 
                            "Erreur d'envoi: " + e.getMessage(), 
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                });
            }
        }).start();
    }
}