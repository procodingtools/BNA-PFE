package com.example.samia.bnaentreprise.dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samia.bnaentreprise.R;
import com.example.samia.bnaentreprise.commons.entities.EmployeeEntity;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnEmployeesFetchComplete;
import com.example.samia.bnaentreprise.commons.webservice.EmployeesWebService;

import java.util.Calendar;
import java.util.List;


public class EmployeeDetailsDialog extends Dialog {

    private Spinner ribSp;
    private EditText addressEt;
    private TextView birthTv;
    private EditText phoneEt;
    private EditText roleEt;
    private EditText salaryTv;
    private Button saveBtn;
    private TextView nameTv;
    private Calendar birthDate;

    EmployeeEntity employee;

    public EmployeeDetailsDialog(@NonNull Context context, EmployeeEntity entity) {
        super(context);
        this.employee = entity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_employee_details);

        //init
        birthDate = Calendar.getInstance();
        //init views
        initViews();

        //setting init texts
        setInitialValues();

    }

    private void setInitialValues() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, employee.getRibs());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ribSp.setAdapter(adapter);
        ribSp.setSelection(getIndex(employee.getPrimaryRib()));
        addressEt.setText(employee.getAddress());
        DateFormat formatter = new DateFormat();
        if (employee.getBirthDate() != null)
            birthTv.setText(formatter.format("dd/MM/yyyy", employee.getBirthDate()));
        phoneEt.setText(employee.getPhone());
        roleEt.setText(employee.getRole());
        salaryTv.setText(employee.getSalary());
        nameTv.setText(employee.getName() + " " + employee.getLastName());

        birthTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        birthDate.set(Calendar.YEAR, year);
                        birthDate.set(Calendar.MONTH, month);
                        birthDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        birthTv.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }

    private void update() {
        employee.setAddress(addressEt.getText().toString());
        employee.setBirthDate(birthDate.getTime());
        employee.setPhone(phoneEt.getText().toString());
        employee.setRole(roleEt.getText().toString());
        employee.setSalary(salaryTv.getText().toString());
        employee.setPrimaryRib(ribSp.getSelectedItem().toString());

        new EmployeesWebService(getContext()).updateEmployee(employee, new OnEmployeesFetchComplete() {
            @Override
            public void onSuccess(List<EmployeeEntity> entities) {
                getContext().sendBroadcast(new Intent(getContext().getApplicationContext().getPackageName() + "refresh"));
                dismiss();
            }

            @Override
            public void onFailed(int errorCode) {
                Toast.makeText(getContext(), "Error occured", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void initViews() {
        ribSp = findViewById(R.id.ribs_sp);
        addressEt = findViewById(R.id.adr_et);
        birthTv = findViewById(R.id.birth_et);
        phoneEt = findViewById(R.id.phone_et);
        roleEt = findViewById(R.id.role_et);
        salaryTv = findViewById(R.id.salary_et);
        nameTv = findViewById(R.id.name_tv);
        saveBtn = findViewById(R.id.save_btn);
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getContext().getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                getCurrentFocus().getWindowToken(), 0);
    }

    //private method of your class
    private int getIndex(String rib){
        for (int i=0;i<ribSp.getCount();i++){
            if (ribSp.getItemAtPosition(i).toString().equalsIgnoreCase(rib)){
                return i;
            }
        }

        return 0;
    }

}
