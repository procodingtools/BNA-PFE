package com.example.samia.bnaentreprise.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.samia.bnaentreprise.R;
import com.example.samia.bnaentreprise.commons.entities.EmployeeEntity;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnEmployeeClickListener;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnSalaryCheckChangedListener;

import java.util.ArrayList;
import java.util.List;

public class PaySalaryRecyclerAdapter extends RecyclerView.Adapter<PaySalaryRecyclerAdapter.ViewHolder>{

    private List<EmployeeEntity> list;
    private OnEmployeeClickListener callback;
    private OnSalaryCheckChangedListener checkCallback;
    private List<String> selectedEmployees;

    public PaySalaryRecyclerAdapter(List<EmployeeEntity> list){
        this.list = list;
        selectedEmployees = new ArrayList<>();
    }

    public void notifyData(List<EmployeeEntity> list){
        for (int i=0; i<list.size();i++)
            selectedEmployees.add(list.get(i).getId());
        notifyDataSetChanged();
    }

    public void setOnSalaryCheckChangedListener(OnSalaryCheckChangedListener checkCallback){
        this.checkCallback = checkCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_pay_salary, viewGroup, false));
    }

    public void setOnEmployeeClickListener(OnEmployeeClickListener callback){
        this.callback = callback;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        EmployeeEntity entity = list.get(i);
        holder.empName.setText(entity.getName() + " " + entity.getLastName());
        holder.empRole.setText(entity.getSalary());
        holder.employee = entity;
        holder.position = i;
        if (!selectedEmployees.contains(entity.getId())) {
            holder.check.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView empName;
        TextView empRole;
        CheckBox check;
        EmployeeEntity employee;
        int position;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            empName = itemView.findViewById(R.id.emp_name_tv);
            empRole = itemView.findViewById(R.id.emp_role_tv);
            check = itemView.findViewById(R.id.check_box);

            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        selectedEmployees.add(employee.getId());
                    else
                        selectedEmployees.add(employee.getId());

                    checkCallback.oncheckChanged(employee.getId(), isChecked);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null)
                        callback.onClick(employee);
                }
            });
        }
    }
}
