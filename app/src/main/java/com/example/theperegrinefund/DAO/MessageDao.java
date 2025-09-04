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

    public long insertMessage(Message message) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(MyDatabaseHelper.COLUMN_MESSAGE_ID, message.getIdMessage());
        values.put(MyDatabaseHelper.COLUMN_DATE_COMMENCEMENT, message.getDateCommencement());
        values.put(MyDatabaseHelper.COLUMN_DATE_SIGNAL, message.getDateSignalement());
        values.put(MyDatabaseHelper.COLUMN_POINT_REPERE, message.getPointRepere());
        values.put(MyDatabaseHelper.COLUMN_SURFACE, message.getSurfaceApproximative());
        values.put(MyDatabaseHelper.COLUMN_DESCRIPTION, message.getDescription());
        values.put(MyDatabaseHelper.COLUMN_DIRECTION, message.getDirection());
        values.put(MyDatabaseHelper.COLUMN_RENFORT, message.isRenfort() ? 1 : 0); // Convertir boolean to int
        values.put(MyDatabaseHelper.COLUMN_LONGITUDE, message.getLongitude());
        values.put(MyDatabaseHelper.COLUMN_LATITUDE, message.getLatitude());
        values.put(MyDatabaseHelper.COLUMN_INTERVENTION_FK, message.getIdIntervention());
        values.put(MyDatabaseHelper.COLUMN_USER_FK, message.getIdUserApp());
       

        return db.insert(MyDatabaseHelper.TABLE_MESSAGE, null, values);
    }

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
            msg.setDateCommencement(cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_DATE_COMMENCEMENT)));
            msg.setDateSignalement(cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_DATE_SIGNAL)));
            msg.setPointRepere(cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_POINT_REPERE)));
            msg.setSurfaceApproximative(cursor.getDouble(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_SURFACE)));
            msg.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_DESCRIPTION)));
            msg.setDirection(cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_DIRECTION)));
            
            // Conversion de int (0/1) vers boolean pour le renfort
            msg.setRenfort(cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_RENFORT)) == 1);
            
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
    public Message getMessageById(int messageId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase(); // Correction ici
        Message message = null;
        
        Cursor cursor = db.query(MyDatabaseHelper.TABLE_MESSAGE, // Correction ici
                null, 
                MyDatabaseHelper.COLUMN_MESSAGE_ID + " = ?", // Correction ici
                new String[]{String.valueOf(messageId)}, 
                null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            message = new Message();
            message.setIdMessage(cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_MESSAGE_ID))); // Correction
            message.setDateCommencement(cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_DATE_COMMENCEMENT))); // Correction
            message.setDateSignalement(cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_DATE_SIGNAL))); // Correction
            message.setPointRepere(cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_POINT_REPERE))); // Correction
            message.setSurfaceApproximative(cursor.getDouble(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_SURFACE))); // Correction
            message.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_DESCRIPTION))); // Correction
            message.setDirection(cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_DIRECTION))); // Correction
            message.setRenfort(cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_RENFORT)) == 1); // Correction
            message.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_LONGITUDE))); // Correction
            message.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_LATITUDE))); // Correction
            message.setIdIntervention(cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_INTERVENTION_FK))); // Correction
            message.setIdUserApp(cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_USER_FK))); // Correction
            
            cursor.close();
        }
        db.close();
        return message;
    }
        
    public boolean exists(int idMessage) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                MyDatabaseHelper.TABLE_MESSAGE,                // nom de la table
                new String[]{MyDatabaseHelper.COLUMN_MESSAGE_ID}, // colonne à vérifier
                MyDatabaseHelper.COLUMN_MESSAGE_ID + " = ?",   // clause WHERE
                new String[]{String.valueOf(idMessage)},       // valeur du paramètre
                null, null, null
        );

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
    
   
}
