package com.example.budgetestimator.Modeles;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.budgetestimator.R;

import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.List;

public class Transaction {

    //  private static int i = 0; //je pense on en aura pas besoin car on recupere dans la base de donnée

    //Definition des constantes
    public static final int BUDGET = 101;
    public static final int DEPENSE = 102;

    public static final int UNE_FOIS = 200;
    public static final int JOURNALIER = 201;
    public static final int HEBDOMADAIRE = 202;
    public static final int MENSUEL = 203;

    private int id;
    private Double montant;
    private Categorie categorie;
    private String note;
    private GregorianCalendar date;
    private int periode; //annuel, mensuel, hebdomadaire, journalier, non frequenciel
    private int types; //Budget ou Depense

    public Transaction(int types, Double montant, Categorie categorie, String note, GregorianCalendar date, int periode) {

        if (types != BUDGET && types != DEPENSE) {
            //Exception de types TransactionTypeNotExist doit etre lancée
        }

        if (periode != MENSUEL && periode != HEBDOMADAIRE && periode != JOURNALIER && periode != UNE_FOIS) {
            //Exception de type TransactionPeriodeNotExist
        }

        this.types = types;
        this.montant = montant;
        this.note = note;
        this.categorie = categorie;
        this.periode = periode;
        this.date = date;
    }

    public Transaction(int id, int type, Double montant, Categorie categorie, String note, GregorianCalendar date, int periode) {
        this(type, montant, categorie, note, date, periode);
        this.id = id;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Double totalBudget(List<Transaction> list, Mois mois) {
        double sum = 0;
        for (Transaction t : list) {
            if (t.isBudget())
                sum += t.getTotalDuMois(mois);
        }
        return sum;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Double totalExpensese(List<Transaction> list, Mois mois) {
        double sum = 0;
        for (Transaction t : list) {
            if (t.isExpensise()) {
                sum += t.getTotalDuMois(mois);
            }
        }
        return sum;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Double getTotalDuMois(Mois mois) {
        if (this.getPeriodes() == Transaction.UNE_FOIS || this.getPeriodes() == Transaction.MENSUEL)
            return this.getMontant();

        if(mois == null)
            return this.getMontant();

        GregorianCalendar gc = new GregorianCalendar();
        try {
            gc.setTimeInMillis( Calendriers.stringToCalandar(mois.toString()).getTimeInMillis() );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        gc.setTimeInMillis(this.getDate().getTimeInMillis());

        gc.set(GregorianCalendar.MONTH, this.getDate().get(GregorianCalendar.MONTH));

        // if(gc == null)
            // return null;

        if (this.getPeriodes() == Transaction.JOURNALIER) {
            int nbJour = gc.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
            return this.getMontant() * nbJour;
        }

        if (this.getPeriodes() == Transaction.HEBDOMADAIRE) {
            int nbHeb = gc.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
            return this.getMontant() * nbHeb;
        }

        return null;
    }


    public boolean isBudget() {
        return (types == BUDGET ? true : false);
    }

    public boolean isExpensise() {
        return !isBudget();
    }

    //Getters and Setters
    public int getTypes() {
        return types;
    }

    public void setTypes(int types) {
        this.types = types;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public int getPeriodes() {
        return periode;
    }

    public void setPeriodes(int frequence) {
        this.periode = frequence;
    }

    public int getPeriodesResID() {
        if (periode == Transaction.UNE_FOIS)
            return R.string.once;
        if (periode == Transaction.JOURNALIER)
            return R.string.chaque_jour;
        if (periode == Transaction.HEBDOMADAIRE)
            return R.string.chaque_semaine;
        if (periode == Transaction.MENSUEL)
            return R.string.chaque_mois;
        return 0;
    }
}


