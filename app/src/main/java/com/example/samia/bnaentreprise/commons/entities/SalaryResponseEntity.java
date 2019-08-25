package com.example.samia.bnaentreprise.commons.entities;

import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class SalaryResponseEntity implements Serializable{

    @SerializedName("id_emp")
    @Expose
    private String idEmp;

    @SerializedName("status")
    @Expose
    private boolean isPaid;

    @SerializedName("nom")
    @Expose
    private String name;

    @SerializedName("date_paiement")
    @Expose
    private Date paymentDate;

    @SerializedName("salaire")
    @Expose
    private String Salary;

    public String getSalary() {
        return Salary;
    }

    public String getName() {
        return name;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public String getIdEmp() {
        return idEmp;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public String toJson(){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(this);
    }
}
