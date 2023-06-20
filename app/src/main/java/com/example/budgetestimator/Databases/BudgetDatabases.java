package com.example.budgetestimator.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BudgetDatabases extends SQLiteOpenHelper {

    public static final String TABLE_TRANSACTION = "Transactions";
    public static final String TABLE_MOIS_ECOULES = "MoisEcoules";
    public static final String TABLE_CALENDRIER = "Calendriers";

    public static final String COLUMN_TRANSACTION_ID = "trans_id";
    public static final String COLUMN_TRANSACTION_TYPE = "types";
    public static final String COLUMN_TRANSACTION_MONTANT = "montants";
    public static final String COLUMN_TRANSACTION_CATEGORIE = "categories";
    public static final String COLUMN_TRANSACTION_NOTE = "notes";
    public static final String COLUMN_TRANSACTION_DATE = "dates";
    public static final String COLUMN_TRANSACTION_PERIODE = "periodes";
    public static final String COLUMN_MOIS_ECOULES_ID = "mois_id";
    public static final String COLUMN_MOIS_ECOULES_MOIS = "mois"; //format MM/YYYY
    public static final String COLUMN_CALENDRIER_ID = "cal_id";
    public static final String COLUMN_CALENDRIER_VALUE = "valeurs";

    public static final String NB_LANCEMENT_APP = "NB_LANCEMENT_APP";

    private final static String DATABASE_NAME = "budgetmanager.db";
    private final static int DATABASE_VERSION = 1;

    public BudgetDatabases(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Requete de creation de la table Transaction
        String requeteCreationTableTransaction = "CREATE TABLE " + TABLE_TRANSACTION + " (" +
                COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TRANSACTION_TYPE + " INTEGER, " +
                COLUMN_TRANSACTION_MONTANT + " REAL, " +
                COLUMN_TRANSACTION_CATEGORIE + " TEXT, " +
                COLUMN_TRANSACTION_NOTE + " TEXT," +
                COLUMN_TRANSACTION_DATE + " TEXT, " +
                COLUMN_TRANSACTION_PERIODE + " INTEGER" +
                ")";

        // Requete de creation de la table des mois qui se sont écoulés
        String requeteCreationTableMoisEcoules = "CREATE TABLE " + TABLE_MOIS_ECOULES + " (" +
                COLUMN_MOIS_ECOULES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MOIS_ECOULES_MOIS + " TEXT)";

        // requete de creation de la table Calendrier contenant quelques données utiles
        String requeteCreationTableCalendrier = "CREATE TABLE " + TABLE_CALENDRIER + " (" + COLUMN_CALENDRIER_ID + " Text PRIMARY KEY, " + COLUMN_CALENDRIER_VALUE + " Text)";

        db.execSQL(requeteCreationTableTransaction);
        db.execSQL(requeteCreationTableMoisEcoules);
        db.execSQL(requeteCreationTableCalendrier);

        // Ajout de la ligne qui contient le nombre de fois que l'application a été lancé pour refresh l'application
        String requeteAjoutLigne_nb_lancement_app = "INSERT INTO " + TABLE_CALENDRIER + " (" + COLUMN_CALENDRIER_ID + ", " + COLUMN_CALENDRIER_VALUE + ") values ('" + NB_LANCEMENT_APP + "', '0')";
        db.execSQL(requeteAjoutLigne_nb_lancement_app);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String requeteDeMisAjourTableTransaction = "DROP TABLE IF EXISTS " + TABLE_TRANSACTION;
        String requeteDeMisAjourTableMoisEcoules = "DROP TABLE IF EXISTS " + TABLE_MOIS_ECOULES;
        String requeteDeMisAjourTableCalendrier = "DROP TABLE IF EXISTS " + TABLE_CALENDRIER;

        db.execSQL(requeteDeMisAjourTableTransaction);
        db.execSQL(requeteDeMisAjourTableMoisEcoules);
        db.execSQL(requeteDeMisAjourTableCalendrier);
        onCreate(db);
    }
}
