package com.example.theperegrinefund.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.app.models.HistoriqueMessageStatus;

import java.util.ArrayList;
import java.util.List;

public class HistoriqueMessageStatusDao {
    private MyDatabaseHelper dbHelper;

    public HistoriqueMessageStatusDao(Context context) {
        dbHelper = new MyDatabaseHelper(context);
    }

    public long insertHistorique(HistoriqueMessageStatus h) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date_changement", h.getDateChangement().toString());
        values.put("Id_status_message", h.getIdStatusMessage());
        values.put("Id_Message", h.getIdMessage());
        return db.insert("historique_message_status", null, values);
    }

    public List<HistoriqueMessageStatus> getAllHistorique() {
        List<HistoriqueMessageStatus> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("historique_message_status", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                HistoriqueMessageStatus h = new HistoriqueMessageStatus();
                h.setIdHistorique(cursor.getInt(cursor.getColumnIndexOrThrow("Id_historique")));
                h.setDateChangement(java.time.LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow("date_changement"))));
                h.setIdStatusMessage(cursor.getInt(cursor.getColumnIndexOrThrow("Id_status_message")));
                h.setIdMessage(cursor.getInt(cursor.getColumnIndexOrThrow("Id_Message")));
                list.add(h);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
