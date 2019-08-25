package com.example.samia.bnaentreprise.commons.webservice;

public interface WebServiceConfig {
    String BASE_URL = "http://localhost:8080/index.php/";
        String ENTREPRISE = BASE_URL + "Entreprise/";
        String LOGIN = ENTREPRISE + "Login";
        String EMPLOYEES = BASE_URL + "Employees/";
        String salary = BASE_URL + "Salaire";
        String CHG_PASSWD = ENTREPRISE + "ChgPasswd/";
        String HISTORY = BASE_URL + "History/";
        String BALANCE = ENTREPRISE + "Solde/";
        String VERIF_EMP = EMPLOYEES + "employeExiste/";
        }
