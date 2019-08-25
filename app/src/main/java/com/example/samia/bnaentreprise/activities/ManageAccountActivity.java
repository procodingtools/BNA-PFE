package com.example.samia.bnaentreprise.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.samia.bnaentreprise.R;

public class ManageAccountActivity extends AppCompatActivity {

    private Button checkBalanceBtn;
    private Button historyBtn;
    private Button chgPasswdBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        //hidding action bar
        getSupportActionBar().hide();

        //init
        //init views
        initViews();

        chgPasswdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageAccountActivity.this, ChangePasswdActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageAccountActivity.this, HistoryActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        checkBalanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageAccountActivity.this, CheckBalanceActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    private void initViews() {
        checkBalanceBtn = findViewById(R.id.chk_balance_btn);
        chgPasswdBtn = findViewById(R.id.chg_passwd_btn);
        historyBtn = findViewById(R.id.history_btn);
    }
}
