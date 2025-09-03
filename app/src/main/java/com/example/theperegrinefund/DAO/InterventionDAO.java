package com.example.theperegrinefund.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.theperegrinefund.Intervention;
import com.example.theperegrinefund.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class InterventionDao {
    private MyDatabaseHelper dbHelper;

    public InterventionDao(Context context) {
        dbHelper = new MyDatabaseHelper(context);
    }

    public long insertIntervention(Intervention intervention) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("intervention", intervention.getIntervention());
        long id = db.insert("Intervention", null, values);
        db.close();
        return id;
    }

    public List<Intervention> getAllInterventions() {
        List<Intervention> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Intervention", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Intervention i = new Intervention();
                i.setIdIntervention(cursor.getInt(cursor.getColumnIndexOrThrow("Id_Intervention")));
                i.setIntervention(cursor.getString(cursor.getColumnIndexOrThrow("intervention")));
                list.add(i);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
}