package com.example.serveur.model;

import jakarta.persistence.*;

@Entity
@Table(name = "alerte")
public class Alerte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alerte")
    private Long idAlerte;
    
    @Column(name = "id_site", nullable = false)
    private Integer idSite;
    
    @Column(name = "id_message", nullable = false)
    private Integer idMessage;
    
    @Column(name = "id_typealerte", nullable = false)
    private Integer idTypeAlerte;
    
    // Getters et setters
    public Long getIdAlerte() { return idAlerte; }
    public void setIdAlerte(Long idAlerte) { this.idAlerte = idAlerte; }
    
    public Integer getIdSite() { return idSite; }
    public void setIdSite(Integer idSite) { this.idSite = idSite; }
    
    public Integer getIdMessage() { return idMessage; }
    public void setIdMessage(Integer idMessage) { this.idMessage = idMessage; }
    
    public Integer getIdTypeAlerte() { return idTypeAlerte; }
    public void setIdTypeAlerte(Integer idTypeAlerte) { this.idTypeAlerte = idTypeAlerte; }
}