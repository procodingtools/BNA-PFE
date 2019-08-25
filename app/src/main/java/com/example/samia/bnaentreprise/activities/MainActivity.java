package com.example.samia.bnaentreprise.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.samia.bnaentreprise.R;
import com.example.samia.bnaentreprise.commons.entities.EntrepriseEntity;
import com.example.samia.bnaentreprise.commons.webservice.LoginWebService;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnLoginListener;
import com.example.samia.bnaentreprise.dialogs.InfoDialog;

import static com.example.samia.bnaentreprise.commons.StaticObjects.ENTREPRISE_ENTITY;

public class MainActivity extends AppCompatActivity {

    private ImageView logoIV;
    private RelativeLayout loginForm;
    private ProgressBar progressBar;
    private Button loginBtn;
    private EditText userTV;
    private EditText passTv;
    private LoginWebService loginWebService;
    private RelativeLayout toScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        //init
        //init views
        initViews();

        //init LoginWebService
        loginWebService = new LoginWebService(MainActivity.this);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(loginBtn);
                loginWebService.loginWebService(userTV.getText().toString(), passTv.getText().toString(), new OnLoginListener() {
                    @Override
                    public void onLogin(EntrepriseEntity entity) {
                        if (entity != null) {
                            findViewById(R.id.text_layout).setVisibility(View.VISIBLE);
                            userTV.setVisibility(View.GONE);
                            passTv.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            findViewById(R.id.forget_passwd_tv).setVisibility(View.GONE);
                            findViewById(R.id.welcome_txt).setVisibility(View.VISIBLE);
                            ENTREPRISE_ENTITY = entity;
                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {

                                    }
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    startActivity(new Intent(MainActivity.this, RibActivity.class));
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    finish();
                                }
                            }.execute();
                        }
                    }

                    @Override
                    public void onError(int statusCode) {
                        if (statusCode == 0)
                            new InfoDialog(MainActivity.this, getString(R.string.error_cnx)).show();
                        else
                            new InfoDialog(MainActivity.this, getString(R.string.error_login)).show();
                        showProgress(false);
                    }
                });
                showProgress(true);
            }
        });


        //starting init animation
        initAnimation();

    }

    private void showProgress(boolean b) {
        loginBtn.setVisibility(b ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(b ? View.VISIBLE : View.GONE);
        //findViewById(R.id.to_scale_parent).setBackgroundResource(R.drawable.btn_background);
        toScale.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    private void initAnimation() {
        Animation logoAnim = AnimationUtils.loadAnimation(this, R.anim.anim_logo);
        Animation loginFormAnim = AnimationUtils.loadAnimation(this, R.anim.anim_login_form);
        logoAnim.setInterpolator((new AccelerateDecelerateInterpolator()));
        logoAnim.setFillAfter(true);
        logoAnim.setStartOffset(1000);
        logoIV.setAnimation(logoAnim);

        loginFormAnim.setStartOffset(1600);
        loginFormAnim.setDuration(300);
        loginForm.setAnimation(loginFormAnim);



    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                getCurrentFocus().getWindowToken(), 0);
    }

    private void initViews() {
        loginBtn = findViewById(R.id.login_btn);
        logoIV = findViewById(R.id.img_logo);
        loginForm = findViewById(R.id.login_form);
        progressBar = findViewById(R.id.progress);
        userTV = findViewById(R.id.username_tv);
        passTv = findViewById(R.id.pass_tv);
        toScale = findViewById(R.id.to_scale_layout);
    }

    public void actionResetPasswd(View v){
        new InfoDialog(this, getString(R.string.reset_passwd_msg)).show();
    }
}
