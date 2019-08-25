package com.example.samia.bnaentreprise.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samia.bnaentreprise.R;
import com.example.samia.bnaentreprise.commons.entities.EmployeeEntity;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnEmployeeClickListener;

import java.util.List;

public class EmployeesManagerRecyclerAdapter extends RecyclerView.Adapter<EmployeesManagerRecyclerAdapter.ViewHolder>{

    private List<EmployeeEntity> list;
    private OnEmployeeClickListener callback;

    public EmployeesManagerRecyclerAdapter(List<EmployeeEntity> list){
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_employees_manager, viewGroup, false));
    }


    public void setOnEmployeeClickListener(OnEmployeeClickListener callback){
        this.callback = callback;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        EmployeeEntity entity = list.get(i);
        holder.empName.setText(entity.getName() + " " + entity.getLastName());
        holder.empRole.setText(entity.getRole());
        holder.employee = entity;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView empName;
        TextView empRole;
        EmployeeEntity employee;
        ImageView deleteIv;
        ImageView changeIv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            empName = itemView.findViewById(R.id.emp_name_tv);
            empRole = itemView.findViewById(R.id.emp_role_tv);
            changeIv = itemView.findViewById(R.id.change_iv);
            deleteIv = itemView.findViewById(R.id.delete_iv);

            changeIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null)
                        callback.onClick(employee);
                }
            });

            deleteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null)
                        callback.onLongClick(employee);
                }
            });
        }
    }
}
