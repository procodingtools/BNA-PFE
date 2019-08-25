package com.example.samia.bnaentreprise.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.samia.bnaentreprise.R;
import com.example.samia.bnaentreprise.adapters.EmployeesManagerRecyclerAdapter;
import com.example.samia.bnaentreprise.commons.entities.EmployeeEntity;
import com.example.samia.bnaentreprise.commons.interfaces.ListActivityActions;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnEmployeSupprime;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnEmployeeClickListener;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnEmployeesFetchComplete;
import com.example.samia.bnaentreprise.commons.webservice.AjoutEmployeWebService;
import com.example.samia.bnaentreprise.commons.webservice.EmployeesWebService;
import com.example.samia.bnaentreprise.dialogs.EmployeeDetailsDialog;
import com.example.samia.bnaentreprise.dialogs.EntrerRibDialog;
import com.example.samia.bnaentreprise.dialogs.InfoDialog;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class EmployeesManagerActivity extends AppCompatActivity implements ListActivityActions {

    private EmployeesManagerRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private ShimmerFrameLayout shimmer;
    private RefreshBroadcast receiver;
    private List<EmployeeEntity> data;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //hidding action bar
        getSupportActionBar().hide();

        //broadcast instance
        receiver = new RefreshBroadcast();

        data = new ArrayList<>();

        //init
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);
        ((TextView) findViewById(R.id.list_title_tv)).setText(getString(R.string.emp_manage));
        findViewById(R.id.fab).setVisibility(View.VISIBLE);

        shimmer = findViewById(R.id.shimmer);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new EmployeesManagerRecyclerAdapter(data);

        adapter.setOnEmployeeClickListener(new OnEmployeeClickListener() {
            @Override
            public void onClick(EmployeeEntity employee) {
                EmployeeDetailsDialog dialog = new EmployeeDetailsDialog(EmployeesManagerActivity.this, employee);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }

            @Override
            public void onLongClick(final EmployeeEntity employee) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(EmployeesManagerActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(EmployeesManagerActivity.this);
                }
                builder.setTitle(getString(R.string.supprimer));
                builder.setMessage(getString(R.string.deman_supp, employee.getName() + " " + employee.getLastName()));
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {
                        dialog.dismiss();
                        progressDialog.show();
                        supprimerEmp(employee.getId());
                    }
                });
                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.show();
            }
        });

        recyclerView.setAdapter((RecyclerView.Adapter) adapter);
    }

    private void supprimerEmp(String id) {
        new EmployeesWebService(this).supprimerEmp(id, new OnEmployeSupprime() {
            @Override
            public void onSucces() {
                progressDialog.dismiss();
                new InfoDialog(EmployeesManagerActivity.this, "Employé supprimé").title(null).show();
                obtenirEmployes();
            }

            @Override
            public void onEchec() {
                progressDialog.dismiss();
                new InfoDialog(EmployeesManagerActivity.this, getString(R.string.error_cnx)).show();
            }
        });
    }

    private void obtenirEmployes() {
        data.clear();

        //fetch EMPLOYEES
        new EmployeesWebService(this).getEmployeesList(new OnEmployeesFetchComplete() {
            @Override
            public void onSuccess(List<EmployeeEntity> entities) {
                data.addAll(entities);
                shimmer.setVisibility(View.GONE);
                ((RecyclerView.Adapter) adapter).notifyDataSetChanged();
            }

            @Override
            public void onFailed(int errorCode) {
                shimmer.setVisibility(View.GONE);
                new InfoDialog(EmployeesManagerActivity.this, getString(R.string.error_cnx)).show();
            }
        });
    }

    public void actionAjoutEmploye(View v){
        new EntrerRibDialog(this).show();
    }

    public void actionFinish(View view) {
        finish();
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                getCurrentFocus().getWindowToken(), 0);
    }

    public void restartActivity() {
        startActivity(new Intent(this, EmployeesManagerActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        obtenirEmployes();
        registerReceiver(receiver, new IntentFilter(getApplicationContext().getPackageName() + "refresh"));
    }
}

class RefreshBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        EmployeesManagerActivity employeesManagerActivity = (EmployeesManagerActivity) context;
        employeesManagerActivity.restartActivity();
    }
}