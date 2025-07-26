package com.example.blfatsamples.constants;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ApiQueue {
    private static ApiQueue instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private ApiQueue(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized ApiQueue getInstance(Context context) {
        if (instance == null) {
            instance = new ApiQueue(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
