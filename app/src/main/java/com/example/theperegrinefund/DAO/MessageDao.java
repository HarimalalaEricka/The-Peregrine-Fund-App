package com.example.theperegrinefund.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.theperegrinefund.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MessageDao {
    private MyDatabaseHelper dbHelper;

    public MessageDao(Context context) {
        dbHelper = new MyDatabaseHelper(context);
    }

    // INSERT
    public long insertMessage(String dateCommencement, String dateSignalement,
                              String pointRepere, String description, double surfaceApproximative,
                              String direction, String elementsVisibles, String degats,
                              String contenuCode) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MyDatabaseHelper.COLUMN_DATE_COMMENCEMENT, dateCommencement);
        values.put(MyDatabaseHelper.COLUMN_DATE_SIGNAL, dateSignalement);
        values.put(MyDatabaseHelper.COLUMN_POINT_REPERE, pointRepere);
        values.put(MyDatabaseHelper.COLUMN_DESCRIPTION, description);
        values.put(MyDatabaseHelper.COLUMN_SURFACE, surfaceApproximative);
        values.put(MyDatabaseHelper.COLUMN_DIRECTION, direction);
        values.put(MyDatabaseHelper.COLUMN_ELEMENTS_VISIBLES, elementsVisibles);
        values.put(MyDatabaseHelper.COLUMN_DEGATS, degats);
        values.put(MyDatabaseHelper.COLUMN_CONTENU_CODE, contenuCode);

        long id = db.insert(MyDatabaseHelper.TABLE_MESSAGE, null, values);
        db.close();
        return id;
    }

    // SELECT - Retourner toutes les descriptions de messages
    public List<String> getAllMessages() {
        List<String> messages = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                MyDatabaseHelper.TABLE_MESSAGE,
                null, null, null, null, null, null
        );

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_MESSAGE_ID));
            String desc = cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_DESCRIPTION));
            String direction = cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_DIRECTION));
            String contenu = cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_CONTENU_CODE));

            messages.add(id + " | " + desc + " | " + direction + " | " + contenu);
        }

        cursor.close();
        db.close();
        return messages;
    }
}
