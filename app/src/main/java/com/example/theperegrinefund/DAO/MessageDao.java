package com.example.theperegrinefund.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.theperegrinefund.MyDatabaseHelper;
import com.example.theperegrinefund.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageDao {
    private final MyDatabaseHelper dbHelper;

    public MessageDao(Context context) {
        dbHelper = new MyDatabaseHelper(context);
    }

    // INSERT
    public long insertMessage(Message message) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MyDatabaseHelper.COLUMN_UUID, message.getUuidMessage());
        values.put(MyDatabaseHelper.COLUMN_DATE_COMMENCEMENT, message.getDateCommencement());
        values.put(MyDatabaseHelper.COLUMN_DATE_SIGNAL, message.getDateSignalement());
        values.put(MyDatabaseHelper.COLUMN_POINT_REPERE, message.getPointRepere());
        values.put(MyDatabaseHelper.COLUMN_DESCRIPTION, message.getDescription());
        values.put(MyDatabaseHelper.COLUMN_SURFACE, message.getSurfaceApproximative());
        values.put(MyDatabaseHelper.COLUMN_DIRECTION, message.getDirection());
        values.put(MyDatabaseHelper.COLUMN_ELEMENTS_VISIBLES, message.getElementsVisibles());
        values.put(MyDatabaseHelper.COLUMN_DEGATS, message.getDegats());
        values.put(MyDatabaseHelper.COLUMN_CONTENU_CODE, message.getContenuCode());
        values.put(MyDatabaseHelper.COLUMN_INTERVENTION, message.getIntervention());
        values.put(MyDatabaseHelper.COLUMN_RENFORT, message.getRenfort());
        values.put(MyDatabaseHelper.COLUMN_LONGITUDE, message.getLongitude());
        values.put(MyDatabaseHelper.COLUMN_LATITUDE, message.getLatitude());
        values.put(MyDatabaseHelper.COLUMN_INTERVENTION_FK, message.getIdIntervention());
        values.put(MyDatabaseHelper.COLUMN_USER_FK, message.getIdUserApp());

        long id = db.insert(MyDatabaseHelper.TABLE_MESSAGE, null, values);
        db.close();
        return id;
    }

    // SELECT - Récupérer tous les messages (objets complets)
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                MyDatabaseHelper.TABLE_MESSAGE,
                null, null, null, null, null, null
        );

        while (cursor.moveToNext()) {
            Message msg = new Message();
            msg.setIdMessage(cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_MESSAGE_ID)));
            msg.setUuidMessage(cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_UUID)));
            msg.setDateCommencement(cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_DATE_COMMENCEMENT)));
            msg.setDateSignalement(cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_DATE_SIGNAL)));
            msg.setPointRepere(cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_POINT_REPERE)));
            msg.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_DESCRIPTION)));
            msg.setSurfaceApproximative(cursor.getDouble(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_SURFACE)));
            msg.setDirection(cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_DIRECTION)));
            msg.setElementsVisibles(cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_ELEMENTS_VISIBLES)));
            msg.setDegats(cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_DEGATS)));
            msg.setContenuCode(cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_CONTENU_CODE)));
            msg.setIntervention(cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_INTERVENTION)));
            msg.setRenfort(cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_RENFORT)));
            msg.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_LONGITUDE)));
            msg.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_LATITUDE)));
            msg.setIdIntervention(cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_INTERVENTION_FK)));
            msg.setIdUserApp(cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_USER_FK)));

            messages.add(msg);
        }

        cursor.close();
        db.close();
        return messages;
    }
}
