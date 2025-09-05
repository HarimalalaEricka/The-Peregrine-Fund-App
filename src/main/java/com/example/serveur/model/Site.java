package com.example.serveur.model;
import jakarta.persistence.*;
import java.math.BigDecimal;

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
}
