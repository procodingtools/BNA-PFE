package com.example.samia.bnaentreprise.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.samia.bnaentreprise.R;
import com.example.samia.bnaentreprise.dialogs.InfoDialog;

import java.util.List;

import static com.example.samia.bnaentreprise.commons.StaticObjects.ENTREPRISE_ENTITY;
import static com.example.samia.bnaentreprise.commons.StaticObjects.ENTREPRISE_RIB;

public class RibActivity extends AppCompatActivity {

    private EditText ribEt;
    private Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rib);

        //init
        initViews();
        getSupportActionBar().hide();

        //check rib & go continue LOGIN
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> ribs = ENTREPRISE_ENTITY.getRibs();
                if (ribs.contains(ribEt.getText().toString())) {
                    ENTREPRISE_RIB = ribEt.getText().toString();
                    startActivity(new Intent(RibActivity.this, ActionsActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                else {
                    new InfoDialog(RibActivity.this, getString(R.string.error_rib)).show();
                }
            }
        });
    }

    private void initViews() {
        ribEt = findViewById(R.id.rib_et);
        nextBtn = findViewById(R.id.next_btn);
    }
}
