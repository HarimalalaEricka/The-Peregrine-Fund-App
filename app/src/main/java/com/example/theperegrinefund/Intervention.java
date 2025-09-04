package com.example.theperegrinefund;

public class Intervention {
    private int id_Intervention;
    private String intervention;

    public Intervention(int id_Intervention, String intervention) {
        this.id_Intervention = id_Intervention ;
        this.intervention = intervention;
    }

    public int getIdIntervention() {
        return id_Intervention;
    }

    public void setIdIntervention(int id_Intervention) {
        this.id_Intervention = id_Intervention;
    }

    public String getIntervention() {
        return intervention;
    }
}
