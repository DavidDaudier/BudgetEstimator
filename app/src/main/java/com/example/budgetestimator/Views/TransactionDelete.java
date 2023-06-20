package com.example.budgetestimator.Views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;

import com.example.budgetestimator.Databases.TransactionDatabases;
import com.example.budgetestimator.Modeles.Categorie;
import com.example.budgetestimator.Modeles.Transaction;
import com.example.budgetestimator.Modeles.Calendriers;
import com.example.budgetestimator.R;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TransactionDelete extends Dialog implements View.OnClickListener{

    public Activity c;
    private Transaction transaction;
    private TransactionDatabases transactionDatabases;
    private AppCompatButton buttonDelete, bouttonUpdate;
    private TextView textViewType, textViewDate, textViewNote, textViewPeriode, textViewMontantTransaction, textViewCategorieTransaction;
    private EditText EditDate, EditNote, EditMontant;
    private final static Calendar calendar = Calendar.getInstance();
    private static EditText datePicker;
    private Spinner spinnerPeriode;
    Context context;
    private ImageView imageViewCategorie, imageViewType;

    public TransactionDelete(Activity a, Transaction t, TransactionDatabases transactionDatabases) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.transactionDatabases = transactionDatabases;
        transaction = t;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.delete_transaction);

        DisplayMetrics dm = new DisplayMetrics();
        c.getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setLayout((int) (width * 1), height);

        init();

        buttonDelete.setOnClickListener(this);
        bouttonUpdate.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init() {

        textViewType = findViewById(R.id.textViewTypeTransaction);
        EditMontant = findViewById(R.id.editMontantTransaction);
        imageViewCategorie = findViewById(R.id.imageViewCategorieTransaction);
        textViewCategorieTransaction = findViewById(R.id.textViewCategorieTransaction);
        EditDate = findViewById(R.id.editDateTransaction);
        EditNote = findViewById(R.id.editNoteTransaction);
        spinnerPeriode = findViewById(R.id.spinnerPeriodeTransaction);

        buttonDelete = findViewById(R.id.buttonDeleteTransaction);
        bouttonUpdate = findViewById(R.id.buttonUpdateTransaction);

        imageViewType = findViewById(R.id.imageViewTypeTransaction);

        int typeResId = 0;
        int typeIconId = 0;
        if (transaction.isBudget()) {
            typeResId = R.string.budget;
            typeIconId = R.drawable.ic_income_up;
             textViewType.setTextColor(Color.parseColor("#15c28c"));
        } else {
            typeResId = R.string.depense;
            typeIconId = R.drawable.ic_expense_down;
             textViewType.setTextColor(Color.parseColor("#f91626"));
        }

        imageViewType.setImageResource(typeIconId);
        textViewType.setText(typeResId);
        EditMontant.setText(transaction.getMontant().toString());
        imageViewCategorie.setImageResource(transaction.getCategorie().getDrawableResId());
        textViewCategorieTransaction.setText(transaction.getCategorie().getStringResId());
        EditDate.setText(Calendriers.calandarToString(transaction.getDate()));
        EditNote.setText(transaction.getNote());
        spinnerPeriode.getSelectedItem();
    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private void buttonModifierAction() {
//        double montant = 0;
//        try {
//            montant = (double) Integer.parseInt(EditMontant.getText().toString());
//        } catch (Exception e) {
//            montant = (double) Integer.parseInt(EditMontant.getHint().toString());
//            EditMontant.setHintTextColor(Color.RED);
//            Toast.makeText(context, R.string.montant_erreur, Toast.LENGTH_LONG).show();
//            return;
//        }
//        Categorie categorie = (Categorie) textViewCategorieTransaction.getText();
//        String note = EditNote.getText().toString();
//        String dateStr = EditDate.getText().toString();
////        date = Calendriers.stringToCalandar(datePicker.getText().toString());
//
////        GregorianCalendar date = null;
////        try {
////            String dateStr = EditDate.getText().toString();
////            //  String tvDate = tvDisplayDate.getText().toString();
////            if (dateStr.equalsIgnoreCase(tos))
////                date = new GregorianCalendar();
////            else
////                date = Calendriers.stringToCalandar(datePicker.getText().toString());
////        } catch (ParseException e) {
////            e.printStackTrace();
////        }
//
//        int periode = Transaction.UNE_FOIS + spinnerPeriode.getSelectedItemPosition();
//
////        int typeDeTransaction = getContext().getIntExtra("types", -1); //Budget ou Depense
////        Transaction transaction = new Transaction(typeDeTransaction, montant, categorie, note, dateStr, periode);
//
//        if (transactionDatabases.ajouterTransaction(transaction)) {
//
//            Toast.makeText(context, R.string.msg_reussi, Toast.LENGTH_SHORT).show();
//
//        }
////        else if (transactionDatabases.modifierTransaction(e, transaction)){
////            Toast.makeText(this, R.string.msg_reussi, Toast.LENGTH_SHORT).show();
////        }
////        finish();
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonDeleteTransaction:
                transactionDatabases.supprimerTransaction(transaction.getId());
                c.recreate();
                break;

            case R.id.buttonUpdateTransaction:
                transactionDatabases.modifierTransaction(transaction.getId(), transaction);
                c.recreate();
                break;
        }
        dismiss();
    }

}
