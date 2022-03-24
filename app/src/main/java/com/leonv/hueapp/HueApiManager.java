package com.leonv.hueapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HueApiManager {

    private static final String LOGTAG = HueApiManager.class.getName();

    private AppCompatActivity context;
    private RequestQueue requestQueue;
    private LightViewModel lightViewModel;

    public HueApiManager(AppCompatActivity appContext) {
        this.context = appContext;
        this.requestQueue = Volley.newRequestQueue(this.context);
        this.lightViewModel = new ViewModelProvider(appContext).get(LightViewModel.class);
    }

    //Setup the connection
    private CustomJsonArrayRequest getLinkRequest() {

        final String url = getStartAdress();

        return new CustomJsonArrayRequest(Request.Method.POST, url, getDeviceNameMessage(), response -> {

            Log.d(LOGTAG, "Linking response: " + response.toString());
            try {
                JSONObject object = response.getJSONObject(0);
                if (object.toString().contains("success")) {

                    String username = object.getJSONObject("success").getString("username");
                    setUsername(username);

                    Toast.makeText(context, "Linked", Toast.LENGTH_SHORT).show();

                }
                Log.d(LOGTAG, response.toString());

            } catch (JSONException e) {
                Log.e(LOGTAG, "JSON parsing error: " + e.getLocalizedMessage());
            }
            queueGetLights();
        }, error -> {
            Log.e(LOGTAG, error.getLocalizedMessage());
        });

    }

    //Request the lights
    private JsonObjectRequest getLightsRequest() {
        final String url = getFullAddress();
        Log.i(LOGTAG, "url: " + url);
        return new JsonObjectRequest(
                url, response -> {
            try {
                Log.i(LOGTAG, "getLightsRequest: " + response.toString());

                JSONObject lights = response.getJSONObject("lights");
                createHueLights(lights);
                this.lightViewModel.setIsLinkVisible(false);
            } catch (JSONException e) {
                Log.d(LOGTAG, "getLightsRequest: " + e.getLocalizedMessage());
            }
        }, error -> {
            if (error.getLocalizedMessage() != null) {
                this.lightViewModel.setIsLinkVisible(true);
                Log.e(LOGTAG, error.getLocalizedMessage());
            } else {
                Log.e(LOGTAG, error.getMessage());
            }
        });
    }

    //Get the device name message
    private JSONObject getDeviceNameMessage() {
        JSONObject message = new JSONObject();
        try {
            message.put("devicetype", "HueApp#LAMP");
            Log.d(LOGTAG, message.toString());
        } catch (JSONException e) {
            Log.e(LOGTAG, e.getLocalizedMessage());
        }
        return message;
    }

    //Set the username and store it in SharedPreferences
    private void setUsername(String username) {
        SharedPreferences sharedPref = this.context.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", username);
        editor.apply();

        Log.i(LOGTAG, "Stored username: " + username);
    }

    //Get the username from SharedPreferences
    private String getUsername() {
        SharedPreferences sharedPref = this.context.getPreferences(Context.MODE_PRIVATE);

        String username = sharedPref.getString("username", "");

        if (username.trim().isEmpty()) {
            Log.w(LOGTAG, "Username SharedPreference was empty");
        }

        return username;
    }

    //Get the address
    private String getStartAdress() {
        String adress = "http://" + "192.168.178.34:8000" + "/api/";
        return adress;
    }

    //Get the full address with username
    private String getFullAddress() {
        return getStartAdress() + getUsername();
    }

    public void setLinkVisible(boolean isLinkVisible) {
        this.lightViewModel.setIsLinkVisible(isLinkVisible);
    }

    private void createHueLights(JSONObject lights) {

        ArrayList<HueLight> hueLights = new ArrayList<>();

        for (int i = 1; i <= lights.length(); i++) {

            try {

                String lightId = Integer.toString(i);

                JSONObject light = lights.getJSONObject(lightId);

                String lightName = light.getString("name");

                JSONObject HSBValues = light.getJSONObject("state");
                int hue = HSBValues.getInt("hue");
                int saturation = HSBValues.getInt("sat");
                int brightness = HSBValues.getInt("bri");
                CustomColors customColor = new CustomColors();
                customColor.setAPIValues(hue, saturation, brightness);

                boolean isOn = HSBValues.getBoolean("on");

                HueLight hueLight = new HueLight(lightId, lightName, customColor, isOn, this);
                hueLights.add(hueLight);

                lightViewModel.addLight(new HueLight(lightId, lightName, customColor, isOn, this));

                Log.d("Light", lightName);

            } catch (JSONException e) {
                Log.e(LOGTAG, e.getLocalizedMessage());
            }

        }

        lightViewModel.getLightManager().setHueLights(hueLights);

        for (HueLight light : hueLights) {
            Log.i(LOGTAG, light.toString());
        }

    }

    private JsonRequest<JSONArray> setLightRequest(HueLight light, JSONObject requestData) {
        final String url = getFullAddress() + "/lights/" + light.getID() + "/state";
        Log.d(LOGTAG, "SetLightsUrl: " + url);

        return new CustomJsonArrayRequest(Request.Method.PUT, url,
                requestData, response -> {
            Log.i(LOGTAG, "Set Light Response: " + response.toString());
        }, error -> {
            Log.e(LOGTAG, error.getLocalizedMessage());
        });

    }

    //Queue getLinkRequest
    public void queueGetLink() {
        this.requestQueue.add(getLinkRequest());
    }

    //Queue getLightsRequest
    public void queueGetLights() {
        this.requestQueue.add(getLightsRequest());
    }

    //Queue setLightState
    public void queueSetLightState(HueLight light, boolean state) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("on", state);
        } catch (JSONException e) {
            Log.e(LOGTAG, e.getLocalizedMessage());
        }
        this.requestQueue.add(setLightRequest(light, jsonObject));
    }

    //Queue setLightColor
    public void queueSetLightColor(HueLight light, CustomColors customColor) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("hue", customColor.getAPIHue());
            jsonObject.put("sat", customColor.getAPISaturation());
            jsonObject.put("bri", customColor.getAPIBrightness());
        } catch (JSONException e) {
            Log.e(LOGTAG, e.getLocalizedMessage());
        }
        this.requestQueue.add(setLightRequest(light, jsonObject));
    }

}