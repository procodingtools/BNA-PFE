package com.example.samia.bnaentreprise.commons.interfaces;

import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnEmployeeClickListener;

public interface EmployeesListAdapter extends ListAdapter {
    void setOnEmployeeClickListener(OnEmployeeClickListener callback);
}
