package com.example.budgetestimator.Views;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
//import android.widget.Toolbar;

import com.example.budgetestimator.Controlors.ListTransactionAdapteur;
import com.example.budgetestimator.Databases.PeriodesDatabases;
import com.example.budgetestimator.Databases.TransactionDatabases;
import com.example.budgetestimator.Databases.CalendriersDatabases;
import com.example.budgetestimator.Modeles.Mois;
import com.example.budgetestimator.Modeles.Transaction;
import com.example.budgetestimator.Modeles.Calendriers;
import com.example.budgetestimator.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static Context mainContext;
    protected static TransactionDatabases transactionDatabases;
    private static PeriodesDatabases periodesDatabases;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private CalendriersDatabases calendriersDatabases;
    private ArrayList<Mois> moisEcoulesList;

    FloatingActionButton fab_AddBudget, fab_AppDepense;
    ExtendedFloatingActionButton fab_menuAdd;
    TextView fab_AddBudget_text, fab_AddDepense_text;
    Boolean isAllFabsVisible;
    CardView txtCardView, txtCardView2;

    Toolbar toolbar;

    public static void listItemClicked(Transaction transaction) {
        Dialog deleteTransaction = new TransactionDelete((Activity) mainContext, transaction, transactionDatabases);
        deleteTransaction.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        deleteTransaction.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainContext = this;

        // Créez l'adaptateur qui renverra un fragment pour chacun des trois
        // parties principales de l'activité.

        // configurer les fichier BD pour les requêtes de bases de données
        calendriersDatabases = new CalendriersDatabases(this);
        periodesDatabases = new PeriodesDatabases(this);
        transactionDatabases = new TransactionDatabases(this);

        calendriersDatabases.mettreAjourNbLancementApp();
        if (calendriersDatabases.nombreLancementApp() == 1) {
            premierLancementinsertion();
            Log.i(Calendriers.MY_LOG, "nombreLancementapp()");
        }

        initMoisEcoulesList();

        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Configurez le ViewPager avec l'adaptateur de sections.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(moisEcoulesList.size() - 1);

        PagerTabStrip pagerTabStrip = (PagerTabStrip) findViewById(R.id.pagerTitleStrip);
        pagerTabStrip.setTextColor(Color.WHITE);
        pagerTabStrip.setTabIndicatorColor(Color.parseColor("#FF54D4"));

        fab_menuAdd = (ExtendedFloatingActionButton)findViewById(R.id.fab_menuAdd);
        fab_AddBudget = (FloatingActionButton) findViewById(R.id.fab_AddBudget);
        fab_AppDepense = (FloatingActionButton) findViewById(R.id.fab_AppDepense);
        fab_AddBudget_text = (TextView)findViewById(R.id.fab_AddBudget_text);
        fab_AddDepense_text = (TextView)findViewById(R.id.fab_AddDepense_text);
        txtCardView = (CardView) findViewById(R.id.txtCardView);
        txtCardView2 = (CardView) findViewById(R.id.txtCardView2);

        fab_AddBudget.setVisibility(View.GONE);
        fab_AppDepense.setVisibility(View.GONE);
        fab_AddBudget_text.setVisibility(View.GONE);
        fab_AddDepense_text.setVisibility(View.GONE);
        txtCardView.setVisibility(View.GONE);
        txtCardView2.setVisibility(View.GONE);

        isAllFabsVisible = false;
        fab_menuAdd.shrink();

        fab_menuAdd.setOnClickListener(view -> {
            if (!isAllFabsVisible) {
                fab_AddBudget.show();
                fab_AppDepense.show();
                fab_AddBudget_text.setVisibility(View.VISIBLE);
                fab_AddDepense_text.setVisibility(View.VISIBLE);
                txtCardView.setVisibility(View.VISIBLE);
                txtCardView2.setVisibility(View.VISIBLE);

                fab_menuAdd.extend();

                isAllFabsVisible = true;
            } else {

                fab_AddBudget.hide();
                fab_AppDepense.hide();
                fab_AddBudget_text.setVisibility(View.GONE);
                fab_AddDepense_text.setVisibility(View.GONE);
                txtCardView.setVisibility(View.GONE);
                txtCardView2.setVisibility(View.GONE);

                fab_menuAdd.shrink();

                isAllFabsVisible = false;
            }
        });

        fab_AddBudget.setOnClickListener(view -> {
            Intent activitySwitcher = new Intent(getApplicationContext(), TransactionActivity.class);
            activitySwitcher.putExtra("types", Transaction.BUDGET);
            activitySwitcher.putExtra("titleResID", R.string.ajuster_budget);
            startActivity(activitySwitcher);
        });

        fab_AppDepense.setOnClickListener(view -> {
            Intent activitySwitcher = new Intent(getApplicationContext(), TransactionActivity.class);
            activitySwitcher.putExtra("types", Transaction.DEPENSE);
            activitySwitcher.putExtra("titleResID", R.string.ajouter_depense);
            startActivity(activitySwitcher);
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    private void premierLancementinsertion() {
        periodesDatabases.insertionLorsDuPremierLancement();
    }

    private void initMoisEcoulesList() {
        moisEcoulesList = periodesDatabases.liste();
        Mois moisCourant = Mois.getCurrentMonth();
        Mois dernierMoisEnregistre = moisEcoulesList.get(moisEcoulesList.size() - 1);
        if (dernierMoisEnregistre.isBefore(moisCourant) || dernierMoisEnregistre.isAfter(moisCourant)) {
            Log.i(Calendriers.MY_LOG, "dans if de initMois");
            periodesDatabases.reInit();
            moisEcoulesList = periodesDatabases.liste();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Gonflez le menu; cela ajoute des éléments à la barre d'action si elle est présente.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_home:
                mViewPager.setCurrentItem(moisEcoulesList.size() - 1);
                return true;
            case R.id.menu_rapport:
                return true;

        }

        return super.onOptionsItemSelected(item);
    }


    // Fragment d'espace réservé contenant une vue simple.
    public static class PlaceholderFragment extends Fragment {

        // L'argument de fragment représentant le numéro de section pour ce fragment.
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        // Renvoie une nouvelle instance de ce fragment pour le numéro de section donné
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            int numSlide = getArguments().getInt(ARG_SECTION_NUMBER);
            ArrayList<Mois> listeDesMois = periodesDatabases.liste();
            Mois moisDuFragment = null;
            List<Transaction> listDesTransactionsDuSlide;
            Mois ceMoisCi = null;

            if (numSlide < listeDesMois.size()) {
                // Page liste mois de transaction
                moisDuFragment = listeDesMois.get(numSlide);
                listDesTransactionsDuSlide = transactionDatabases.listDuMois(moisDuFragment);
                List<Transaction> listDesTransactionsFreq = transactionDatabases.listAvantpFreqMois(moisDuFragment);
                listDesTransactionsDuSlide.addAll(listDesTransactionsFreq);
            } else {
                // Page Futures transaction
                ceMoisCi = listeDesMois.get(listeDesMois.size() - 1);
                listDesTransactionsDuSlide = transactionDatabases.listApresMois(ceMoisCi);
            }

            TextView textTotalDepense = rootView.findViewById(R.id.text_total_depense);
            TextView textTotalBudget = rootView.findViewById(R.id.text_total_budget);
            TextView textResultatGP = rootView.findViewById(R.id.resultatGP);
            TextView textViewDifference = rootView.findViewById(R.id.text_difference);
            final ListView listView = rootView.findViewById(R.id.listview);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                     listItemClicked((Transaction) listView.getItemAtPosition(position));
                }
            });

            listView.setAdapter(new ListTransactionAdapteur(mainContext, listDesTransactionsDuSlide));

            final Double totalDepense = Transaction.totalExpensese(listDesTransactionsDuSlide, moisDuFragment);
            final Double totalBudet = Transaction.totalBudget(listDesTransactionsDuSlide, moisDuFragment);
            final Double difference = totalBudet - totalDepense;

            textTotalDepense.setText(totalDepense.toString());
            textTotalBudget.setText(totalBudet.toString());
            textViewDifference.setText(difference.toString());

            if (difference < 0){
                // Afficher perte
                textViewDifference.setTextColor(Color.parseColor("#f91626"));
                textResultatGP.setText(R.string.txt_perte);
            }else {
                //Afficher Gain
                textViewDifference.setTextColor(Color.parseColor("#15c28c"));
                textResultatGP.setText(R.string.txt_gain);
            }
//            else if (difference ){
//                textViewDifference.setTextColor(Color.parseColor("#fff"));
//                textResultatGP.setText(R.string.txt_aucun);
//            }

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem est appelé pour instancier le fragment pour la page donnée.
            //Renvoie un PlaceholderFragment (défini comme une classe interne statique ci-dessous).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return moisEcoulesList.size() + 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == moisEcoulesList.size())
                return getResources().getString(R.string.mois_proche);
            if (position == moisEcoulesList.size() - 1)
                return getResources().getString(R.string.ce_mois);
            if (position == moisEcoulesList.size() - 2)
                return getResources().getString(R.string.mois_dernier);
            return moisEcoulesList.get(position).toString();
        }
    }


}