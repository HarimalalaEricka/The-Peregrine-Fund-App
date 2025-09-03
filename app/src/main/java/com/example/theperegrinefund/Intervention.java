package com.example.app.models;

public class Intervention {
    private int idIntervention;
    private String intervention;

    public Intervention() {
    }

    public Intervention(int idIntervention, String intervention) {
        this.idIntervention = idIntervention;
        this.intervention = intervention;
    }

    public int getIdIntervention() {
        return idIntervention;
    }

    public void setIdIntervention(int idIntervention) {
        this.idIntervention = idIntervention;
    }

    public String getIntervention() {
        return intervention;
    }

    public void setIntervention(String intervention) {
        this.intervention = intervention;
    }
}
