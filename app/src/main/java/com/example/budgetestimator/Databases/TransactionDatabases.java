package com.example.budgetestimator.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.budgetestimator.Modeles.Categorie;
import com.example.budgetestimator.Modeles.Mois;
import com.example.budgetestimator.Modeles.Transaction;
import com.example.budgetestimator.Modeles.Calendriers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class TransactionDatabases {

    private SQLiteDatabase db;

    public TransactionDatabases(Context context) {
        db = new BudgetDatabases(context).getWritableDatabase();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Transaction> listeNonTriee() {

        List<Transaction> list = null;

        Cursor result = db.rawQuery("select " +
            BudgetDatabases.COLUMN_TRANSACTION_ID + ", " +
            BudgetDatabases.COLUMN_TRANSACTION_TYPE + ", " +
            BudgetDatabases.COLUMN_TRANSACTION_MONTANT + ", " +
            BudgetDatabases.COLUMN_TRANSACTION_CATEGORIE + ", " +
            BudgetDatabases.COLUMN_TRANSACTION_NOTE + ", " +
            BudgetDatabases.COLUMN_TRANSACTION_DATE + ", " +
            BudgetDatabases.COLUMN_TRANSACTION_PERIODE + " from " +
            BudgetDatabases.TABLE_TRANSACTION + "", null);

        list = commeList(result);
        result.close();
        return list;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<Transaction> commeList(Cursor result) {
        List<Transaction> list = new ArrayList<>();
        if (result == null)
            return list;

        result.moveToFirst();

        while (!result.isAfterLast()) {
            int id = result.getInt(0);
            int types = result.getInt(1);
            double montant = result.getDouble(2);
            Categorie categorie = Categorie.getInstance(result.getInt(3));
            String note = result.getString(4);
            String dateString = result.getString(5);
            int periode = result.getInt(6);

            GregorianCalendar date = null;
            try {
                date = Calendriers.stringToCalandar(dateString);
                Transaction transaction = new Transaction(id, types, montant, categorie, note, date, periode);
                list.add(transaction);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //Utiliser calandar serait mieux dans ce cas ci au lieu de date
            result.moveToNext();
        }
        return list;
    }

    // Methode de recuperation Transaction qui permet de modifier les donnees
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Transaction getTransaction(int id) {
        Cursor result = db.rawQuery("SELECT " +
            BudgetDatabases.COLUMN_TRANSACTION_ID + ", " +
            BudgetDatabases.COLUMN_TRANSACTION_TYPE + ", " +
            BudgetDatabases.COLUMN_TRANSACTION_MONTANT + ", " +
            BudgetDatabases.COLUMN_TRANSACTION_CATEGORIE + ", " +
            BudgetDatabases.COLUMN_TRANSACTION_NOTE + ", " +
            BudgetDatabases.COLUMN_TRANSACTION_DATE + ", " +
            BudgetDatabases.COLUMN_TRANSACTION_PERIODE +
            " FROM " + BudgetDatabases.TABLE_TRANSACTION +
            " WHERE " + BudgetDatabases.COLUMN_TRANSACTION_ID + "=" + id, null);

        List<Transaction> list = commeList(result);
        if (list.size() == 1)
            return list.get(0);
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean ajouterTransaction(Transaction transaction) {

        ContentValues values = getContentValuesWithoutID(transaction);
        if (values == null)
            return false;

        long resultatDeLaRequete = db.insert(BudgetDatabases.TABLE_TRANSACTION, null, values);
        if (resultatDeLaRequete == -1)
            return false;
        return true;
    }

    public boolean supprimerTransaction(int transactionID) {
        int resultatDeLaRequete = db.delete(BudgetDatabases.TABLE_TRANSACTION, BudgetDatabases.COLUMN_TRANSACTION_ID + "=" + transactionID, null);
        if (resultatDeLaRequete == 0)
            return false;
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean modifierTransaction(int transactionID, Transaction transaction) {
        ContentValues contentValues = getContentValuesWithoutID(transaction);
        if (contentValues == null)
            return false;

        int resultDeLaRequete = db.update(BudgetDatabases.TABLE_TRANSACTION, contentValues, BudgetDatabases.COLUMN_TRANSACTION_ID + "=" + transactionID, null);
        if (resultDeLaRequete == -1)
            return false;
        else
            return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Transaction> listDuMois(Mois mois) {
        List<Transaction> list = new ArrayList<Transaction>();
        for (Transaction transac : listeNonTriee()) {
            if (mois.includes(transac.getDate()))
                list.add(transac);
        }
        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Transaction> listApresMois(Mois mois) {
        List<Transaction> list = new ArrayList<Transaction>();
        for (Transaction transac : listeNonTriee()) {
            if (mois.isBefore(transac.getDate()))
                list.add(transac);
        }
        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Transaction> listAvantMois(Mois mois) {
        List<Transaction> list = new ArrayList<Transaction>();
        for (Transaction transac : listeNonTriee()) {
            if (mois.isAfter(transac.getDate()))
                list.add(transac);
        }
        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Transaction dernierTransaction() {
        List<Transaction> list = listeNonTriee();
        int lastIndex = listeNonTriee().size() - 1;
        if (lastIndex >= 0) {
            return list.get(lastIndex);
        }
        return null;
    }

    /*
     * Retourne la liste des transactions effectuées entre un mois A et B (inclus les mois effectués pendant A et B)
     * */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Transaction> listEntre(Mois moisA, Mois moisB) {
        Mois moisInf, moisSup;
        if (moisA.isBefore(moisB)) {
            moisInf = moisA;
            moisSup = moisB;
        } else {
            moisInf = moisB;
            moisSup = moisA;
        }
        List<Transaction> list = new ArrayList<Transaction>();
        for (Transaction transac : listeNonTriee()) {
            Calendar calendar = transac.getDate();
            if ((moisInf.isBefore(calendar) || moisInf.includes(calendar)) && (moisSup.isAfter(calendar) || moisSup.includes(calendar)))
                list.add(transac);
        }
        return list;
    }

    // Un simple meethode pour economiser temps
    @RequiresApi(api = Build.VERSION_CODES.N)
    private ContentValues getContentValuesWithoutID(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        ContentValues values = new ContentValues();
        values.put(BudgetDatabases.COLUMN_TRANSACTION_TYPE, transaction.getTypes());
        values.put(BudgetDatabases.COLUMN_TRANSACTION_MONTANT, transaction.getMontant());
        values.put(BudgetDatabases.COLUMN_TRANSACTION_CATEGORIE, transaction.getCategorie().getId());
        values.put(BudgetDatabases.COLUMN_TRANSACTION_NOTE, transaction.getNote());
        values.put(BudgetDatabases.COLUMN_TRANSACTION_DATE, Calendriers.calandarToString(transaction.getDate()));
        values.put(BudgetDatabases.COLUMN_TRANSACTION_PERIODE, transaction.getPeriodes());
        return values;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Transaction> listAvantpFreqMois(Mois moisDuFragment) {
        List<Transaction> list = new ArrayList<>();
        for (Transaction transaction :this.listAvantMois(moisDuFragment)) {
            if(transaction.getPeriodes() != Transaction.UNE_FOIS)
                list.add(transaction);
        }
        return list;
    }

}
