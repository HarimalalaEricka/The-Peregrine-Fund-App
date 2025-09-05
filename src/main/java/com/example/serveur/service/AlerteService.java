package com.example.serveur.service;

import com.example.serveur.model.HistoriqueMessageStatus;
import com.example.serveur.model.Intervention;
import com.example.serveur.model.Message;
import com.example.serveur.model.StatusMessage;
import com.example.serveur.repository.HistoriqueMessageStatusRepository;
import com.example.serveur.repository.MessageRepository;
import com.example.serveur.repository.InterventionRepository;
import com.example.serveur.repository.StatusMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class AlerteService {
    
    private final MessageRepository messageRepository;
    private final HistoriqueMessageStatusRepository historiqueRepository;
    private final InterventionRepository interventionRepository;
    private final StatusMessageRepository statusMessageRepository;
    private final NiveauAlerteService niveauAlerteService;
    private final InfoRetourService infoRetourService; // ← Nouveau service
    private final SmsResponseService smsResponseService; // ← Pour envoyer la réponse
    
    public AlerteService(MessageRepository messageRepository,
                        HistoriqueMessageStatusRepository historiqueRepository,
                        InterventionRepository interventionRepository,
                        StatusMessageRepository statusMessageRepository,
                        NiveauAlerteService niveauAlerteService,
                        InfoRetourService infoRetourService,
                        SmsResponseService smsResponseService) {
        this.messageRepository = messageRepository;
        this.historiqueRepository = historiqueRepository;
        this.interventionRepository = interventionRepository;
        this.statusMessageRepository = statusMessageRepository;
        this.niveauAlerteService = niveauAlerteService;
        this.infoRetourService = infoRetourService;
        this.smsResponseService = smsResponseService;
    }
    
    /**
     * Traite un message d'alerte et l'insère en base de données
     */
    @Transactional
    public String processAlerte(String messageAlerte, Integer idSite, String phoneNumber) {
        try {
            String[] parties = messageAlerte.split("/", -1);
            
            if (parties.length != 12) {
                return "❌ Format invalide: " + (parties.length - 1) + 
                       " séparateurs trouvés (12 attendus). Éléments manquants.";
            }
            
            // Debug: Afficher toutes les parties
            System.out.println("🔍 Parties du message:");
            for (int i = 0; i < parties.length; i++) {
                System.out.println("  [" + i + "]: '" + parties[i] + "'");
            }
            
            // Parser les données avec validation renforcée
            LocalDateTime dateCommencement = parseDateTime(parties[0]);
            LocalDateTime dateSignalement = parseDateTime(parties[1]);
            Integer idIntervention = parseInt(parties[2]);
            Boolean renfort = parseBoolean(parties[3]);
            String direction = emptyToNull(parties[4]);
            BigDecimal surfaceApproximative = parseBigDecimal(parties[5]);
            String pointRepere = emptyToNull(parties[6]);
            String description = emptyToNull(parties[7]);
            Integer idUserApp = parseInt(parties[8]); // Ceci est crucial!
            BigDecimal longitude = parseBigDecimal(parties[10]);
            BigDecimal latitude = parseBigDecimal(parties[9]);
            Integer idStatus = parseInt(parties[11]);
            
            // Validation renforcée des champs obligatoires
            if (dateCommencement == null) {
                return "❌ Date de commencement manquante ou invalide";
            }
            if (dateSignalement == null) {
                return "❌ Date de signalement manquante ou invalide";
            }
            if (idIntervention == null) {
                return "❌ ID intervention manquant ou invalide";
            }
            if (direction == null) {
                return "❌ Direction manquante ou invalide";
            }
            if (idUserApp == null) {
                return "❌ ID UserApp manquant ou invalide: '" + parties[8] + "'";
            }
            if (idStatus == null) {
                return "❌ ID Status manquant ou invalide";
            }
            
            System.out.println("✅ Données parsées - ID UserApp: " + idUserApp);

            if (isDuplicateMessage(longitude, latitude)) {
                return "⚠️ Message déjà existant - Doublon ignoré";
            }
            
            // Créer et sauvegarder le message
            Message message = new Message();
            message.setDateCommencement(dateCommencement);
            message.setDateSignalement(dateSignalement);
            message.setIdIntervention(idIntervention);
            message.setRenfort(renfort);
            message.setDirection(direction);
            message.setSurfaceApproximative(surfaceApproximative);
            message.setPointRepere(pointRepere);
            message.setDescription(description);
            message.setIdUserApp(idUserApp); // Ceci doit être bien défini!
            message.setLongitude(longitude);
            message.setLatitude(latitude);
            
            Message savedMessage = messageRepository.save(message);
            
            // Créer l'historique de statut
            HistoriqueMessageStatus historique = new HistoriqueMessageStatus();
            historique.setDateChangement(LocalDateTime.now());
            historique.setIdStatus(idStatus);
            historique.setIdMessage(savedMessage.getIdMessage().intValue());
            
            historiqueRepository.save(historique);
            
            // DÉTERMINER LE NIVEAU D'ALERTE
            // Récupérer le texte du status et de l'intervention
            String statusText = getStatusTextById(idStatus);
            String interventionText = getInterventionTextById(idIntervention);

            System.out.println("🔍 Debug Alerte - Status: " + statusText + 
                  ", Intervention: " + interventionText + 
                  ", Renfort: " + renfort);
            
            niveauAlerteService.traiterAlerteComplete(
                statusText, interventionText, renfort, idSite, savedMessage.getIdMessage().intValue());

            // GÉNÉRER ET ENVOYER L'INFO DE RETOUR
            String infoRetour = infoRetourService.genererInfoRetour(savedMessage);
            smsResponseService.sendResponse(phoneNumber, infoRetour);
            
            // return "✅ Alerte enregistrée avec succès! ID: " + savedMessage.getIdMessage() + 
            //        ", Niveau: " + niveauAlerte;

            return infoRetour;
            
        } catch (Exception e) {
            System.err.println("❌ Erreur détaillée lors du traitement de l'alerte: " + e.getMessage());
            e.printStackTrace();
            return "❌ Erreur lors du traitement de l'alerte: " + e.getMessage();
        } 
    }

    private String getStatusTextById(Integer idStatus) {
        if (idStatus == null) return "Inconnu";
        try {
            return statusMessageRepository.findById(idStatus)
                .map(StatusMessage::getStatus)
                .orElse("Inconnu");
        } catch (Exception e) {
            return "Inconnu";
        }
    }
    
    private String getInterventionTextById(Integer idIntervention) {
        if (idIntervention == null) return "Inconnu";
        try {
            return interventionRepository.findById(idIntervention.longValue())
                .map(Intervention::getIntervention)
                .orElse("Inconnu");
        } catch (Exception e) {
            return "Inconnu";
        }
    }

    private boolean isDuplicateMessage(BigDecimal Longitude, BigDecimal Latitude) {
        return messageRepository.existsByDetails(
            Longitude, Latitude);
    }

    // Méthodes utilitaires pour le parsing
    private LocalDateTime parseDateTime(String value) {
        if (value == null || value.trim().isEmpty() || "null".equalsIgnoreCase(value)) {
            return null;
        }
        try {
            // Essayez différents formats de date/heure
            DateTimeFormatter[] formatters = {
                DateTimeFormatter.ISO_LOCAL_DATE_TIME,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            };
            
            for (DateTimeFormatter formatter : formatters) {
                try {
                    if (value.length() <= 10) {
                        // Juste la date
                        return LocalDateTime.parse(value + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    } else {
                        // Date et heure
                        return LocalDateTime.parse(value, formatter);
                    }
                } catch (DateTimeParseException e) {
                    // Continuer avec le formateur suivant
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    private Integer parseInt(String value) {
        if (value == null || value.trim().isEmpty() || "null".equalsIgnoreCase(value)) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            System.err.println("❌ Impossible de parser en integer: '" + value + "'");
            return null;
        }
    }
    
    private Boolean parseBoolean(String value) {
        if (value == null || value.trim().isEmpty() || "null".equalsIgnoreCase(value)) {
            return null;
        }
        String lowerValue = value.trim().toLowerCase();
        return "true".equals(lowerValue) || "1".equals(lowerValue) || "oui".equals(lowerValue);
    }
    
    private BigDecimal parseBigDecimal(String value) {
        if (value == null || value.trim().isEmpty() || "null".equalsIgnoreCase(value)) {
            return null;
        }
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    private String emptyToNull(String value) {
        if (value == null || value.trim().isEmpty() || "null".equalsIgnoreCase(value)) {
            return null;
        }
        return value.trim();
    }
}