package com.example.samia.bnaentreprise.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.samia.bnaentreprise.R;
import com.example.samia.bnaentreprise.commons.entities.HistoryEntity;

import java.util.List;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder> {

    private List<HistoryEntity> data;

    public HistoryRecyclerAdapter(List<HistoryEntity> data){
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_history, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        HistoryEntity entity = data.get(i);

        holder.empRibTv.setText(entity.getEmpRib());
        holder.salaryTv.setText(entity.getSalary());
        holder.dateTv.setText(new DateFormat().format("dd/MM/yyyy HH:mm", entity.getPaymentDate()));
        holder.nameTv.setText(entity.getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTv;
        private TextView dateTv;
        private TextView salaryTv;
        private TextView empRibTv;
        public ViewHolder(@NonNull View v) {
            super(v);
            nameTv = v.findViewById(R.id.emp_name_tv);
            dateTv = v.findViewById(R.id.date_tv);
            salaryTv = v.findViewById(R.id.salary_tv);
            empRibTv = v.findViewById(R.id.rib_tv);
        }
    }
}
