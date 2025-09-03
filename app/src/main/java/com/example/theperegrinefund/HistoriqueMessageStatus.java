package com.example.theperegrinefund;

import java.time.LocalDateTime;

public class HistoriqueMessageStatus {
    private int idHistorique;
    private LocalDateTime dateChangement;
    private int idStatusMessage; // clé étrangère vers StatusMessage
    private int idMessage;       // clé étrangère vers Message

    public HistoriqueMessageStatus() {
    }

    public HistoriqueMessageStatus(int idHistorique, LocalDateTime dateChangement, int idStatusMessage, int idMessage) {
        this.idHistorique = idHistorique;
        this.dateChangement = dateChangement;
        this.idStatusMessage = idStatusMessage;
        this.idMessage = idMessage;
    }

    public int getIdHistorique() {
        return idHistorique;
    }

    public void setIdHistorique(int idHistorique) {
        this.idHistorique = idHistorique;
    }

    public LocalDateTime getDateChangement() {
        return dateChangement;
    }

    public void setDateChangement(LocalDateTime dateChangement) {
        this.dateChangement = dateChangement;
    }

    public int getIdStatusMessage() {
        return idStatusMessage;
    }

    public void setIdStatusMessage(int idStatusMessage) {
        this.idStatusMessage = idStatusMessage;
    }

    public int getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(int idMessage) {
        this.idMessage = idMessage;
    }
}
