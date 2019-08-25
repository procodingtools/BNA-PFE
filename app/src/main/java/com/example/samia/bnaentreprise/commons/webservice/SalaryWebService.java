package com.example.samia.bnaentreprise.commons.webservice;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.samia.bnaentreprise.commons.entities.SalaryResponseEntity;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnSalaryPaymentComplete;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SalaryWebService {

    private RequestQueue queue;
    private OnSalaryPaymentComplete callback;

    public SalaryWebService (Context context){
        queue = Volley.newRequestQueue(context);
    }

    public void setOnSalaryPaymentComplete(OnSalaryPaymentComplete callback){
        this.callback = callback;
    }

    public void paySalary(JSONArray array){

        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, WebServiceConfig.salary, array, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<SalaryResponseEntity> data = new ArrayList<>();
                for (int i=0; i<response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        data.add(gson.fromJson(object.toString(), SalaryResponseEntity.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callback.onSuccess(data);
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
    }
}
