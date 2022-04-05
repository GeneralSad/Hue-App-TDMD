package com.leonv.hueapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HueApiManager {

    private static final String LOGTAG = HueApiManager.class.getName();

    private final Context context;
    private final RequestQueue requestQueue;
    private final LightViewModel lightViewModel;
    private String IP;

    public HueApiManager(Context appContext, LightViewModel lightViewModel) {
        this.context = appContext;
        this.requestQueue = Volley.newRequestQueue(this.context);
        this.lightViewModel = lightViewModel;
        this.IP = getIPAddress();
    }

    public boolean isLinked(){
        return isUserNameSet();
    }

    public void setLinked(boolean isLinkVisible) {
        this.lightViewModel.setIsLinked(isLinkVisible);
    }

    public void unLink(){
        forgetUserName();
    }

    //Queue getLinkRequest
    public void queueGetLink() {
        this.IP = getIPAddress();
        this.requestQueue.add(getLinkRequest());
    }

    //Queue getLightsRequest
    public void queueGetLights() {
        this.requestQueue.add(getLightsRequest());
    }

    public void queueGetGroups()
    {
        this.requestQueue.add(getGroupsRequest());
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
            jsonObject.put("hue", customColor.getHue() >= 65536 ? 65535 : customColor.getHue());
            jsonObject.put("sat", (customColor.getSaturation() >= 255 ? 254 : customColor.getSaturation()));
            jsonObject.put("bri", (customColor.getBrightness() >= 255 ? 254 : customColor.getBrightness()));
        } catch (JSONException e) {
            Log.e(LOGTAG, e.getLocalizedMessage());
        }
        this.requestQueue.add(setLightRequest(light, jsonObject));
    }

    //Queue setLightState
    public void queueSetGroupState(HueGroup group, boolean state) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("on", state);
        } catch (JSONException e) {
            Log.e(LOGTAG, e.getLocalizedMessage());
        }
        this.requestQueue.add(setGroupActionRequest(group, jsonObject));
    }

    //Queue setGroupActionRequest
    public void queueSetGroupColor(HueGroup group, CustomColors customColor) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("hue", customColor.getHue() >= 65536 ? 65535 : customColor.getHue());
            jsonObject.put("sat", (customColor.getSaturation() >= 255 ? 254 : customColor.getSaturation()));
            jsonObject.put("bri", (customColor.getBrightness() >= 255 ? 254 : customColor.getBrightness()));
        } catch (JSONException e) {
            Log.e(LOGTAG, e.getLocalizedMessage());
        }
        this.requestQueue.add(setGroupActionRequest(group, jsonObject));
    }

    private String getIPAddress()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPreferences.getString("IPAddress", "");
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

                JSONObject groups = response.getJSONObject("groups");
                createHueGroups(groups);

                this.lightViewModel.setIsLinked(true);
            } catch (JSONException e) {
                Log.d(LOGTAG, "getLightsRequest: " + e.getLocalizedMessage());
            }
        }, error -> {
            if (error.getLocalizedMessage() != null) {
                this.lightViewModel.setIsLinked(false);
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
        SharedPreferences sharedPref = this.context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", username);
        editor.apply();

        Log.i(LOGTAG, "Stored username: " + username);
    }

    //Get the username from SharedPreferences
    private String getUsername() {
        SharedPreferences sharedPref = this.context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        if (!sharedPref.contains("username")) {
            Log.w(LOGTAG, "Username SharedPreference was empty, try to link again");
        }

        return sharedPref.getString("username", "");
    }

    private void forgetUserName()
    {
        SharedPreferences sharedPref = this.context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        sharedPref.edit().remove("username").apply();
    }

    private boolean isUserNameSet()
    {
        SharedPreferences sharedPref = this.context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPref.contains("username");
    }

    //Get the address
    private String getStartAdress() {
        String adress = "http://" + this.IP + "/api/";
        return adress;
    }

    //Get the full address with username
    private String getFullAddress() {
        return getStartAdress() + getUsername();
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

        return new CustomJsonArrayRequest(Request.Method.PUT, url, requestData, response -> {
            Log.i(LOGTAG, "Set Light Response: " + response.toString());
        }, error -> {
            Log.e(LOGTAG, error.getLocalizedMessage());
        });

    }

    //Request the lights
    private JsonObjectRequest getGroupsRequest() {
        final String url = getFullAddress() + "/groups";
        Log.i(LOGTAG, "url: " + url);
        return new JsonObjectRequest(
                url, response -> {
            try {
                Log.i(LOGTAG, "getGroupsRequest: " + response.toString());

                createHueGroups(response);
                this.lightViewModel.setIsLinked(true);
            } catch (Exception e) {
                Log.d(LOGTAG, "getGroupsRequest: " + e.getLocalizedMessage());
            }
        }, error -> {
            if (error.getLocalizedMessage() != null) {
                this.lightViewModel.setIsLinked(false);
                Log.e(LOGTAG, error.getLocalizedMessage());
            } else {
                Log.e(LOGTAG, error.getMessage());
            }
        });
    }

    private JsonRequest<JSONArray> setGroupActionRequest(HueGroup group, JSONObject requestData) {
        final String url = getFullAddress() + "/groups/" + group.getId() + "/action";
        Log.d(LOGTAG, "SetGroupActionUrl: " + url);

        return new CustomJsonArrayRequest(Request.Method.PUT, url, requestData, response -> {
            Log.i(LOGTAG, "Set Group action Response: " + response.toString());
        }, error -> {
            Log.e(LOGTAG, error.getLocalizedMessage());
        });

    }

    private void createHueGroups(JSONObject groups) {

        ArrayList<HueGroup> hueGroups = new ArrayList<>();

        for (int i = 1; i <= groups.length(); i++) {

            try {

                String groupId = Integer.toString(i);

                JSONObject group = groups.getJSONObject(groupId);

                HueGroup hueGroup = new HueGroup();
                hueGroup.setId(groupId);

                String groupName = group.getString("name");
                hueGroup.setName(groupName);

                if(!group.isNull("action")) {
                    JSONObject HSBValues = group.getJSONObject("action");
                    int hue = HSBValues.getInt("hue");
                    int saturation = HSBValues.getInt("sat");
                    int brightness = HSBValues.getInt("bri");
                    CustomColors customColor = new CustomColors();
                    customColor.setAPIValues(hue, saturation, brightness);
                    hueGroup.setColor(customColor);

                    boolean isOn = HSBValues.getBoolean("on");
                    hueGroup.setOn(isOn);
                }
                hueGroups.add(hueGroup);

                if(!group.isNull("lights")){
                    JSONArray lights = group.getJSONArray("lights");
                    for(int j = 0; j < lights.length(); j++)
                    {
                        hueGroup.addHueLight(lights.getString(j));
                    }
                }

                Log.d("group", groupName);

            } catch (JSONException e) {
                Log.e(LOGTAG, e.getLocalizedMessage());
            }

        }

        lightViewModel.getGroupManager().setHueGroups(hueGroups);

        for (HueGroup group : hueGroups) {
            Log.i(LOGTAG, group.toString());
        }

    }

}
