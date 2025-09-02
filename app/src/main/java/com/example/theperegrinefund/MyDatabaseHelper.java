package com.example.theperegrinefund;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MaBase.db";
    private static final int DATABASE_VERSION = 1;

    // ---------- TABLE USERS ----------
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "idUserApp";
    public static final String COLUMN_USER_NAME = "name";

    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_NAME + " TEXT NOT NULL);";

    // ---------- TABLE MESSAGE ----------
    public static final String TABLE_MESSAGE = "message";
    public static final String COLUMN_MESSAGE_ID = "idMessage";
    public static final String COLUMN_UUID = "uuidMessage"; // identifiant unique pour la synchro
    public static final String COLUMN_DATE_COMMENCEMENT = "dateCommencement";
    public static final String COLUMN_DATE_SIGNAL = "dateSignalement";
    public static final String COLUMN_CONTENU_CODE = "contenuCode";
    public static final String COLUMN_POINT_REPERE = "pointRepere";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_SURFACE = "surfaceApproximative";
    public static final String COLUMN_DIRECTION = "direction";
    public static final String COLUMN_ELEMENTS_VISIBLES = "elementsVisibles";
    public static final String COLUMN_DEGATS = "degats";
    public static final String COLUMN_INTERVENTION = "intervention";
    public static final String COLUMN_RENFORT = "renfort";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_INTERVENTION_FK = "idIntervention";
    public static final String COLUMN_USER_FK = "idUserApp";

    private static final String CREATE_TABLE_MESSAGE =
            "CREATE TABLE " + TABLE_MESSAGE + " (" +
                    COLUMN_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_UUID + " TEXT UNIQUE, " +
                    COLUMN_DATE_COMMENCEMENT + " TEXT NOT NULL, " +
                    COLUMN_DATE_SIGNAL + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_CONTENU_CODE + " TEXT, " +
                    COLUMN_POINT_REPERE + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_SURFACE + " REAL, " +
                    COLUMN_DIRECTION + " TEXT NOT NULL, " +
                    COLUMN_ELEMENTS_VISIBLES + " TEXT, " +
                    COLUMN_DEGATS + " TEXT, " +
                    COLUMN_INTERVENTION + " INTEGER, " +
                    COLUMN_RENFORT + " INTEGER, " +   // 0 = false, 1 = true
                    COLUMN_LONGITUDE + " REAL, " +
                    COLUMN_LATITUDE + " REAL, " +
                    COLUMN_INTERVENTION_FK + " INTEGER NOT NULL, " +
                    COLUMN_USER_FK + " INTEGER NOT NULL, " +
                    "FOREIGN KEY(" + COLUMN_USER_FK + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "));";

    // ---------- TABLE ALERTE ----------
    public static final String TABLE_ALERTE = "alerte";
    public static final String COLUMN_ALERTE_ID = "idAlerte";
    public static final String COLUMN_MESSAGE_FK = "idMessage";
    public static final String COLUMN_TYPE = "type";

    private static final String CREATE_TABLE_ALERTE =
            "CREATE TABLE " + TABLE_ALERTE + " (" +
                    COLUMN_ALERTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_MESSAGE_FK + " INTEGER, " +
                    COLUMN_TYPE + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_MESSAGE_FK + ") REFERENCES " + TABLE_MESSAGE + "(" + COLUMN_MESSAGE_ID + "));";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Activer la gestion des clés étrangères
        db.execSQL("PRAGMA foreign_keys=ON;");
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_MESSAGE);
        db.execSQL(CREATE_TABLE_ALERTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALERTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
}
