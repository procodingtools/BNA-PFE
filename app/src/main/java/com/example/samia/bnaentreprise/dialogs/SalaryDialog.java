package com.example.samia.bnaentreprise.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.samia.bnaentreprise.R;
import com.example.samia.bnaentreprise.commons.entities.EmployeeEntity;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnSalaryChangeListener;


public class SalaryDialog extends Dialog {

    private Spinner ribSp;
    private EditText salaryTv;
    private Button saveBtn;
    private TextView nameTv;
    private OnSalaryChangeListener callback;
    private EmployeeEntity employee;

    public SalaryDialog setOnSalaryChangedListener(OnSalaryChangeListener callback){
        this.callback = callback;
        return this;
    }

    public SalaryDialog(@NonNull Context context, EmployeeEntity entity) {
        super(context);
        this.employee = entity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_salary);

        //init
        //init views
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initViews();

        //setting init texts
        setInitialValues();

    }

    private void setInitialValues() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, employee.getRibs());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ribSp.setAdapter(adapter);
        ribSp.setSelection(getIndex(employee.getPrimaryRib()));
        salaryTv.setText(employee.getSalary());
        nameTv.setText(employee.getName() + " " + employee.getLastName());

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employee.setSalary(salaryTv.getText().toString());
                employee.setPrimaryRib(ribSp.getSelectedItem().toString());
                callback.onChange(employee);
                dismiss();
            }
        });
    }

    private void initViews() {
        ribSp = findViewById(R.id.ribs_sp);
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
