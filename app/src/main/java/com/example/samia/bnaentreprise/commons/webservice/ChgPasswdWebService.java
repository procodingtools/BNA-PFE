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
import com.example.samia.bnaentreprise.commons.StaticObjects;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnPasswdChangedListener;

import org.json.JSONObject;

import static com.example.samia.bnaentreprise.commons.webservice.WebServiceConfig.CHG_PASSWD;

public class ChgPasswdWebService {

    RequestQueue queue;

    public ChgPasswdWebService(Context context){
        queue = Volley.newRequestQueue(context);
    }

    public ChgPasswdWebService changePasswd(JSONObject object, final OnPasswdChangedListener callback){

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, CHG_PASSWD + StaticObjects.ENTREPRISE_ENTITY.getId(), object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onPasswdChanged(200);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null)
                    callback.onPasswdChanged(error.networkResponse.statusCode);
                else
                    callback.onPasswdChanged(0);
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy());

        queue.add(request);

        return this;
    }


}
