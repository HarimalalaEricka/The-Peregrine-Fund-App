package com.example.serveur.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "Site")
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_Site;

    @Column(nullable = false)
    private String Nom;

    @Column(nullable = false)
    private String Region;

    private BigDecimal Surface;
    private String Decree;

    private BigDecimal latitude;
    private BigDecimal longitude;

    // Exemple de relation avec Patrouilleurs si besoin
    @OneToMany(mappedBy = "site")
    private List<Patrouilleurs> patrouilleurs;

    // Exemple de relation avec Alerte
    @OneToMany(mappedBy = "site")
    private List<Alerte> alertes;

    // Getters et Setters
    public Integer getId_Site() { return id_Site; }
    public void setId_Site(Integer id_Site) { this.id_Site = id_Site; }

    public String getNom() { return Nom; }
    public void setNom(String Nom) { this.Nom = Nom; }

    public String getRegion() { return Region; }
    public void setRegion(String Region) { this.Region = Region; }

    public BigDecimal getSurface() { return Surface; }
    public void setSurface(BigDecimal Surface) { this.Surface = Surface; }

    public String getDecree() { return Decree; }
    public void setDecree(String Decree) { this.Decree = Decree; }

    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }

    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }

    public List<Patrouilleurs> getPatrouilleurs() { return patrouilleurs; }
    public void setPatrouilleurs(List<Patrouilleurs> patrouilleurs) { this.patrouilleurs = patrouilleurs; }

    public List<Alerte> getAlertes() { return alertes; }
    public void setAlertes(List<Alerte> alertes) { this.alertes = alertes; }
}
