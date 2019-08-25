package com.example.samia.bnaentreprise.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.samia.bnaentreprise.R;
import com.example.samia.bnaentreprise.activities.AjoutEmployeActivity;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnVerifEmployeListener;
import com.example.samia.bnaentreprise.commons.webservice.AjoutEmployeWebService;

public class EntrerRibDialog extends Dialog{

    private String rib;
    private EditText ribEt;
    private Context context;
    private Button okBtn;
    private ProgressBar progress;

    public EntrerRibDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_rib);

        //init
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ribEt = findViewById(R.id.rib_et);
        okBtn = findViewById(R.id.ok_btn);
        progress = findViewById(R.id.progress);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifRibLen()){
                    showProgress(true);
                    setCancelable(false);
                    new AjoutEmployeWebService(context).verifEmp(ribEt.getText().toString(), new OnVerifEmployeListener() {
                        @Override
                        public void onSucces() {
                            showProgress(false);
                            setCancelable(true);
                            context.startActivity(new Intent(context, AjoutEmployeActivity.class)
                                    .putExtra("rib", ribEt.getText().toString()));
                            dismiss();
                        }

                        @Override
                        public void onRibPasTrouve() {
                            setCancelable(true);
                            showProgress(false);
                            ribEt.setError(context.getString(R.string.rib_not_found));
                        }

                        @Override
                        public void onEmployeExist() {
                            setCancelable(true);
                            showProgress(false);
                            ribEt.setError(context.getString(R.string.emp_exist));
                        }

                        @Override
                        public void onErreur() {
                            setCancelable(true);
                            showProgress(false);
                            new InfoDialog(context, context.getString(R.string.error_cnx));
                        }

                        @Override
                        public void onRibEntr() {
                            setCancelable(true);
                            showProgress(false);
                            ribEt.setError(context.getString(R.string.entr_rib));
                        }
                    });
                }
            }
        });
    }

    private boolean verifRibLen() {
        if (ribEt.getText().toString().length() == 20){
            ribEt.setError(null);
            return true;
        }else{
            ribEt.setError(context.getString(R.string.length_error, "rib", "20"));
            return false;
        }
    }

    private void showProgress(boolean b){
        progress.setVisibility(b ? View.VISIBLE : View.GONE);
        okBtn.setVisibility(b ? View.GONE : View.VISIBLE);
    }
}
