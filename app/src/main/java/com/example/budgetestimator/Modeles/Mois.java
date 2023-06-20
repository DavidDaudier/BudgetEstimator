package com.example.budgetestimator.Modeles;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Mois implements Comparable<Mois>{

    // On commence à compter à partir de 1
    // donc on a Janvier = 1, Fevrier = 2 ... Decembre = 12
    private int numero;
    private int annee;

    public Mois(int numero, int annee) {
        this.numero = numero;
        this.annee = annee;
    }

    // Crée un mois à partir d'un String de format MM/YYYY ou M/YYYY
    public Mois(String mois) {
        String[] tab = mois.split("/");
        if (tab.length != 2) {
            //TODO throw Exception;
        }
        numero = Integer.parseInt(tab[0]);
        annee = Integer.parseInt(tab[1]);

    }

    public static Mois extractMois(Calendar calendar) {
        int m = calendar.get(Calendar.MONTH) + 1;
        int a = calendar.get(Calendar.YEAR);
        return new Mois(m, a);
    }

    public static Mois getCurrentMonth() {
        GregorianCalendar calendar = new GregorianCalendar();
        return new Mois(calendar.get(GregorianCalendar.MONTH) + 1, calendar.get(Calendar.YEAR));
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        if (numero < 10)
            str.append("0");
        str.append(numero + "/" + annee);
        return str.toString();
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    @Override
    public int compareTo(Mois another) {
        if (this.annee == another.annee)
            return this.numero - another.numero;
        return this.annee - another.annee;
    }

    public boolean equals(Mois another) {
        return (this.compareTo(another) == 0 ? true : false);
    }

    public boolean isAfter(Mois another) {
        return (this.compareTo(another) > 0 ? true : false);
    }

    public boolean isBefore(Mois another) {
        return (!isAfter(another) && !equals(another));
    }

    public boolean isBefore(Calendar calendar) {
        return this.isBefore(extractMois(calendar));
    }

    public boolean isAfter(Calendar calendar) {
        return this.isAfter(extractMois(calendar));
    }

    public boolean includes(Calendar calendar) {
        Mois mois = extractMois(calendar);
        return this.equals(mois);
    }

}
