package com.example.budgetestimator.Controlors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.budgetestimator.Modeles.Categorie;
import com.example.budgetestimator.R;

import java.util.List;

public class AdapteurCategorie extends ArrayAdapter<Categorie> {

    public AdapteurCategorie(Context context, int resource, int textViewResourceId, List<Categorie> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View recup, ViewGroup parent) {

        Categorie categorie = getItem(position);

        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View itemView = li.inflate(R.layout.item_categories, parent, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageCat);
        TextView textView = (TextView) itemView.findViewById(R.id.txtcat);
        imageView.setImageResource(categorie.getDrawableResId());
        textView.setText(categorie.getStringResId());
        return itemView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Categorie categorie = getItem(position);

        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View itemView = li.inflate(R.layout.item_categories, parent, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageCat);
        TextView textView = (TextView) itemView.findViewById(R.id.txtcat);
        imageView.setImageResource(categorie.getDrawableResId());
        textView.setText(categorie.getStringResId());
        return itemView;
    }

}
