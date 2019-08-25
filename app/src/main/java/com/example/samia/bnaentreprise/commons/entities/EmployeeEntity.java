package com.example.samia.bnaentreprise.commons.entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class EmployeeEntity implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("nom")
    @Expose
    private String name;

    @SerializedName("prenom")
    @Expose
    private String lastName;

    @SerializedName("adresse")
    @Expose
    private String address;

    @SerializedName("date_naiss")
    @Expose
    private Date birthDate;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("role")
    @Expose
    private String role;

    @SerializedName("salaire")
    @Expose
    private String salary;

    public List<String> getRibs() {
        return ribs;
    }

    public void setRibs(List<String> ribs) {
        this.ribs = ribs;
    }

    @SerializedName("ribs")
    @Expose
    private List<String> ribs;

    @SerializedName("primary_rib")
    @Expose
    private String primaryRib;

    private boolean isPaid;



    //getters and setters

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public String getPrimaryRib() {
        return primaryRib;
    }

    public void setPrimaryRib(String primaryRib) {
        this.primaryRib = primaryRib;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String toJson(){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(this);
    }
}
