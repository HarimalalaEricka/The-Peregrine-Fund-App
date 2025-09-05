package com.example.serveur.service;

import com.example.serveur.model.Alerte;
import com.example.serveur.model.TypeAlerte;
import com.example.serveur.repository.AlerteRepository;
import com.example.serveur.repository.TypeAlerteRepository;
import com.example.serveur.repository.StatusMessageRepository;
import com.example.serveur.repository.InterventionRepository;
import org.springframework.stereotype.Service;

@Service
public class NiveauAlerteService {
    
    private final TypeAlerteRepository typeAlerteRepository;
    private final AlerteRepository alerteRepository;
    private final StatusMessageRepository statusMessageRepository;
    private final InterventionRepository interventionRepository;
    
    public NiveauAlerteService(TypeAlerteRepository typeAlerteRepository,
                              AlerteRepository alerteRepository,
                              StatusMessageRepository statusMessageRepository,
                              InterventionRepository interventionRepository) {
        this.typeAlerteRepository = typeAlerteRepository;
        this.alerteRepository = alerteRepository;
        this.statusMessageRepository = statusMessageRepository;     
        this.interventionRepository = interventionRepository;     
    }

    public String determinerNiveauAlerteParId(Integer idStatus, Integer idIntervention, Boolean renfort) {
        if (idStatus == null || idIntervention == null || renfort == null) {
            return "Inconnu";
        }
        
        // LOGIQUE REVISÉE - Ordre croissant de gravité
        if (idStatus == 3) { // Maîtrisé - Situation sous contrôle
            return "Vert";
        }
        else if (idStatus == 1) { // Début de feu - Situation naissante
            if (idIntervention == 1) { // Possible - Intervention réalisable
                return "Vert";
            } else if (idIntervention == 2) { // Partielle - Intervention partielle
                return "Jaune";
            } else if (idIntervention == 3) { // Impossible - Intervention impossible
                return renfort ? "Orange" : "Rouge"; // Avec renfort: Orange, Sans: Rouge
            }
        }
        else if (idStatus == 2) { // En cours - Situation évolutive
            if (idIntervention == 1) { // Possible 
                return "Jaune";
            } else if (idIntervention == 2) { // Partielle
                return renfort ? "Orange" : "Rouge"; // Avec renfort: Orange, Sans: Rouge
            } else if (idIntervention == 3) { // Impossible
                return "Rouge"; // Situation critique
            }
        }
        
        return "Inconnu";
    }
    
    /**
     * Crée une alerte dans la base de données
     */
    public void creerAlerte(Integer idSite, Integer idMessage, String niveauAlerte) {
        try {
            // Trouver le type d'alerte correspondant
            TypeAlerte typeAlerte = typeAlerteRepository.findByZone(niveauAlerte);
            
            if (typeAlerte != null) {
                Alerte alerte = new Alerte();
                alerte.setIdSite(idSite);
                alerte.setIdMessage(idMessage);
                alerte.setIdTypeAlerte(typeAlerte.getIdTypeAlerte());

                alerteRepository.save(alerte);
                
                System.out.println("✅ Alerte créée: Site=" + idSite + 
                                  ", Message=" + idMessage + 
                                  ", Niveau=" + niveauAlerte);
            } else {
                System.err.println("❌ Type d'alerte non trouvé: " + niveauAlerte);
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la création de l'alerte: " + e.getMessage());
        }
    }
    
    /**
     * Méthode complète pour traiter une alerte et déterminer son niveau
     */
    public String traiterAlerteComplete(String status, String intervention, Boolean renfort, 
                                      Integer idSite, Integer idMessage) {

        int idStatus = statusMessageRepository.findByStatus(status).getIdStatusMessage();
        int idIntervention = interventionRepository.findByType(intervention).getId_Intervention();
        String niveau = determinerNiveauAlerteParId(idStatus, idIntervention, renfort);
        
        if (!"Inconnu".equals(niveau)) {
            creerAlerte(idSite, idMessage, niveau);
        }
        
        return niveau;
    }
}