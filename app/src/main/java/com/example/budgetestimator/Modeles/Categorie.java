package com.example.budgetestimator.Modeles;

import com.example.budgetestimator.R;

import java.util.ArrayList;

public class Categorie {

    public static final ArrayList<Categorie> ALL_EXP;
    public static final ArrayList<Categorie> ALL_BUD;
    public static final ArrayList<Categorie> ALL;

    // Arraylist Categorie Depense
    static {
        ALL_EXP = new ArrayList<>();
        ALL_EXP.add(new Categorie(0, R.string.achat, R.drawable.achat));
        ALL_EXP.add(new Categorie(1, R.string.voyage, R.drawable.voyage));
        ALL_EXP.add(new Categorie(2, R.string.cadeau, R.drawable.cadeau));
        ALL_EXP.add(new Categorie(3, R.string.nourriture, R.drawable.restaurant));
        ALL_EXP.add(new Categorie(4, R.string.education, R.drawable.educations));
        ALL_EXP.add(new Categorie(5, R.string.transport, R.drawable.transports));
        ALL_EXP.add(new Categorie(6, R.string.autre, R.drawable.autre));
    }

    static {
        ALL_BUD = new ArrayList<>();
        ALL_BUD.add(new Categorie(7, R.string.salaire, R.drawable.salaire));
        ALL_BUD.add(new Categorie(8, R.string.cadeau, R.drawable.cadeau));
        ALL_BUD.add(new Categorie(9, R.string.Ma_banque, R.drawable.bank));
        ALL_BUD.add(new Categorie(10, R.string.recompense, R.drawable.recompense));
        ALL_BUD.add(new Categorie(6, R.string.autre, R.drawable.autre));
    }

    static {
        ALL = (ArrayList<Categorie>) ALL_EXP.clone();
        ALL.addAll(ALL_BUD);
    }

    private int id;
    private int label;
    private int icon;

    public Categorie(int id, int label, int icon) {
        this.id = id;
        this.label = label;
        this.icon = icon;
    }

    // public int
    public static Categorie getInstance(int i) {
        if (i <= (ALL.size() - 1) && i >= 0) {
            return ALL.get(i);
        }
        return null;
    }

    public int getId() {
        return this.id;
    }

    public int getStringResId() {
        return this.label;
    }

    public int getDrawableResId() {
        return icon;
    }

}
