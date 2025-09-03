package com.example.serveur.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Alerte")
public class Alerte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_Alerte;

    @ManyToOne
    @JoinColumn(name = "Id_Site", nullable = false)
    private Site site;

    @ManyToOne
    @JoinColumn(name = "Id_Message", nullable = false)
    private Message message;

    @ManyToOne
    @JoinColumn(name = "Id_TypeAlerte", nullable = false)
    private TypeAlerte typeAlerte;

    // Getters et Setters

    public Integer getId_Alerte() {
        return id_Alerte;
    }

    public void setId_Alerte(Integer id_Alerte) {
        this.id_Alerte = id_Alerte;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public TypeAlerte getTypeAlerte() {
        return typeAlerte;
    }

    public void setTypeAlerte(TypeAlerte typeAlerte) {
        this.typeAlerte = typeAlerte;
    }
}
