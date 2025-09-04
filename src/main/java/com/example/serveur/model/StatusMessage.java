package com.example.serveur.model;

import jakarta.persistence.*;

@Entity
@Table(name = "status_message")
public class StatusMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idStatusMessage;

    private String status;

    public StatusMessage() {}  // constructeur vide pour JPA

    public StatusMessage(int idStatusMessage, String status) {
        this.idStatusMessage = idStatusMessage;
        this.status = status;
    }

    // --- Getters & Setters ---
    public int getIdStatusMessage() {
        return idStatusMessage;
    }

    public void setIdStatusMessage(int idStatusMessage) {
        this.idStatusMessage = idStatusMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
