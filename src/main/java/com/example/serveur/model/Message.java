package com.example.serveur.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMessage;

    private Date dateSignalement;
    private String description;
    private String pointRepere;

    @ManyToOne
    @JoinColumn(name = "id_user_app") // colonne dans la table
    private UserApp user;

    // Champs optionnels
    private Double surfaceApproximative;
    private String direction;
    private String elementsVisibles;
    private String degats;
    private String contenuCode;

    public Message() {}  // constructeur vide pour JPA

    public Message(int idMessage, Date dateSignalement, String description, String pointRepere, UserApp user) {
        this.idMessage = idMessage;
        this.dateSignalement = dateSignalement;
        this.description = description;
        this.pointRepere = pointRepere;
        this.user = user;
    }

    // --- Getters & Setters ---
    public int getIdMessage() { return idMessage; }
    public void setIdMessage(int idMessage) { this.idMessage = idMessage; }

    public Date getDateSignalement() { return dateSignalement; }
    public void setDateSignalement(Date dateSignalement) { this.dateSignalement = dateSignalement; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPointRepere() { return pointRepere; }
    public void setPointRepere(String pointRepere) { this.pointRepere = pointRepere; }

    public UserApp getUser() { return user; }
    public void setUser(UserApp user) { this.user = user; }

    public Double getSurfaceApproximative() { return surfaceApproximative; }
    public void setSurfaceApproximative(Double surfaceApproximative) { this.surfaceApproximative = surfaceApproximative; }

    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }

    public String getElementsVisibles() { return elementsVisibles; }
    public void setElementsVisibles(String elementsVisibles) { this.elementsVisibles = elementsVisibles; }

    public String getDegats() { return degats; }
    public void setDegats(String degats) { this.degats = degats; }

    public String getContenuCode() { return contenuCode; }
    public void setContenuCode(String contenuCode) { this.contenuCode = contenuCode; }
}
