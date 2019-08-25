package com.example.samia.bnaentreprise.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.samia.bnaentreprise.R;

public class ActionsActivity extends AppCompatActivity {

    private Button salaryBtn;
    private Button manageEmpBtn;
    private Button manageAccountBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actions);

        //hidding actionbar
        getSupportActionBar().hide();

        //init
        //starting init views
        initViews();

        //setting listeners
        salaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(
                        new Intent(
                                ActionsActivity.this,
                                PaySalaryActivity.class
                        )
                );
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        manageEmpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(
                        new Intent(
                                ActionsActivity.this,
                                EmployeesManagerActivity.class
                        )
                );
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        manageAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActionsActivity.this, ManageAccountActivity.class));
            }
        });
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void initViews() {
        salaryBtn = findViewById(R.id.salary_payement_btn);
        manageEmpBtn = findViewById(R.id.manage_emp_btn);
        manageAccountBtn = findViewById(R.id.manage_acc_btn);
    }
}
