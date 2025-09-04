package com.example.serveur.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_message")
    private Long idMessage;
    
    @Column(name = "date_commencement", nullable = false)
    private LocalDateTime dateCommencement;
    
    @Column(name = "date_signalement", nullable = false, unique = true)
    private LocalDateTime dateSignalement;
    
    @Column(name = "pointrepere")
    private String pointRepere;
    
    @Column(name = "surface_approximative", precision = 15, scale = 2)
    private BigDecimal surfaceApproximative;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "direction", nullable = false, length = 20)
    private String direction;
    
    @Column(name = "renfort")
    private Boolean renfort;
    
    @Column(name = "longitude", precision = 15, scale = 8)
    private BigDecimal longitude;
    
    @Column(name = "latitude", precision = 15, scale = 8)
    private BigDecimal latitude;
    
    @Column(name = "id_intervention", nullable = false)
    private Integer idIntervention;
    
    @Column(name = "iduserapp", nullable = false)
    private Integer idUserApp;
    
    // Getters et setters
    public Long getIdMessage() { return idMessage; }
    public void setIdMessage(Long idMessage) { this.idMessage = idMessage; }
    
    public LocalDateTime getDateCommencement() { return dateCommencement; }
    public void setDateCommencement(LocalDateTime dateCommencement) { this.dateCommencement = dateCommencement; }
    
    public LocalDateTime getDateSignalement() { return dateSignalement; }
    public void setDateSignalement(LocalDateTime dateSignalement) { this.dateSignalement = dateSignalement; }
    
    public String getPointRepere() { return pointRepere; }
    public void setPointRepere(String pointRepere) { this.pointRepere = pointRepere; }
    
    public BigDecimal getSurfaceApproximative() { return surfaceApproximative; }
    public void setSurfaceApproximative(BigDecimal surfaceApproximative) { this.surfaceApproximative = surfaceApproximative; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }
    
    public Boolean getRenfort() { return renfort; }
    public void setRenfort(Boolean renfort) { this.renfort = renfort; }
    
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    
    public Integer getIdIntervention() { return idIntervention; }
    public void setIdIntervention(Integer idIntervention) { this.idIntervention = idIntervention; }
    
    public Integer getIdUserApp() { return idUserApp; }
    public void setIdUserApp(Integer idUserApp) { this.idUserApp = idUserApp; }
}