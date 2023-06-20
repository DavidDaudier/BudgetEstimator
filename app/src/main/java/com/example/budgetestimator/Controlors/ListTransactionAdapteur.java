package com.example.budgetestimator.Controlors;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.budgetestimator.Modeles.Calendriers;
import com.example.budgetestimator.Modeles.Categorie;
import com.example.budgetestimator.Modeles.Transaction;
import com.example.budgetestimator.R;

import java.util.List;

public class ListTransactionAdapteur extends ArrayAdapter<Transaction> {

    public ListTransactionAdapteur(Context context, List<Transaction> transactions) {
        super(context, R.layout.row_list_transaction, transactions);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View recup, ViewGroup parent) {

        View row = recup;
        if (row == null) {
            LayoutInflater li = LayoutInflater.from(parent.getContext());
            row = li.inflate(R.layout.row_list_transaction, parent, false);
        }

        ImageView iconCategorie = row.findViewById(R.id.imageCategories);
        TextView textViewCategorie = row.findViewById(R.id.txtcategories);
        TextView textViewDate = row.findViewById(R.id.date);
        TextView textViewMontant = row.findViewById(R.id.montant);
        TextView textViewType = row.findViewById(R.id.row_textview_type);

        Transaction transaction = getItem(position);

        iconCategorie.setImageResource(transaction.getCategorie().getDrawableResId());
        textViewCategorie.setText(transaction.getCategorie().getStringResId());
        textViewDate.setText(Calendriers.calandarToString(transaction.getDate()));
        textViewMontant.setText(transaction.getMontant().toString());

        int typeResId = R.string.depense;
        if (transaction.isBudget()){
            typeResId = R.string.budget;
            textViewMontant.setTextColor(Color.parseColor("#15c28c"));
            textViewType.setText(typeResId);
        }
        else{
            typeResId = R.string.depense;
            textViewMontant.setTextColor(Color.parseColor("#f91626"));
            textViewType.setText(typeResId);

        }

        return row;
    }

}
