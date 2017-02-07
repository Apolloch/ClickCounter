package com.example.bacquet.myapplication.clickCounting;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.bacquet.myapplication.config.Config;
import com.example.bacquet.myapplication.requests.ParamStringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DIENG on 06/01/2017.
 */

public class ClickReportSender {
    Context context;
    private static String TAG = "ClickReportSender";
    public ClickReportSender(Context context) {
        this.context = context;
    }

    public void sendClickReport(JSONObject clickReport){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url =Config.URL +"/appColis/saveClickReport.php";
        Map<String,String> params = new HashMap<String, String>();
        params.put("user",Config.USER);
        params.put("pwd",Config.PWD);

        params.put("click_report",clickReport.toString());

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: ",error);
            }
        };

        final ParamStringRequest request= new ParamStringRequest(url, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: "+"click report successfully sent");
            }
        },errorListener);
        queue.add(request);

    }
}
