package com.example.theperegrinefund;

import java.util.Date;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class HistoriqueMessageStatus {
    private int idHistorique;
    private String dateChangement;
    private int idStatusMessage;
    private int idMessage;
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

    public void setIdHistorique(int idHistorique) {
        this.idHistorique = idHistorique;
    }

    public String getDateChangement() {
        return dateChangement;
    }

    public void setDateChangement(String dateChangement) {
        this.dateChangement = dateChangement;
    }

    public int getIdStatusMessage() {
        return idStatusMessage;
    }

    public void setIdStatusMessage(int idStatusMessage) {
        this.idStatusMessage = idStatusMessage;
    }

    public int getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(int idMessage) {
        this.idMessage = idMessage;
    }

    public StatusMessage getStatus() {
        return status;
    }

    public void setStatus(StatusMessage status) {
        this.status = status;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}