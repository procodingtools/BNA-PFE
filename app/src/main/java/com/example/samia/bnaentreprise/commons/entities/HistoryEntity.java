package com.example.samia.bnaentreprise.commons.entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class HistoryEntity {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("rib_emp")
    @Expose
    private String empRib;

    @SerializedName("date_payement")
    @Expose
    private Date paymentDate;

    @SerializedName("salaire")
    @Expose
    private String salary;



    public String getName() {
        return name;
    }

    public String getEmpRib() {
        return empRib;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public String getSalary() {
        return salary;
    }

    public String toJson(){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(this);
    }
}
