package com.example.theperegrinefund.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.theperegrinefund.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private MyDatabaseHelper dbHelper;

    public UserDao(Context context) {
        dbHelper = new MyDatabaseHelper(context);
    }

    // INSERT
    public long insertUser(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MyDatabaseHelper.COLUMN_USER_NAME, name);

        long id = db.insert(MyDatabaseHelper.TABLE_USERS, null, values);
        db.close();
        return id;
    }

    // SELECT
    public List<String> getAllUsers() {
        List<String> users = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                MyDatabaseHelper.TABLE_USERS,
                null, null, null, null, null, null
        );

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_USER_NAME));
            users.add(name);
        }

        cursor.close();
        db.close();
        return users;
    }
}
