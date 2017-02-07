package com.example.bacquet.myapplication.requests;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Itinerance on 10/06/2016.
 * Http request with post params
 */
public class ParamStringRequest extends Request<String> {

    private Listener<String> listener;
    private Map<String, String> params;

    public ParamStringRequest(String url, Map<String, String> params,
                              Listener<String> reponseListener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    public ParamStringRequest(int method, String url, Map<String, String> params,
                              Listener<String> reponseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.params = params;

        this.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    };

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new String(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));

        }

    }

    @Override
    protected void deliverResponse(String response) {
        listener.onResponse(response);
    }

}