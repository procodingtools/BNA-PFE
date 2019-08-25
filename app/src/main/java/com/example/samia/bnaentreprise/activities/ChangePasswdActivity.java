package com.example.samia.bnaentreprise.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.samia.bnaentreprise.R;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnPasswdChangedListener;
import com.example.samia.bnaentreprise.commons.webservice.ChgPasswdWebService;
import com.example.samia.bnaentreprise.dialogs.InfoDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswdActivity extends AppCompatActivity {

    private EditText cureentPasswdEt;
    private EditText newPasswdEt;
    private EditText verifyEt;
    private Button sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_passwd);

        getSupportActionBar().hide();

        //init
        //init views
        initViews();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyEt.setError(null);
                cureentPasswdEt.setError(null);
                if (!checkVerify()){
                    verifyEt.setError(getString(R.string.not_same_passwd));
                }else {
                    try {
                        sendRequest();
                        showProgress(true);
                    } catch (JSONException e) {
                        Log.e("json error", e.getMessage());
                    }
                }
            }
        });

    }

    private boolean checkVerify() {
        return verifyEt.getText().toString().equals(newPasswdEt.getText().toString());
    }

    private void initViews() {
        cureentPasswdEt = findViewById(R.id.current_passwd_et);
        newPasswdEt = findViewById(R.id.new_passwd_et);
        verifyEt = findViewById(R.id.verif_passwd_et);
        sendBtn = findViewById(R.id.send_btn);
    }

    private void sendRequest() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("old_passwd", cureentPasswdEt.getText().toString());
        object.put("new_passwd", newPasswdEt.getText().toString());

        new ChgPasswdWebService(ChangePasswdActivity.this).changePasswd(object, new OnPasswdChangedListener() {
            @Override
            public void onPasswdChanged(int statusCode) {
                if (statusCode == 200)
                    new InfoDialog(ChangePasswdActivity.this, getString(R.string.finished))
                            .setOnButtonClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            }).show();
                else if (statusCode == 406) {
                    new InfoDialog(ChangePasswdActivity.this, getString(R.string.incorrect_passwd)).show();
                    cureentPasswdEt.setError(getString(R.string.incorrect_passwd));
                }
                else
                    new InfoDialog(ChangePasswdActivity.this, getString(R.string.error_cnx));
                showProgress(false);
            }
        });
    }

    private void showProgress(boolean b){
        sendBtn.setVisibility(b ? View.GONE : View.VISIBLE);
        findViewById(R.id.progress).setVisibility(b ? View.VISIBLE : View.GONE);
    }
}
