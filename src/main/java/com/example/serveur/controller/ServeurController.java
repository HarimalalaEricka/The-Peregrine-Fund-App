package com.example.serveur.controller;

import com.example.serveur.model.Serveur;
import org.springframework.web.bind.annotation.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class ServeurController {

    // Chemin absolu pour Windows (change selon ton dossier)
    private static final String LOG_FILE = "C:/Users/Nam/Documents/Stage/SERVEUR/sms_log.txt";

    @PostMapping("/sms-receiver")
    public Serveur receiveSms(@RequestParam String from,
                              @RequestParam String message,
                              @RequestParam(required = false) String sent) {

        // Préparer la ligne à écrire dans le fichier
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logLine = timestamp + " | From: " + from + " | Message: " + message + " | Sent: " + sent + "\n";

        // Écrire dans le fichier (ajoute à la fin)
        try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
            fw.write(logLine);
        } catch (IOException e) {
            e.printStackTrace(); // Affiche une erreur si le fichier ne peut pas être créé ou écrit
        }

        // Réponse à envoyer via SMSSync
        Serveur response = new Serveur();
        response.setFrom("SERVER");
        response.setMessage("Message reçu !");
        return response;
    }
}
