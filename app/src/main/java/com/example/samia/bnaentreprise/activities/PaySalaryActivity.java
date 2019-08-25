package com.example.samia.bnaentreprise.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.samia.bnaentreprise.R;
import com.example.samia.bnaentreprise.adapters.PaySalaryRecyclerAdapter;
import com.example.samia.bnaentreprise.commons.StaticObjects;
import com.example.samia.bnaentreprise.commons.entities.EmployeeEntity;
import com.example.samia.bnaentreprise.commons.entities.SalaryResponseEntity;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnEmployeeClickListener;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnEmployeesFetchComplete;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnSalaryChangeListener;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnSalaryCheckChangedListener;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnSalaryPaymentComplete;
import com.example.samia.bnaentreprise.commons.webservice.EmployeesWebService;
import com.example.samia.bnaentreprise.commons.webservice.SalaryWebService;
import com.example.samia.bnaentreprise.dialogs.InfoDialog;
import com.example.samia.bnaentreprise.dialogs.SalaryDialog;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.example.samia.bnaentreprise.commons.StaticObjects.ENTREPRISE_RIB;

public class PaySalaryActivity extends AppCompatActivity {

    private PaySalaryRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private ShimmerFrameLayout shimmer;
    private List<EmployeeEntity> data;
    private List<String> toSendList;
    private Button payBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //hidding action bar
        getSupportActionBar().hide();

        //init
        ((TextView)findViewById(R.id.list_title_tv)).setText(getString(R.string.salary_pay));

        shimmer = findViewById(R.id.shimmer);
        payBtn = findViewById(R.id.pay_btn);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        data = new ArrayList<>();

        adapter = new PaySalaryRecyclerAdapter(data);

        recyclerView.setAdapter(adapter);

        //set item click listener
        adapter.setOnEmployeeClickListener(new OnEmployeeClickListener() {
            @Override
            public void onClick(EmployeeEntity employee) {
                new SalaryDialog(PaySalaryActivity.this, employee)
                        .setOnSalaryChangedListener(new OnSalaryChangeListener() {
                            @Override
                            public void onChange(EmployeeEntity entity) {
                                boolean b = true;
                                int i = -1;
                                do {//starting entity modification
                                    i++;
                                    if (entity.getId() == data.get(i).getId()) {
                                        data.get(i).setSalary(entity.getSalary());
                                        data.get(i).setPrimaryRib(entity.getPrimaryRib());
                                        b = !b;
                                        adapter.notifyDataSetChanged();
                                    }
                                }while (b);
                            }
                        }).show();
            }

            @Override
            public void onLongClick(EmployeeEntity employee) {

            }
        });

        //setting employee checkchanged
        adapter.setOnSalaryCheckChangedListener(new OnSalaryCheckChangedListener() {
            @Override
            public void oncheckChanged(String employeeId, boolean checked) {
                if (checked)
                    toSendList.add(employeeId);
                else
                    toSendList.remove(employeeId);
            }
        });

        new EmployeesWebService(this).getEmployeesList(new OnEmployeesFetchComplete() {
            @Override
            public void onSuccess(List<EmployeeEntity> entities) {
                data.addAll(entities);
                toSendList = new ArrayList<>();
                //adding IDs to send salary
                for (EmployeeEntity datom : data){
                    toSendList.add(datom.getId());
                }

                shimmer.setVisibility(View.GONE);
                adapter.notifyData(data);
                payBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailed(int errorCode) {
                new InfoDialog(PaySalaryActivity.this, getString(R.string.error_cnx));
            }
        });

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    pay();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void pay() throws JSONException {
        JSONArray array = new JSONArray();
        for (String id : toSendList) {
            for (EmployeeEntity employee : data)
                if (id.equals(employee.getId())) {
                    JSONObject object = new JSONObject();
                    object.put("salaire", employee.getSalary());
                    object.put("nom", employee.getName() + " " + employee.getLastName());
                    object.put("rib_entr", ENTREPRISE_RIB);
                    object.put("rib_emp", employee.getPrimaryRib());
                    object.put("id_emp", employee.getId());
                    object.put("id_entr", StaticObjects.ENTREPRISE_ENTITY.getId());
                    array.put(object);
                }
        }
        SalaryWebService service = new SalaryWebService(PaySalaryActivity.this);
        service.paySalary(array);
        service.setOnSalaryPaymentComplete(new OnSalaryPaymentComplete() {
            @Override
            public void onSuccess(final List<SalaryResponseEntity> successPayements) {

                boolean i= true;
                for (SalaryResponseEntity salary : successPayements)
                    if (!salary.isPaid())
                        i = false;
                if (i)
                    new InfoDialog(PaySalaryActivity.this, "success").setOnButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            startActivity(new Intent(PaySalaryActivity.this, PaidEmployrsListActivity.class)
                                    .putExtra("data", (Serializable) successPayements));
                            finish();
                        }
                    }).show();
                else
                    new InfoDialog(PaySalaryActivity.this, getString(R.string.recharge_account)).setOnButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(PaySalaryActivity.this, PaidEmployrsListActivity.class)
                                    .putExtra("data", (Serializable) successPayements));
                            finish();
                        }
                    }).show();

            }

            @Override
            public void onFailed(int statusCode) {
                new InfoDialog(PaySalaryActivity.this, "failed").show();
            }
        });
    }
}
