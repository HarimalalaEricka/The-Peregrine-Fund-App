package com.example.theperegrinefund;

import java.sql.Date;


public class Agent {
    private int idAgent;
    private String nom;
    private String telephone;
    private Date date_recrutement;
    private Site site;
    public Agent( String nom, String telephone, Date recrut, Site site)
    {
        this.nom = nom;
        this.telephone = telephone;
        this.date_recrutement = recrut;
        this.site = site;
    }
    public int getIdAgent()
    {
        return this.idAgent;
    }
    public String getNom()
    {
        return this.nom;
    }
    public String getTelephone()
    {
        return this.telephone;
    }
    public Date getDateRecrutement()
    {
        return this.date_recrutement;
    }
    public Site getSite()
    {
        return this.site;
    }
    public void setIdAgent( int id)
    {
        this.idAgent = id;
    }
    public void setNom( String nom)
    {
        this.nom = nom;
    }
    public void setTelephone( String tel)
    {
        this.telephone = tel;
    }
    public void setDateRecrutement( Date date)
    {
        this.date_recrutement = date;
    }
    public void setSite( Site site)
    {
        this.site = site;
    }
}
