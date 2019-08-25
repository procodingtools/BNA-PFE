package com.example.samia.bnaentreprise.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samia.bnaentreprise.R;
import com.example.samia.bnaentreprise.commons.entities.AjouEmployeEntity;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnAjoutEmploye;
import com.example.samia.bnaentreprise.commons.webservice.AjoutEmployeWebService;
import com.example.samia.bnaentreprise.dialogs.InfoDialog;

import java.util.Calendar;
import java.util.Date;

public class AjoutEmployeActivity extends AppCompatActivity {

    private ProgressBar progress;
    private Button okBtn;
    private EditText nomEt;
    private EditText prenomEt;
    private EditText adresseEt;
    private EditText telEt;
    private EditText roleEt;
    private EditText salaireEt;
    private TextView dateNaissTv;
    private boolean canExit = true;
    private String rib;
    private Calendar date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_employe);

        //init
        date = null;
        rib = getIntent().getStringExtra("rib");
        //initViews
        initViews();

        dateNaissTv.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               new DatePickerDialog(AjoutEmployeActivity.this, new DatePickerDialog.OnDateSetListener() {
                                                   @Override
                                                   public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                                       date = Calendar.getInstance();
                                                       date.set(Calendar.YEAR, year);
                                                       date.set(Calendar.MONTH, month);
                                                       date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                                       dateNaissTv.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                                                   }
                                               }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                                               ).show();
                                           }
                                       });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifTexts()){
                    showProgress(true);
                    canExit = false;
                    String data = creerJson();

                    new AjoutEmployeWebService(AjoutEmployeActivity.this).ajouter(data, new OnAjoutEmploye() {
                        @Override
                        public void onSucces() {
                            canExit = true;
                            showProgress(false);
                            new InfoDialog(AjoutEmployeActivity.this, "Success").setOnButtonClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            }).title(null).setCancellable(false).show();
                        }

                        @Override
                        public void onEchec() {
                            canExit = true;
                            showProgress(false);
                            new InfoDialog(AjoutEmployeActivity.this, getString(R.string.error_cnx)).show();
                        }
                    });
                }
            }
        });
    }

    private String creerJson() {
        AjouEmployeEntity entity = new AjouEmployeEntity();

        entity.setAdresse(adresseEt.getText().toString());
        entity.setNom(nomEt.getText().toString());
        entity.setPhone(telEt.getText().toString());
        entity.setPrenom(prenomEt.getText().toString());
        entity.setRib(rib);
        entity.setRole(roleEt.getText().toString());
        entity.setSalaire(Double.parseDouble(salaireEt.getText().toString()));
        entity.setDateNaiss(date.getTime());
        return entity.toJson();
    }

    private boolean verifTexts() {
        boolean b = true;

        nomEt.setError(null);
        prenomEt.setError(null);
        adresseEt.setError(null);
        telEt.setError(null);
        roleEt.setError(null);
        salaireEt.setError(null);
        dateNaissTv.setError(null);

        if (nomEt.getText().toString().length() == 0){
            b = false;
            nomEt.setError(getString(R.string.put_txt, "le nom"));
        }

        if (prenomEt.getText().toString().length() == 0){
            b = false;
            prenomEt.setError(getString(R.string.put_txt, "le prenom"));
        }

        if (adresseEt.getText().toString().length() == 0){
            b = false;
            adresseEt.setError(getString(R.string.put_txt, "l' adresse"));
        }

        if (telEt.getText().toString().length() == 0){
            b = false;
            telEt.setError(getString(R.string.put_txt, "le téléphone"));
        }

        if (roleEt.getText().toString().length() == 0){
            b = false;
            roleEt.setError(getString(R.string.put_txt, "le role"));
        }

        if (salaireEt.getText().toString().length() == 0){
            b = false;
            salaireEt.setError(getString(R.string.put_txt, "le salaire"));
        }

        if (!dateNaissTv.getText().toString().contains("/")){
            b = false;
            dateNaissTv.setError(getString(R.string.put_txt, "la date de naissaance"));
        }
        return b;
    }

    private void initViews() {
        ((TextView)findViewById(R.id.list_title_tv)).setText(getString(R.string.ajout_emp));
        progress = findViewById(R.id.progress);
        okBtn = findViewById(R.id.add_btn);
        nomEt = findViewById(R.id.nom_et);
        prenomEt = findViewById(R.id.prenom_et);
        adresseEt = findViewById(R.id.adr_et);
        telEt = findViewById(R.id.phone_et);
        roleEt = findViewById(R.id.role_et);
        salaireEt = findViewById(R.id.salary_et);
        dateNaissTv = findViewById(R.id.birth_et);
    }

    private void showProgress(boolean b){
        progress.setVisibility(b ? View.VISIBLE : View.GONE);
        okBtn.setVisibility(b ? View.GONE : View.VISIBLE);
    }

    public void actionFinish(View v){
        if (canExit)
            finish();
    }

    @Override
    public void onBackPressed() {
        if (canExit)
            super.onBackPressed();
        else
            Toast.makeText(this, getString(R.string.please_wait), Toast.LENGTH_SHORT).show();
    }
}
