package com.example.samia.bnaentreprise.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.samia.bnaentreprise.R;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnFiltrerHistoriqueListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RechercheDialog extends Dialog{

    private AutoCompleteTextView nomEmp;
    private OnFiltrerHistoriqueListener callback;
    private List<String> nomsEmp;
    private Button okBtn;
    private TextView dateTv;
    private RelativeLayout dateLayout;


    public RechercheDialog(@NonNull Context context) {
        super(context);
    }

    public RechercheDialog setOnFiltrerHistoriqueListener(OnFiltrerHistoriqueListener callback){
        this.callback = callback;
        return this;
    }

    public RechercheDialog setNomsEmp(List<String> nomsEmp){
        this.nomsEmp = nomsEmp;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_recherche_historique);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //init
        initViews();

        setListeners();

    }

    private void setListeners() {
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrer();
            }
        });

        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, month);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        dateTv.setText(new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime()));
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void initViews() {
        dateTv = findViewById(R.id.date_tv);
        dateLayout = findViewById(R.id.date_layout);
        okBtn = findViewById(R.id.ok_btn);
        nomEmp = findViewById(R.id.emp_nom_tv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.select_dialog_item, nomsEmp);

        nomEmp.setThreshold(0);
        nomEmp.setAdapter(adapter);
    }

    public void filtrer(){
        nomEmp.setError(null);
        if (nomEmp.getText().toString().length() > 0 && !nomsEmp.contains(nomEmp.getText().toString()))
            nomEmp.setError(getContext().getString(R.string.verif_nom_emp));
        else {
            callback.filtrerNom(nomEmp.getText().toString(), dateTv.getText().toString());
            dismiss();
        }
    }
}
