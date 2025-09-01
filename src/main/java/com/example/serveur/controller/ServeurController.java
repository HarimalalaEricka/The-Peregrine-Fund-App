package com.example.serveur.controller;

import com.example.serveur.model.SmsResponse;
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

    @Value("${gateway.auth.username}")
    private String expectedUsername;

    @Value("${gateway.auth.password}")
    private String expectedPassword;

    @Value("${gateway.internal-send-url}")
    private String internalSendUrl;

    public ServeurController(@Value("${encryption.secret-key}") String secretKey,
                            @Value("${sms.logfile.path}") String logFile,
                            @Value("${gateway.allowed-numbers}") String allowedNumbers,
                            RestTemplate restTemplate) {
        this.encryptionUtil = new EncryptionUtil(secretKey);
        this.logFile = logFile;
        this.numerosAutorises = Arrays.stream(allowedNumbers.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        System.out.println("✅ Serveur démarré avec " + numerosAutorises.size() + " numéros autorisés");
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
        try {
            messageClair = encryptionUtil.dechiffrer(messageChiffre);
            System.out.println("✅ Message déchiffré de " + phoneNumber + ": " + messageClair);
        } catch (Exception e) {
            messageClair = "[NON CHIFFRÉ] " + messageChiffre;
            System.out.println("⚠️  Message non chiffré de " + phoneNumber + ": " + messageClair);
        }

        // 4. LOGGING
        logSms(phoneNumber, messageClair, payload.getReceivedAt());

        // 5. ENVOI AUTOMATIQUE DE L'ACCUSÉ DE RÉCEPTION
        envoyerAccuseReception(phoneNumber);

        // 6. RÉPONSE
        return createSuccessResponse();
    }

    /**
     * Méthode pour envoyer automatiquement un accusé de réception
     */
    private void envoyerAccuseReception(String phoneNumber) {
        try {
            // Préparer le message accusé
            String messageAccuse = "✅ Message reçu et traité avec succès !";
            String messageChiffre = encryptionUtil.chiffrer(messageAccuse);
            
            // Préparer le corps de la requête pour l'API d'envoi
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("phoneNumbers", Collections.singletonList(phoneNumber));
            requestBody.put("message", messageChiffre);
            requestBody.put("withDeliveryReport", true);

            // Préparer l'authentification Basic Auth
            String authStr = expectedUsername + ":" + expectedPassword;
            String base64Auth = Base64.getEncoder().encodeToString(authStr.getBytes());

            // Configurer les en-têtes HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic " + base64Auth);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            // Appeler l'API d'envoi de SMS INTERNE
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
}