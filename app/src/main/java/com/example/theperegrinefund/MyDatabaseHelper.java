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
    public static final String COLUMN_DATE_COMMENCEMENT = "dateCommencement";
    public static final String COLUMN_DATE_SIGNAL = "dateSignalement";
    public static final String COLUMN_CONTENU_CODE = "contenuCode";
    public static final String COLUMN_POINT_REPERE = "pointRepere";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_SURFACE = "surfaceApproximative";
    public static final String COLUMN_DIRECTION = "direction";
    public static final String COLUMN_ELEMENTS_VISIBLES = "elementsVisibles";
    public static final String COLUMN_DEGATS = "degats";
    public static final String COLUMN_RENFORT = "renfort";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_INTERVENTION_FK = "idIntervention";
    public static final String COLUMN_USER_FK = "idUserApp";

    private static final String CREATE_TABLE_MESSAGE =
            "CREATE TABLE " + TABLE_MESSAGE + " (" +
                    COLUMN_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DATE_COMMENCEMENT + " TEXT NOT NULL, " +
                    COLUMN_DATE_SIGNAL + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_CONTENU_CODE + " TEXT, " +
                    COLUMN_POINT_REPERE + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_SURFACE + " REAL, " +
                    COLUMN_DIRECTION + " TEXT NOT NULL, " +
                    COLUMN_ELEMENTS_VISIBLES + " TEXT, " +
                    COLUMN_DEGATS + " TEXT, " +
                    COLUMN_RENFORT + " INTEGER, " +   // 0 = false, 1 = true
                    COLUMN_LONGITUDE + " REAL, " +
                    COLUMN_LATITUDE + " REAL, " +
                    COLUMN_INTERVENTION_FK + " INTEGER NOT NULL, " +
                    COLUMN_USER_FK + " INTEGER NOT NULL, " +
                    "FOREIGN KEY(" + COLUMN_USER_FK + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "));";

    // ---------- TABLE INTERVENTION ----------
    public static final String TABLE_INTERVENTION = "intervention";
    public static final String COLUMN_INTERVENTION_ID = "idIntervention";
    public static final String COLUMN_INTERVENTION_NAME = "intervention";

    private static final String CREATE_TABLE_INTERVENTION =
            "CREATE TABLE " + TABLE_INTERVENTION + " (" +
                    COLUMN_INTERVENTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_INTERVENTION_NAME + " TEXT NOT NULL);";

    // ---------- TABLE STATUS MESSAGE ----------
    public static final String TABLE_STATUS = "status_message";
    public static final String COLUMN_STATUS_ID = "idStatusMessage";
    public static final String COLUMN_STATUS_NAME = "status";

    private static final String CREATE_TABLE_STATUS =
            "CREATE TABLE " + TABLE_STATUS + " (" +
                    COLUMN_STATUS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_STATUS_NAME + " TEXT NOT NULL UNIQUE);";

    // ---------- TABLE HISTORIQUE MESSAGE STATUS ----------
    public static final String TABLE_HISTORIQUE = "historique_message_status";
    public static final String COLUMN_HISTORIQUE_ID = "idHistorique";
    public static final String COLUMN_HISTORIQUE_DATE = "dateChangement";
    public static final String COLUMN_HISTORIQUE_STATUS_FK = "idStatusMessage";
    public static final String COLUMN_HISTORIQUE_MESSAGE_FK = "idMessage";

    private static final String CREATE_TABLE_HISTORIQUE =
            "CREATE TABLE " + TABLE_HISTORIQUE + " (" +
                    COLUMN_HISTORIQUE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_HISTORIQUE_DATE + " TEXT, " +
                    COLUMN_HISTORIQUE_STATUS_FK + " INTEGER NOT NULL, " +
                    COLUMN_HISTORIQUE_MESSAGE_FK + " INTEGER NOT NULL, " +
                    "FOREIGN KEY(" + COLUMN_HISTORIQUE_STATUS_FK + ") REFERENCES " + TABLE_STATUS + "(" + COLUMN_STATUS_ID + "), " +
                    "FOREIGN KEY(" + COLUMN_HISTORIQUE_MESSAGE_FK + ") REFERENCES " + TABLE_MESSAGE + "(" + COLUMN_MESSAGE_ID + "));";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_INTERVENTION);
        db.execSQL(CREATE_TABLE_MESSAGE);
        db.execSQL(CREATE_TABLE_STATUS);
        db.execSQL(CREATE_TABLE_HISTORIQUE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORIQUE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATUS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERVENTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
}
