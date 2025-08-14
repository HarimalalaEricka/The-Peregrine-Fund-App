package com.example.theperegrinefund;

import java.sql.Date;

public class HistoriqueAgentStatus {
    private int idHistorique;
    private Date date_changement;
    private StatusAgent status;
    private Agent agent;
    public HistoriqueAgentStatus( int id, Date changement, StatusAgent status, Agent agent)
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
    public Agent getAgent()
    {
        return this.agent;
    }
    public void setAgent( Agent agent)
    {
        this.agent = agent;
    }
}
