package com.example.samia.bnaentreprise.commons.interfaces.callbacks;

import android.support.annotation.Nullable;

import com.example.samia.bnaentreprise.commons.entities.SalaryResponseEntity;

import java.util.List;

public interface OnSalaryPaymentComplete {
    void onSuccess (List<SalaryResponseEntity> successPayements);
    void onFailed (int statusCode);
}
