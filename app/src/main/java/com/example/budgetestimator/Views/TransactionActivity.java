package com.example.budgetestimator.Views;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.budgetestimator.Controlors.AdapteurCategorie;
import com.example.budgetestimator.Databases.TransactionDatabases;
import com.example.budgetestimator.Modeles.Categorie;
import com.example.budgetestimator.Modeles.Transaction;
import com.example.budgetestimator.Modeles.Calendriers;
import com.example.budgetestimator.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TransactionActivity extends AppCompatActivity {

    private final static Calendar calendar = Calendar.getInstance();
    private static EditText datePicker;
    protected String todayStr = null;
    private int jour;
    private int mois;
    private int annee;
    private EditText editTextmontant;
    private EditText editTextNote;
    private Spinner spinnerPeriode;
    private Spinner spinnerCategorie;
    private AppCompatButton btnValider;
    private TransactionDatabases transactionDatabases;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        getSupportActionBar().setTitle(getIntent().getIntExtra("titleResID", 0));

        transactionDatabases = new TransactionDatabases(this);
        btnValider = (AppCompatButton) findViewById(R.id.Valider);

        setElement();

        editTextmontant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnValider.setEnabled(editTextmontant.length() > 0);
                // btnValider.setBackgroundResource(R.drawable.bg_save);

                //Bouton Sauvegarder
                btnValider.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buttonValiderAction();
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setElement() {
        todayStr = getResources().getString(R.string.today);

        datePicker = findViewById(R.id.datebudget);

        editTextmontant = findViewById(R.id.montbudget);
        spinnerPeriode = findViewById(R.id.periodeBudget);
        spinnerCategorie = findViewById(R.id.catbubget);

        ArrayList<Categorie> list;
        if (getIntent().getIntExtra("types", 0) == Transaction.BUDGET)
            list = Categorie.ALL_BUD;
        else
            list = Categorie.ALL_EXP;

        spinnerCategorie.setAdapter(new AdapteurCategorie(this, R.layout.item_categories, R.id.txtcat, list));
        editTextNote = (EditText) findViewById(R.id.notebudget);

        datePicker.setText(todayStr);

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void buttonValiderAction() {
        double montant = 0;
        try {
            montant = (double) Integer.parseInt(editTextmontant.getText().toString());
        } catch (Exception e) {
            montant = (double) Integer.parseInt(editTextmontant.getHint().toString());
            editTextmontant.setHintTextColor(Color.RED);
            Toast.makeText(this, R.string.montant_erreur, Toast.LENGTH_LONG).show();
            return;
        }
        Categorie categorie = (Categorie) spinnerCategorie.getSelectedItem();
        String note = editTextNote.getText().toString();


        GregorianCalendar date = null;
        try {
            String dateStr = datePicker.getText().toString();
            //  String tvDate = tvDisplayDate.getText().toString();
            if (dateStr.equalsIgnoreCase(todayStr))
                date = new GregorianCalendar();
            else
                date = Calendriers.stringToCalandar(datePicker.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int periode = Transaction.UNE_FOIS + spinnerPeriode.getSelectedItemPosition();

        int typeDeTransaction = getIntent().getIntExtra("types", -1); //Budget ou Depense
        Transaction transaction = new Transaction(typeDeTransaction, montant, categorie, note, date, periode);

        if (transactionDatabases.ajouterTransaction(transaction)) {

            Toast.makeText(this, R.string.msg_reussi, Toast.LENGTH_SHORT).show();

        }
//        else if (transactionDatabases.modifierTransaction(e, transaction)){
//            Toast.makeText(this, R.string.msg_reussi, Toast.LENGTH_SHORT).show();
//        }
        finish();
    }

}