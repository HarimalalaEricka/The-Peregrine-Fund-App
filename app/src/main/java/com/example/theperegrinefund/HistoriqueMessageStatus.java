package com.example.theperegrinefund;

import java.util.Date;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class HistoriqueMessageStatus {
    private int idHistorique;
    private Date date_changement;
    private StatusMessage status;
    private Message message;
    public HistoriqueMessageStatus(Date changement, StatusMessage status, Message message)
    {
        this.date_changement = changement;
        this.status = status;
        this.message = message;
    }
    public long save(Context context) {
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("date_changement", date_changement.getTime()); // stocké en timestamp
        values.put("Id_status_message", status.getIdStatusMessage());
        values.put("Id_Message", message.getIdMessage());

        long newId = db.insert("historique_message_status", null, values);
        db.close();
        return newId; // retourne l’ID inséré ou -1 en cas d’erreur
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
