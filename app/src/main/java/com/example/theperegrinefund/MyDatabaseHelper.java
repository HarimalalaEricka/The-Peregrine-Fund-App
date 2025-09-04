package com.example.theperegrinefund;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import java.util.Date;
import java.util.ArrayList;


public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "peregrine.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // --- Création des tables ---
        db.execSQL("CREATE TABLE status_message(" +
                "Id_status_message INTEGER PRIMARY KEY AUTOINCREMENT," +
                "status TEXT NOT NULL UNIQUE)");

        db.execSQL("CREATE TABLE Intervention(" +
                "Id_Intervention INTEGER PRIMARY KEY AUTOINCREMENT," +
                "intervention TEXT NOT NULL UNIQUE)");

        db.execSQL("CREATE TABLE Message(" +
                "Id_Message INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Date_Commencement TEXT NOT NULL," +
                "Date_signalement TEXT NOT NULL UNIQUE," +
                "PointRepere TEXT," +
                "Surface_Approximative REAL," +
                "Description TEXT," +
                "Direction TEXT NOT NULL," +
                "renfort INTEGER," + // 0 ou 1
                "longitude REAL," +
                "latitude REAL," +
                "Id_Intervention INTEGER NOT NULL," +
                "IdUserApp INTEGER" +
                ")");

        db.execSQL("CREATE TABLE historique_message_status(" +
                "Id_historique INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date_changement TEXT," +
                "Id_status_message INTEGER NOT NULL," +
                "Id_Message INTEGER NOT NULL," +
                "FOREIGN KEY(Id_status_message) REFERENCES status_message(Id_status_message)," +
                "FOREIGN KEY(Id_Message) REFERENCES Message(Id_Message)" +
                ")");

        // --- Insertion de données initiales pour status_message ---
        db.execSQL("INSERT INTO status_message(status) VALUES('Debut de feu')");
        db.execSQL("INSERT INTO status_message(status) VALUES('En cours')");
        db.execSQL("INSERT INTO status_message(status) VALUES('Maitrise')");

        db.execSQL("INSERT INTO Intervention(intervention) VALUES('Possible')");
        db.execSQL("INSERT INTO Intervention(intervention) VALUES('Partielle')");
        db.execSQL("INSERT INTO Intervention(intervention) VALUES('Impossible')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS historique_message_status");
        db.execSQL("DROP TABLE IF EXISTS Message");
        db.execSQL("DROP TABLE IF EXISTS status_message");
        onCreate(db);
    }

    public int getIdIntervention(String interventionName) {
        int id = -1; // valeur par défaut si non trouvé
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                "SELECT Id_Intervention FROM Intervention WHERE intervention = ?",
                new String[]{interventionName}
            );
            if (cursor.moveToFirst()) {
                id = cursor.getInt(cursor.getColumnIndexOrThrow("Id_Intervention"));
            }
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return id;
    }
    public int getIdStatusMessage(String status) {
        int id = -1; // valeur par défaut si non trouvé
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
            "SELECT Id_status_message FROM status_message WHERE status = ?",
            new String[]{status}
        );

        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndexOrThrow("Id_status_message"));
        }
        cursor.close();
        return id;
        }

public ArrayList<Message> getAllMessages() {
    ArrayList<Message> messages = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();

    // Sélectionner les colonnes nécessaires
    String query = "SELECT Id_Message, Date_Commencement, Date_signalement, Id_Intervention, " +
                   "renfort, Direction, Surface_Approximative, PointRepere, Description, " +
                   "IdUserApp, longitude, latitude " +
                   "FROM Message";

    Cursor cursor = db.rawQuery(query, null);

    if (cursor.moveToFirst()) {
        do {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("Id_Message"));

            // Dates
            long dateCommMillis = cursor.getLong(cursor.getColumnIndexOrThrow("Date_Commencement"));
            long dateSignalMillis = cursor.getLong(cursor.getColumnIndexOrThrow("Date_signalement"));
            Date dateComm = new Date(dateCommMillis);
            Date dateSignal = new Date(dateSignalMillis);

            // Intervention
            int idInterv = cursor.getInt(cursor.getColumnIndexOrThrow("Id_Intervention"));
            Intervention intervention = new Intervention(idInterv, ""); // nom vide pour l'instant

            // Autres champs
            boolean renfort = cursor.getInt(cursor.getColumnIndexOrThrow("renfort")) != 0;
            String direction = cursor.getString(cursor.getColumnIndexOrThrow("Direction"));
            double surface = cursor.getDouble(cursor.getColumnIndexOrThrow("Surface_Approximative"));
            String pointRepere = cursor.getString(cursor.getColumnIndexOrThrow("PointRepere"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("Description"));
            double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));
            double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
            int user = cursor.getInt(cursor.getColumnIndexOrThrow("IdUserApp"));

            // Créer l'objet Message
            Message msg = new Message(dateComm, dateSignal, intervention, renfort, direction,
                                      surface, pointRepere, description, "0349322431", longitude, latitude, user);
            msg.setIdMessage(id);

            messages.add(msg);

        } while (cursor.moveToNext());
    }
    cursor.close();
    db.close();

    return messages;
}


}
