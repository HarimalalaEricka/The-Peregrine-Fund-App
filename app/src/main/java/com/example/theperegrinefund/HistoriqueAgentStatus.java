package com.example.theperegrinefund;

import java.sql.Date;

public class HistoriqueAgentStatus {
    private int idHistorique;
    private Date date_changement;
    private StatusAgent status;
    private Patrouilleur agent;
    public HistoriqueAgentStatus( int id, Date changement, StatusAgent status, Patrouilleur agent)
    {
        this.idHistorique = id;
        this.date_changement = changement;
        this.status = status;
        this.agent = agent;
    }
    public int getIdHistorique()
    {
        return this.idHistorique;
    }
    public Date getDateChangement()
    {
        return this.date_changement;
    }
    public void setDateChangement( Date date)
    {
        this.date_changement = date;
    }
    public StatusAgent getStatus()
    {
        return this.status;
    }
    public void setStatus( StatusAgent status)
    {
        this.status = status;
    }
    public Patrouilleur getAgent()
    {
        return this.agent;
    }
    public void setAgent( Patrouilleur agent)
    {
        this.agent = agent;
    }
}
