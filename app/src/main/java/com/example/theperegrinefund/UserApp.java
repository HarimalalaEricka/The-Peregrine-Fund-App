package com.example.theperegrinefund;

public class UserApp {
    private int idUserApp;
    private String login;
    private String mot_de_passe;
    private Agent agent;

    public UserApp(int idUserApp, String login, String mot_de_passe, Agent agent) {
        this.idUserApp = idUserApp;
        this.login = login;
        this.mot_de_passe = mot_de_passe;
        this.agent = agent;
    }
    public int getIdUserApp() {
        return idUserApp;
    }

    public String getLogin() {
        return login;
    }

    public String getMot_de_passe() {
        return mot_de_passe;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setIdUserApp(int idUserApp) {
        this.idUserApp = idUserApp;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setMot_de_passe(String mot_de_passe) {
        this.mot_de_passe = mot_de_passe;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
}
