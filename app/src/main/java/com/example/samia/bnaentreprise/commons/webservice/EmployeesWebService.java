package com.example.samia.bnaentreprise.commons.webservice;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.samia.bnaentreprise.commons.entities.EmployeeEntity;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnEmployeSupprime;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnEmployeesFetchComplete;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.samia.bnaentreprise.commons.StaticObjects.ENTREPRISE_ENTITY;

public class EmployeesWebService {

    RequestQueue queue;

    public EmployeesWebService (Context context){
        queue = Volley.newRequestQueue(context);
    }

    public EmployeesWebService getEmployeesList(final OnEmployeesFetchComplete callback){

        JsonArrayRequest request = new JsonArrayRequest(WebServiceConfig.EMPLOYEES + ENTREPRISE_ENTITY.getId(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                List<EmployeeEntity> entities = new ArrayList<>();
                for (int i=0; i<response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        EmployeeEntity entity = gson.fromJson(object.toString(), EmployeeEntity.class);
                        entities.add(entity);
                    } catch (JSONException e) {
                        callback.onFailed(0);
                        Log.e("json error", e.getMessage());
                    }
                }
                callback.onSuccess(entities);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null)
                    callback.onFailed(error.networkResponse.statusCode);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy());
        queue.add(request);
        return this;
    }

    public EmployeesWebService updateEmployee(EmployeeEntity entity, final OnEmployeesFetchComplete callback){

        JSONObject object = null;
        try {
            object = new JSONObject(entity.toJson().toString());
            object.put("id_entr", ENTREPRISE_ENTITY.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        object.remove("ribs");
        object.remove("id");
        object.remove("nom");
        object.remove("prenom");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, WebServiceConfig.EMPLOYEES + entity.getId(),
                object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailed(0);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy());
        queue.add(request);

        return this;
    }

    public EmployeesWebService supprimerEmp(String id, final OnEmployeSupprime callback){

        StringRequest request = new StringRequest(Request.Method.DELETE, WebServiceConfig.EMPLOYEES + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSucces();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onEchec();
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy());

        queue.add(request);

        return this;
    }
}
