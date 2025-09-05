package com.example.serveur.controller;

import com.example.serveur.model.SmsResponse;
import com.example.serveur.service.SmsLoggingService;
import com.example.serveur.service.SmsProcessingService;
import com.example.serveur.service.SmsResponseService;
import com.example.serveur.service.AllowedNumbersService;
import com.example.serveur.service.AlerteService;
import com.example.serveur.service.SiteService;

import jakarta.annotation.PostConstruct;

import org.springframework.web.bind.annotation.*;

import com.example.serveur.util.*;

@RestController
@RequestMapping("/api")
public class ServeurController {

    private final SmsProcessingService smsProcessingService;
    private final SmsResponseService smsResponseService;
    private final SmsLoggingService smsLoggingService;
    private final AllowedNumbersService allowedNumbersService;
    private final AlerteService alerteService;
    private final SiteService siteService; // ← Nouveau service

    public ServeurController(SmsProcessingService smsProcessingService,
                            SmsResponseService smsResponseService,
                            SmsLoggingService smsLoggingService,
                            AllowedNumbersService allowedNumbersService,
                            AlerteService alerteService,
                            SiteService siteService) { // ← Injection
        this.smsProcessingService = smsProcessingService;
        this.smsResponseService = smsResponseService;
        this.smsLoggingService = smsLoggingService;
        this.allowedNumbersService = allowedNumbersService;
        this.alerteService = alerteService;
        this.siteService = siteService; // ← Initialisation
    }


    @PostConstruct
    public void init() {
        System.out.println("✅ Serveur démarré avec " + allowedNumbersService.getNumerosAutorises().size() + " numéros autorisés");
    }

    @GetMapping("/test-chiffrement")
    public String testChiffrement(@RequestParam String message) {
        EncryptionUtil encryptionUtil= new EncryptionUtil("0123456789abcdef");

        try {
            String dechiffre = encryptionUtil.dechiffrer(message);

            String chiffre = encryptionUtil.chiffrer(dechiffre);

            
            
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
    public SmsResponse handleSmsWebhook(@RequestBody GatewayWebhookRequest webhookRequest) throws Exception {

        if (!"sms:received".equals(webhookRequest.getEvent())) {
            return smsResponseService.createErrorResponse("Event non supporté");
        }

        GatewayWebhookRequest.Payload payload = webhookRequest.getPayload();
        String phoneNumber = payload.getPhoneNumber();
        String messageChiffre = payload.getMessage();

        if (!allowedNumbersService.isNumeroAutorise(phoneNumber)) {
            System.out.println("🚫 SMS ignoré de: " + phoneNumber);
            return smsResponseService.createSuccessResponse();
        }

        String messageClair = smsProcessingService.processMessage(messageChiffre, phoneNumber);
        SmsProcessingService.TypeMessage typeMessage = smsProcessingService.determineMessageType(messageClair);
        
        System.out.println("📨 Type de message détecté: " + typeMessage);

        String reponseAccuse = "✅ Message reçu et traité avec succès !";
        
        switch (typeMessage) {
            case MESSAGE_SIMPLE:
                reponseAccuse ="💬 Message simple - Traitement standard";
                break;
                
            case LOGIN:
                System.out.println("🔐 Tentative de login - Vérification credentials");
                boolean loginValide = smsProcessingService.processLogin(messageClair);
                reponseAccuse = loginValide ? 
                    "Connexion réussie! ID: " + smsProcessingService.getUserId(messageClair) : 
                    "Échec connexion: Identifiants invalides";
                break;
                
            case ALERTE:
                System.out.println("🚨 Alerte détectée - Traitement spécialisé");
                Integer idSite = siteService.determinerIdSite(phoneNumber);
                
                if (idSite != null) {
                    // Passez maintenant le numéro de téléphone aussi
                    reponseAccuse = alerteService.processAlerte(messageClair, idSite, phoneNumber);
                } else {
                    reponseAccuse = "❌ Impossible de déterminer le site";
                }
                break;
        }

        smsLoggingService.logSms(phoneNumber, messageClair, payload.getReceivedAt());
        smsResponseService.sendResponse(phoneNumber, reponseAccuse);

        return smsResponseService.createSuccessResponse();
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