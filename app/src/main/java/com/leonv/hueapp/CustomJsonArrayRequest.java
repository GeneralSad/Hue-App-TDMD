package com.leonv.hueapp;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class CustomJsonArrayRequest extends JsonRequest<JSONArray> {
    public CustomJsonArrayRequest(int method, String url, @Nullable JSONObject requestBody,
                                  Response.Listener<JSONArray> listener,
                                  @Nullable Response.ErrorListener errorListener) {
        super( method,
                url,
                requestBody.toString(),
                listener,
                errorListener);
    }
    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        Response parsingResult;
        try {
            String responseInText = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            JSONArray returnArray = new JSONArray(responseInText);
            parsingResult = Response.success(returnArray, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JSONException e) {
            parsingResult = Response.error(new VolleyError("Returned info is not a JsonArray"));
        }
        return parsingResult;
    }
}