package com.example.samia.bnaentreprise.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.samia.bnaentreprise.R;
import com.example.samia.bnaentreprise.adapters.HistoryRecyclerAdapter;
import com.example.samia.bnaentreprise.commons.entities.HistoryEntity;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnFiltrerHistoriqueListener;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnHistoryFetchListener;
import com.example.samia.bnaentreprise.commons.webservice.HistoryWebSerive;
import com.example.samia.bnaentreprise.dialogs.InfoDialog;
import com.example.samia.bnaentreprise.dialogs.RechercheDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.google.gson.internal.bind.util.ISO8601Utils.format;

public class HistoryActivity extends AppCompatActivity {

    private List<HistoryEntity> data;
    private List<HistoryEntity> filtredData;
    private RecyclerView recyclerView;
    private List<String> nomsEmp;
    private HistoryRecyclerAdapter adapter;
    private boolean estFiltré = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        getSupportActionBar().hide();

        //init
        data = new ArrayList<>();
        filtredData = new ArrayList<>();
        nomsEmp = new ArrayList<>();
        initViews();
        //init rv
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryRecyclerAdapter(filtredData);
        recyclerView.setAdapter(adapter);

        //fetch history
        new HistoryWebSerive(this).get(new OnHistoryFetchListener() {
            @Override
            public void onHistoryFetch(List<HistoryEntity> data) {
                HistoryActivity.this.data.addAll(data);
                filtredData.addAll(data);
                for (HistoryEntity entity : data)
                    if (!nomsEmp.contains(entity.getName()))
                        nomsEmp.add(entity.getName());
                findViewById(R.id.recherche_fab).setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                findViewById(R.id.shimmer).setVisibility(View.GONE);
            }

            @Override
            public void onFailed() {
                new InfoDialog(HistoryActivity.this, getString(R.string.error_cnx)).setOnButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).show();
            }
        });


    }

    private void initViews() {
        ((TextView) findViewById(R.id.list_title_tv)).setText(getString(R.string.history));
        recyclerView = findViewById(R.id.recycler_view);
    }

    public void actionFinish(View v) {
        finish();
    }

    public void actionFabFiltrer(final View view) {
        if (!estFiltré)
            new RechercheDialog(this).setNomsEmp(nomsEmp).setOnFiltrerHistoriqueListener(new OnFiltrerHistoriqueListener() {
                @Override
                public void filtrerNom(String nomEmp, String date) {
                    if (nomEmp.length() > 0 || date.contains("/")) {
                        filtredData.clear();
                        estFiltré = true;
                        ((FloatingActionButton) view).setImageResource(R.drawable.ic_close_white);
                    }

                    for (HistoryEntity entity : data)
                        if (nomEmp.length() > 0 && date.contains("/")) {
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                            if (entity.getName().equals(nomEmp) && format.format(entity.getPaymentDate()).equals(date))
                                filtredData.add(entity);
                        } else if (nomEmp.length() > 0) {
                            if (entity.getName().equals(nomEmp))
                                filtredData.add(entity);
                        } else if (date.contains("/")){
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                            Log.e(date, format.format(entity.getPaymentDate()));
                            if (format.format(entity.getPaymentDate()).equals(date))
                                filtredData.add(entity);
                        }
                            adapter.notifyDataSetChanged();
                }
            }).show();
        else {
            filtredData.clear();
            filtredData.addAll(data);
            adapter.notifyDataSetChanged();
            estFiltré = false;
            ((FloatingActionButton) view).setImageResource(R.drawable.ic_search_white);
        }

    }
}
