package com.example.theperegrinefund.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.theperegrinefund.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class AlerteDao {
    private MyDatabaseHelper dbHelper;

    public AlerteDao(Context context) {
        dbHelper = new MyDatabaseHelper(context);
    }

    // INSERT
    public long insertAlerte(long messageId, String type) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MyDatabaseHelper.COLUMN_MESSAGE_FK, messageId);
        values.put(MyDatabaseHelper.COLUMN_TYPE, type);

        long id = db.insert(MyDatabaseHelper.TABLE_ALERTE, null, values);
        db.close();
        return id;
    }

    // SELECT
    public List<String> getAllAlertes() {
        List<String> alertes = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                MyDatabaseHelper.TABLE_ALERTE,
                null, null, null, null, null, null
        );

        while (cursor.moveToNext()) {
            String type = cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_TYPE));
            alertes.add(type);
        }

        cursor.close();
        db.close();
        return alertes;
    }
}
