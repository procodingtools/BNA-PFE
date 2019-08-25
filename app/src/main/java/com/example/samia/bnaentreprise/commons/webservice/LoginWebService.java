package com.example.samia.bnaentreprise.commons.webservice;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.samia.bnaentreprise.commons.entities.EntrepriseEntity;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnLoginListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginWebService {

    private RequestQueue queue;

    public LoginWebService(Context context){
        queue = Volley.newRequestQueue(context);
    }

    public LoginWebService loginWebService(String user, String passwd,final OnLoginListener callback){
        JSONObject object = new JSONObject();

        try {
            object.put("id", user);
            object.put("passwd", passwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, WebServiceConfig.LOGIN, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                callback.onLogin(gson.fromJson(response.toString(), EntrepriseEntity.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null)
                    callback.onError(error.networkResponse.statusCode);
                else
                    callback.onError(0);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy());
        queue.add(request);

        return this;
    }
}
