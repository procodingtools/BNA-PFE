package com.example.samia.bnaentreprise.commons.interfaces.callbacks;

import com.example.samia.bnaentreprise.commons.entities.EmployeeEntity;

public interface OnEmployeeClickListener {
    void onClick(EmployeeEntity employee);
    void onLongClick(EmployeeEntity employee);
}
