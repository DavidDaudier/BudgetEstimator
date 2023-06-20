package com.example.budgetestimator.Databases;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public final class CalendriersDatabases {

    private SQLiteDatabase db;

    public CalendriersDatabases(Context context) {
        db = new BudgetDatabases(context).getWritableDatabase();
    }

    public boolean mettreAjourNbLancementApp() {
        int newValue = nombreLancementApp() + 1;
        return mettreAjourUneColone(BudgetDatabases.NB_LANCEMENT_APP, String.valueOf(newValue));
    }

    @SuppressLint("Range")
    public int nombreLancementApp() {
        String requete = "SELECT " + BudgetDatabases.COLUMN_CALENDRIER_ID + ", " + BudgetDatabases.COLUMN_CALENDRIER_VALUE +
                " FROM " + BudgetDatabases.TABLE_CALENDRIER +
                " WHERE " + BudgetDatabases.COLUMN_CALENDRIER_ID + "='" + BudgetDatabases.NB_LANCEMENT_APP + "'";

        Cursor cursor = db.rawQuery(requete, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            return cursor.getInt(cursor.getColumnIndex(BudgetDatabases.COLUMN_CALENDRIER_VALUE));
        }
        cursor.close();
        return -1;
    }

    public boolean mettreAjourUneColone(String key, String newValue) {
        ContentValues cv = new ContentValues();
        cv.put(BudgetDatabases.COLUMN_CALENDRIER_VALUE, newValue);
        int resultatDeLaRequete = db.update(BudgetDatabases.TABLE_CALENDRIER, cv, BudgetDatabases.COLUMN_CALENDRIER_ID + "='" + key + "'", null);
        if (resultatDeLaRequete > 0)
            return true;
        return false;
    }

}
