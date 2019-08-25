package com.example.samia.bnaentreprise.commons.entities;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EntrepriseEntity {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("nom")
    @Expose
    private String name;

    @SerializedName("matricule_fiscale")
    @Expose
    private String matFiscale;

    @SerializedName("adresse")
    @Expose
    private String address;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("ribs")
    @Expose
    private List<String> ribs;

    public List<String> getRibs() {
        return ribs;
    }

    public void setRibs(List<String> ribs) {
        this.ribs = ribs;
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

    public String getMatFiscale() {
        return matFiscale;
    }

    public void setMatFiscale(String matFiscale) {
        this.matFiscale = matFiscale;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
