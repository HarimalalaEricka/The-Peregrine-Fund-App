package com.example.serveur.service;

import com.example.serveur.model.HistoriqueMessageStatus;
import com.example.serveur.model.Message;
import com.example.serveur.repository.HistoriqueMessageStatusRepository;
import com.example.serveur.repository.MessageRepository;
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
    
    public AlerteService(MessageRepository messageRepository, 
                        HistoriqueMessageStatusRepository historiqueRepository) {
        this.messageRepository = messageRepository;
        this.historiqueRepository = historiqueRepository;
    }
    
    /**
     * Traite un message d'alerte et l'insère en base de données
     */
    @Transactional
    public String processAlerte(String messageAlerte) {
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
            BigDecimal longitude = parseBigDecimal(parties[9]);
            BigDecimal latitude = parseBigDecimal(parties[10]);
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

            if (isDuplicateMessage(dateCommencement, dateSignalement, idIntervention, idUserApp)) {
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
            
            return "Alerte enregistrée avec succès! ID: " + savedMessage.getIdMessage();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur détaillée lors du traitement de l'alerte: " + e.getMessage());
            e.printStackTrace();
            return "❌ Erreur lors du traitement de l'alerte: " + e.getMessage();
        }
    }

    private boolean isDuplicateMessage(LocalDateTime dateCommencement, 
                                     LocalDateTime dateSignalement, 
                                     Integer idIntervention, 
                                     Integer idUserApp) {
        return messageRepository.existsByDetails(
            dateCommencement, dateSignalement, idIntervention, idUserApp);
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