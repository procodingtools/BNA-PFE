package com.example.samia.bnaentreprise.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samia.bnaentreprise.R;
import com.example.samia.bnaentreprise.commons.entities.SalaryResponseEntity;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnEmployeeClickListener;

import java.util.List;

public class PaidSalaryRecyclerAdapter extends RecyclerView.Adapter<PaidSalaryRecyclerAdapter.ViewHolder>{

    private List<SalaryResponseEntity> list;
    private OnEmployeeClickListener callback;

    public PaidSalaryRecyclerAdapter(List<SalaryResponseEntity> list){
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_paid_salary, viewGroup, false));
    }


    public void setOnEmployeeClickListener(OnEmployeeClickListener callback){
        this.callback = callback;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        SalaryResponseEntity entity = list.get(i);
        holder.empName.setText(entity.getName());
        holder.isPaidIv.setImageResource(entity.isPaid() ? R.drawable.ic_check_circle_green : R.drawable.ic_check_circle_grey);
        holder.dateTv.setText(new DateFormat().format("dd/MM/yyyy HH:mm", entity.getPaymentDate()));
        holder.salaryTv.setText(entity.getSalary() + " DT");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView empName;
        ImageView isPaidIv;
        TextView salaryTv;
        TextView dateTv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            empName = itemView.findViewById(R.id.emp_name_tv);
            isPaidIv = itemView.findViewById(R.id.is_paid_iv);
            salaryTv = itemView.findViewById(R.id.salary_tv);
            dateTv = itemView.findViewById(R.id.date_tv);
        }
    }
}
