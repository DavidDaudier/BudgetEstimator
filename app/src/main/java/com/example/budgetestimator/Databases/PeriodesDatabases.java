package com.example.budgetestimator.Databases;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.budgetestimator.Modeles.Mois;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class PeriodesDatabases {

    Context context = null;
    private SQLiteDatabase db;

    public PeriodesDatabases(Context context) {
        db = new BudgetDatabases(context).getWritableDatabase();
        this.context = context;
    }

    public boolean ajouterMois(Mois mois) {
        return ajouterMois(mois.getNumero(), mois.getAnnee());
    }

    public boolean ajouterMois(int mois, int annee) {
        ContentValues cv = new ContentValues();
        cv.put(BudgetDatabases.COLUMN_MOIS_ECOULES_MOIS, mois + "/" + annee);
        long resultatRequete = db.insert(BudgetDatabases.TABLE_MOIS_ECOULES, null, cv);
        if (resultatRequete == -1)
            return false;
        return true;
    }

    public boolean insertionLorsDuPremierLancement() {

        GregorianCalendar calendar = new GregorianCalendar();

        //Janvier = 0 dans la classe GregorianCalandar d'ou l'incrementaion de 1
        int moisCourant = calendar.get(GregorianCalendar.MONTH) + 1;
        int anneeCourant = calendar.get(GregorianCalendar.YEAR);

        // AjouterMois(moisCourant, anneeCourant)
        for (int j = 2000; j < anneeCourant; j++)
            for (int i = 1; i <= 12; i++)
                ajouterMois(i, j);
        for (int i = 1; i <= moisCourant; i++) {
            ajouterMois(i, anneeCourant);
        }

        return true;
    }

    // Methode Afficher mois
    public ArrayList<Mois> liste() {
        ArrayList<Mois> liste = new ArrayList<>();
        String requeteMois = "SELECT " + BudgetDatabases.COLUMN_MOIS_ECOULES_ID + "," + BudgetDatabases.COLUMN_MOIS_ECOULES_MOIS +
                " FROM " + BudgetDatabases.TABLE_MOIS_ECOULES;

        Cursor cursor = db.rawQuery(requeteMois, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // int m = cursor.getInt(cursor.getColumnIndex(SqlHelper.COLUMN_MOIS_ECOULES_ID));
            @SuppressLint("Range") String mm_aaaa = cursor.getString(cursor.getColumnIndex(BudgetDatabases.COLUMN_MOIS_ECOULES_MOIS));
            liste.add(new Mois(mm_aaaa));
            cursor.moveToNext();
        }
        cursor.close();
        return liste;
    }

    public void reInit() {
        clear();
        insertionLorsDuPremierLancement();
    }

    private void clear() {
        db.delete(BudgetDatabases.TABLE_MOIS_ECOULES, null, null);
    }

}
