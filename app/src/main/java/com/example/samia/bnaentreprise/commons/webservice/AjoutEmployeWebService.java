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
import com.android.volley.toolbox.Volley;
import com.example.samia.bnaentreprise.commons.StaticObjects;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnAjoutEmploye;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnVerifEmployeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AjoutEmployeWebService {

    private RequestQueue queue;

    public AjoutEmployeWebService(Context context){
        queue = Volley.newRequestQueue(context);
    }

    public AjoutEmployeWebService verifEmp(String rib, final OnVerifEmployeListener callback){

        String url = WebServiceConfig.VERIF_EMP + StaticObjects.ENTREPRISE_ENTITY.getId() + "/" + rib;


        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                callback.onEmployeExist();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    if (error.networkResponse.statusCode == 410)
                        callback.onSucces();
                    else if (error.networkResponse.statusCode == 404)
                        callback.onRibPasTrouve();
                    else if (error.networkResponse.statusCode == 406)
                        callback.onRibEntr();
                }else
                    callback.onErreur();
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy());
        queue.add(request);

        return this;
    }

    public AjoutEmployeWebService ajouter(String data, final OnAjoutEmploye callback){

        JSONObject object = null;

        try {
            object = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = WebServiceConfig.EMPLOYEES + StaticObjects.ENTREPRISE_ENTITY.getId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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
