package com.example.samia.bnaentreprise.commons.webservice;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.samia.bnaentreprise.commons.StaticObjects;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnBalanceFetchListener;

import org.json.JSONException;
import org.json.JSONObject;

public class BalanceWebService {

    private RequestQueue queue;

    public BalanceWebService(Context context){
        queue = Volley.newRequestQueue(context);
    }

    public BalanceWebService get(final OnBalanceFetchListener callback){

        JsonObjectRequest request = new JsonObjectRequest(WebServiceConfig.BALANCE + StaticObjects.ENTREPRISE_RIB, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    callback.onSuccess(response.getString("solde"));
                } catch (JSONException e) {
                    Log.e("json error", e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailed();
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy());

        queue.add(request);

        return this;
    }
}
