package com.example.samia.bnaentreprise.commons.interfaces.callbacks;

import com.example.samia.bnaentreprise.commons.entities.EmployeeEntity;

import java.util.List;

public interface OnEmployeesFetchComplete {
    void onSuccess(List<EmployeeEntity> entities);
    void onFailed(int errorCode);
}
