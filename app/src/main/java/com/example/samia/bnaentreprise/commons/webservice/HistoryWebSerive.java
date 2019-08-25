package com.example.samia.bnaentreprise.commons.webservice;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.samia.bnaentreprise.commons.StaticObjects;
import com.example.samia.bnaentreprise.commons.entities.HistoryEntity;
import com.example.samia.bnaentreprise.commons.interfaces.callbacks.OnHistoryFetchListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryWebSerive {
    private RequestQueue queue;

    public HistoryWebSerive(Context context){
        queue = Volley.newRequestQueue(context);
    }

    public HistoryWebSerive get(final OnHistoryFetchListener callback){

        final String url = WebServiceConfig.HISTORY + StaticObjects.ENTREPRISE_ENTITY.getId() + "/"+ StaticObjects.ENTREPRISE_RIB;

        final JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<HistoryEntity> data = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++){
                            try {
                                JSONObject object = response.getJSONObject(i);
                                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                                HistoryEntity entity = gson.fromJson(object.toString(), HistoryEntity.class);
                                data.add(entity);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        callback.onHistoryFetch(data);
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
