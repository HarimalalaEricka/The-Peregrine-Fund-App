package com.example.theperegrinefund;

public class Message {
    private int idMessage;
    private String uuidMessage;
    private String dateCommencement;
    private String dateSignalement;
    private String contenuCode;
    private String pointRepere;
    private String description;
    private double surfaceApproximative;
    private String direction;
    private String elementsVisibles;
    private String degats;
    private int intervention;
    private int renfort;
    private double longitude;
    private double latitude;
    private int idIntervention;
    private int idUserApp;

    // --- Getters et Setters ---

    public int getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(int idMessage) {
        this.idMessage = idMessage;
    }

    public String getUuidMessage() {
        return uuidMessage;
    }

    public void setUuidMessage(String uuidMessage) {
        this.uuidMessage = uuidMessage;
    }

    public String getDateCommencement() {
        return dateCommencement;
    }

    public void setDateCommencement(String dateCommencement) {
        this.dateCommencement = dateCommencement;
    }

    public String getDateSignalement() {
        return dateSignalement;
    }

    public void setDateSignalement(String dateSignalement) {
        this.dateSignalement = dateSignalement;
    }

    public String getContenuCode() {
        return contenuCode;
    }

    public void setContenuCode(String contenuCode) {
        this.contenuCode = contenuCode;
    }

    public String getPointRepere() {
        return pointRepere;
    }

    public void setPointRepere(String pointRepere) {
        this.pointRepere = pointRepere;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getSurfaceApproximative() {
        return surfaceApproximative;
    }

    public void setSurfaceApproximative(double surfaceApproximative) {
        this.surfaceApproximative = surfaceApproximative;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getElementsVisibles() {
        return elementsVisibles;
    }

    public void setElementsVisibles(String elementsVisibles) {
        this.elementsVisibles = elementsVisibles;
    }

    public String getDegats() {
        return degats;
    }

    public void setDegats(String degats) {
        this.degats = degats;
    }

    public int getIntervention() {
        return intervention;
    }

    public void setIntervention(int intervention) {
        this.intervention = intervention;
    }

    public int getRenfort() {
        return renfort;
    }

    public void setRenfort(int renfort) {
        this.renfort = renfort;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getIdIntervention() {
        return idIntervention;
    }

    public void setIdIntervention(int idIntervention) {
        this.idIntervention = idIntervention;
    }

    public int getIdUserApp() {
        return idUserApp;
    }

    public void setIdUserApp(int idUserApp) {
        this.idUserApp = idUserApp;
    }
}
