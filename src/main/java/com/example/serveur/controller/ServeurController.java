package com.example.serveur.controller;

import com.example.serveur.model.SmsResponse;
import com.example.serveur.model.UserApp;
import com.example.serveur.repository.UserAppRepository;
import com.example.serveur.util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import jakarta.annotation.PostConstruct;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ServeurController {

    private final EncryptionUtil encryptionUtil;
    private final String logFile;
    private Set<String> numerosAutorises;
    private final RestTemplate restTemplate;
    private final String separateur;
    private final UserAppRepository userAppRepository;

    @Value("${gateway.auth.username}")
    private String expectedUsername;

    @Value("${gateway.auth.password}")
    private String expectedPassword;

    @Value("${gateway.internal-send-url}")
    private String internalSendUrl;

    public ServeurController(@Value("${encryption.secret-key}") String secretKey,
                            @Value("${sms.logfile.path}") String logFile,
                            @Value("${gateway.allowed-numbers}") String allowedNumbers,
                            @Value("${message.separator}") String separateur,
                            RestTemplate restTemplate,
                            UserAppRepository userAppRepository) {
        this.encryptionUtil = new EncryptionUtil(secretKey);
        this.logFile = logFile;
        this.numerosAutorises = Arrays.stream(allowedNumbers.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());
        this.restTemplate = restTemplate;
        this.separateur = separateur;
        this.userAppRepository = userAppRepository;
    }

    @PostConstruct
    public void init() {
        System.out.println("✅ Serveur démarré avec " + numerosAutorises.size() + " numéros autorisés");
    }

    @GetMapping("/test-chiffrement")
    public String testChiffrement(@RequestParam String message) {
        try {
            String chiffre = encryptionUtil.chiffrer(message);
            String dechiffre = encryptionUtil.dechiffrer(chiffre);
            
            return String.format("""
                Original: %s
                Chiffré: %s
                Déchiffré: %s
                Match: %s
                Longueur original: %d
                Longueur chiffré: %d
                """, 
                message, chiffre, dechiffre, 
                message.equals(dechiffre),
                message.length(), chiffre.length());
        } catch (Exception e) {
            return "Erreur: " + e.getMessage();
        }
    }

    @PostMapping("/webhook")
    public SmsResponse handleSmsWebhook(
            @RequestBody GatewayWebhookRequest webhookRequest) {

        // 1. VÉRIFICATION DE L'ÉVÉNEMENT
        if (!"sms:received".equals(webhookRequest.getEvent())) {
            return createErrorResponse("Event non supporté");
        }

        GatewayWebhookRequest.Payload payload = webhookRequest.getPayload();
        String phoneNumber = payload.getPhoneNumber();
        String messageChiffre = payload.getMessage();

        // 2. FILTRAGE PAR NUMÉRO
        if (!numerosAutorises.contains(phoneNumber)) {
            System.out.println("🚫 SMS ignoré de: " + phoneNumber);
            return createSuccessResponse();
        }

        // 3. DÉCHIFFREMENT
        String messageClair;
        boolean estChiffre = true;

        try {
            System.out.println("🔍 Tentative de déchiffrement: '" + messageChiffre + "'");
            messageClair = encryptionUtil.dechiffrer(messageChiffre);
            System.out.println("✅ Message déchiffré de " + phoneNumber + ": " + messageClair);
        } catch (Exception e) {
            messageClair = messageChiffre; // On garde le message original
            estChiffre = false;
            System.out.println("⚠️  Message non chiffré de " + phoneNumber + ": " + messageClair);
            System.out.println("⚠️  Raison du échec: " + e.getMessage());
            
            // Log supplémentaire pour debug
            System.out.println("🔍 Longueur du message: " + messageChiffre.length());
            System.out.println("🔍 Contient 'Carène': " + messageChiffre.contains("Carène"));
        }

        // 4. DÉTECTION DU TYPE DE MESSAGE
        int nbSeparateurs = compterOccurrences(messageClair, separateur);
        TypeMessage typeMessage = determinerTypeMessage(nbSeparateurs);
        
        System.out.println("📨 Type de message détecté: " + typeMessage + " (" + nbSeparateurs + " séparateur(s))");

        // 5. TRAITEMENT SELON LE TYPE
        String reponseAccuse = "✅ Message reçu et traité avec succès !";
        
        switch (typeMessage) {
            case MESSAGE_SIMPLE:
                System.out.println("💬 Message simple - Traitement standard");
                break;
                
            case LOGIN:
                System.out.println("🔐 Tentative de login - Vérification credentials");
                boolean loginValide = traiterLogin(messageClair);
                reponseAccuse = loginValide ? 
                    "🔐 Connexion réussie! ID: " + obtenirIdUser(messageClair) : 
                    "❌ Échec connexion: Identifiants invalides";
                break;
                
            case ALERTE:
                System.out.println("🚨 Alerte détectée - Traitement spécialisé");
                reponseAccuse = "🚨 Alerte reçue! Traitement en cours...";
                // Ici, on ajoutera la logique de traitement d'alerte
                break;
        }

        // 4. LOGGING
        logSms(phoneNumber, messageClair, payload.getReceivedAt());

        // 5. ENVOI AUTOMATIQUE DE L'ACCUSÉ DE RÉCEPTION
        envoyerAccuseReception(phoneNumber, reponseAccuse);

        // 6. RÉPONSE
        return createSuccessResponse();
    }
    
    /**
     * Traite une tentative de login
     */
    private boolean traiterLogin(String message) {
        try {
            String[] parties = message.split("\\" + separateur);
            if (parties.length != 2) {
                System.out.println("❌ Format de login invalide: " + message);
                return false;
            }
            
            String login = parties[0].trim();
            String motDePasse = parties[1].trim();
            
            System.out.println("🔐 Vérification login: " + login);
            
            // Vérification dans la base de données
            Optional<UserApp> userOpt = userAppRepository.findByLoginAndMotDePasse(login, motDePasse);
            
            if (userOpt.isPresent()) {
                UserApp user = userOpt.get();
                System.out.println("✅ Login réussi pour: " + login + " (ID: " + user.getIdUserApp() + ")");
                return true;
            } else {
                System.out.println("❌ Login échoué pour: " + login);
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors du traitement du login: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtient l'ID de l'utilisateur pour une réponse positive
     */
    private String obtenirIdUser(String message) {
        try {
            String[] parties = message.split("\\" + separateur);
            String login = parties[0].trim();
            
            Optional<UserApp> userOpt = userAppRepository.findByLogin(login);
            if (userOpt.isPresent()) {
                return userOpt.get().getIdUserApp().toString();
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la récupération de l'ID: " + e.getMessage());
        }
        return "inconnu";
    }

    /**
     * Envoi d'accusé de réception adapté au type de message
     */
    private void envoyerAccuseReception(String phoneNumber, String messageAccuse) {
        try {
            String messageChiffre = encryptionUtil.chiffrer(messageAccuse);
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("phoneNumbers", Collections.singletonList(phoneNumber));
            requestBody.put("message", messageChiffre);
            requestBody.put("withDeliveryReport", true);

            String authStr = expectedUsername + ":" + expectedPassword;
            String base64Auth = Base64.getEncoder().encodeToString(authStr.getBytes());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic " + base64Auth);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                internalSendUrl, 
                HttpMethod.POST, 
                requestEntity, 
                String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("✅ Accusé de réception envoyé à: " + phoneNumber);
            } else {
                System.err.println("❌ Échec envoi accusé. Statut: " + response.getStatusCode());
            }

        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'envoi de l'accusé: " + e.getMessage());
        }
    }
    
    /**
     * Compte le nombre d'occurrences d'un séparateur dans un message
     */
    private int compterOccurrences(String message, String separateur) {
        if (message == null || separateur == null || separateur.isEmpty()) {
            return 0;
        }
        
        int count = 0;
        int index = 0;
        while ((index = message.indexOf(separateur, index)) != -1) {
            count++;
            index += separateur.length();
        }
        return count;
    }

    /**
     * Détermine le type de message basé sur le nombre de séparateurs
     */
    private TypeMessage determinerTypeMessage(int nbSeparateurs) {
        if (nbSeparateurs == 0) {
            return TypeMessage.MESSAGE_SIMPLE;
        } else if (nbSeparateurs == 1) {
            return TypeMessage.LOGIN;
        } else {
            return TypeMessage.ALERTE;
        }
    }

    private void logSms(String from, String message, String timestamp) {
        String logLine = String.format("[%s] %s: %s\n",
                timestamp != null ? timestamp : LocalDateTime.now(),
                from,
                message);

        try (FileWriter fw = new FileWriter(logFile, true)) {
            fw.write(logLine);
        } catch (IOException e) {
            System.err.println("❌ Erreur écriture log: " + e.getMessage());
        }
    }

    private SmsResponse createSuccessResponse() {
        SmsResponse response = new SmsResponse();
        response.setPayload(new SmsResponse.Payload());
        response.getPayload().setSuccess(true);
        return response;
    }

    private SmsResponse createErrorResponse(String error) {
        SmsResponse response = new SmsResponse();
        response.setPayload(new SmsResponse.Payload());
        response.getPayload().setSuccess(false);
        response.getPayload().setError(error);
        return response;
    }

    // Classes internes pour le parsing du JSON
    public static class GatewayWebhookRequest {
        private String event;
        private Payload payload;
        
        public String getEvent() { return event; }
        public void setEvent(String event) { this.event = event; }
        public Payload getPayload() { return payload; }
        public void setPayload(Payload payload) { this.payload = payload; }

        public static class Payload {
            private String phoneNumber;
            private String message;
            private String receivedAt;
            
            public String getPhoneNumber() { return phoneNumber; }
            public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
            public String getMessage() { return message; }
            public void setMessage(String message) { this.message = message; }
            public String getReceivedAt() { return receivedAt; }
            public void setReceivedAt(String receivedAt) { this.receivedAt = receivedAt; }
        }
    }

    /**
     * Enumération des types de messages
     */
    private enum TypeMessage {
        MESSAGE_SIMPLE,
        LOGIN,
        ALERTE
    }
}