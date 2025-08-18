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
    public long insertMessage(String dateCommencement, String dateSignalement, String contenuCode,
                              String pointRepere, String description, double surface,
                              String direction, String elementsVisibles, String degats, long userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MyDatabaseHelper.COLUMN_DATE_COMMENCEMENT, dateCommencement);
        values.put(MyDatabaseHelper.COLUMN_DATE_SIGNAL, dateSignalement);
        values.put(MyDatabaseHelper.COLUMN_CONTENU_CODE, contenuCode);
        values.put(MyDatabaseHelper.COLUMN_POINT_REPERE, pointRepere);
        values.put(MyDatabaseHelper.COLUMN_DESCRIPTION, description);
        values.put(MyDatabaseHelper.COLUMN_SURFACE, surface);
        values.put(MyDatabaseHelper.COLUMN_DIRECTION, direction);
        values.put(MyDatabaseHelper.COLUMN_ELEMENTS_VISIBLES, elementsVisibles);
        values.put(MyDatabaseHelper.COLUMN_DEGATS, degats);
        values.put(MyDatabaseHelper.COLUMN_USER_FK, userId);

        long id = db.insert(MyDatabaseHelper.TABLE_MESSAGE, null, values);
        db.close();
        return id;
    }

    // SELECT
    public List<String> getAllMessages() {
        List<String> messages = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                MyDatabaseHelper.TABLE_MESSAGE,
                null, null, null, null, null, null
        );

        while (cursor.moveToNext()) {
            String desc = cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_DESCRIPTION));
            messages.add(desc);
        }

        cursor.close();
        db.close();
        return messages;
    }
}
