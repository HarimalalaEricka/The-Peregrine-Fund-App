package com.example.theperegrinefund;

import java.sql.Date;

public class HistoriqueMessageStatus {
    private int idHistorique;
    private Date date_changement;
    private StatusMessage status;
    private Message message;
    public HistoriqueMessageStatus( int id, Date changement, StatusMessage status, Message message)
    {
        this.idHistorique = id;
        this.date_changement = changement;
        this.status = status;
        this.message = message;
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
    public StatusMessage getStatus()
    {
        return this.status;
    }
    public void setStatus( StatusMessage status)
    {
        this.status = status;
    }
    public Message getMessage()
    {
        return this.message;
    }
    public void setMessage( Message mess)
    {
        this.message = message;
    }
}
