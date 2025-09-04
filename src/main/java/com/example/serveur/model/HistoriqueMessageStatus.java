package com.example.serveur.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historique_message_status")
public class HistoriqueMessageStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historique")
    private Long idHistorique;
    
    @Column(name = "date_changement")
    private LocalDateTime dateChangement;
    
    @Column(name = "id_status")
    private Integer idStatus;
    
    @Column(name = "id_message")
    private Integer idMessage;
    
    // Getters et setters
    public Long getIdHistorique() { return idHistorique; }
    public void setIdHistorique(Long idHistorique) { this.idHistorique = idHistorique; }
    
    public LocalDateTime getDateChangement() { return dateChangement; }
    public void setDateChangement(LocalDateTime dateChangement) { this.dateChangement = dateChangement; }
    
    public Integer getIdStatus() { return idStatus; }
    public void setIdStatus(Integer idStatus) { this.idStatus = idStatus; }
    
    public Integer getIdMessage() { return idMessage; }
    public void setIdMessage(Integer idMessage) { this.idMessage = idMessage; }
}