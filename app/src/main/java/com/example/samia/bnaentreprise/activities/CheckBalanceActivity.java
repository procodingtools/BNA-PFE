package com.example.samia.bnaentreprise.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.samia.bnaentreprise.R;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnBalanceFetchListener;
import com.example.samia.bnaentreprise.commons.webservice.BalanceWebService;
import com.example.samia.bnaentreprise.dialogs.InfoDialog;

public class CheckBalanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_balance);

        getSupportActionBar().hide();

        new BalanceWebService(this).get(new OnBalanceFetchListener() {
            @Override
            public void onSuccess(String balance) {
                findViewById(R.id.progress).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.balance_tv)).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.balance_tv)).setText(getString(R.string.your_balance, balance));
            }

            @Override
            public void onFailed() {
                new InfoDialog(CheckBalanceActivity.this, getString(R.string.error_cnx)).setOnButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).show();
            }
        });
    }

    public void actionFinish(View view){
        finish();
    }
}
