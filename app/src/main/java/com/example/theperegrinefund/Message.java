package com.example.theperegrinefund;

import com.google.gson.annotations.SerializedName;
public class Message {

    private int idMessage;
    private String dateCommencement;
    private String dateSignalement;
    private String pointRepere;
    private double surfaceApproximative;
    private String description;
    private String direction;
    private boolean renfort;
    private double longitude;
    private double latitude;
    private int idIntervention;

    // Champ utilisé côté mobile pour la synchro
    private int idUserApp;

   

    // --- Getters et setters ---
    public int getIdMessage() { return idMessage; }
    public void setIdMessage(int idMessage) { this.idMessage = idMessage; }

    public String getDateCommencement() { return dateCommencement; }
    public void setDateCommencement(String dateCommencement) { this.dateCommencement = dateCommencement; }

    public String getDateSignalement() { return dateSignalement; }
    public void setDateSignalement(String dateSignalement) { this.dateSignalement = dateSignalement; }

    public String getPointRepere() { return pointRepere; }
    public void setPointRepere(String pointRepere) { this.pointRepere = pointRepere; }

    public double getSurfaceApproximative() { return surfaceApproximative; }
    public void setSurfaceApproximative(double surfaceApproximative) { this.surfaceApproximative = surfaceApproximative; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }

    public boolean isRenfort() { return renfort; }
    public void setRenfort(boolean renfort) { this.renfort = renfort; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public int getIdIntervention() { return idIntervention; }
    public void setIdIntervention(int idIntervention) { this.idIntervention = idIntervention; }

    public int getIdUserApp() { return idUserApp; }
    public void setIdUserApp(int idUserApp) { this.idUserApp = idUserApp; }

  
}