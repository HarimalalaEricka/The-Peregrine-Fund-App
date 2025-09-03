package com.example.theperegrinefund.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.theperegrinefund.StatusMessage;
import com.example.theperegrinefund.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class StatusMessageDao {
    private MyDatabaseHelper dbHelper;

    public StatusMessageDao(Context context) {
        dbHelper = new MyDatabaseHelper(context);
    }

    public long insertStatus(StatusMessage status) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", status.getStatus());
        long id = db.insert("status_message", null, values);
        db.close();
        return id;
    }

    public List<StatusMessage> getAllStatus() {
        List<StatusMessage> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("status_message", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                StatusMessage status = new StatusMessage();
                status.setIdStatusMessage(cursor.getInt(cursor.getColumnIndexOrThrow("Id_status_message")));
                status.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
                list.add(status);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
}