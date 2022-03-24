package com.leonv.hueapp;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class LightManager {

    private ArrayList<HueLight> hueLights = new ArrayList<>();

    public LightManager() {
    }

//    public void connectToHub() {
//        hueApiManager.queueGetLink();
//    }

    public ArrayList<HueLight> getHueLights() {
        return this.hueLights;
    }

    public void setHueLights(ArrayList<HueLight> hueLights) {
        this.hueLights = hueLights;
    }

    public void addHueLight(HueLight hueLight) {
        if (!this.hueLights.contains(hueLight)) {
            this.hueLights.add(hueLight);
        }
    }

//    public void toggleLight(HueLight light) {
//        hueApiManager.queueSetLightState(light, !light.getState());
//        light.setState(!light.getState());
//    }

//    public void toggleLights() {
//        for (HueLight hueLight : this.hueLights) {
//            hueApiManager.queueSetLightState(hueLight, !hueLight.getState());
//            hueLight.setState(!hueLight.getState());
//        }
//    }

//    public void setLightState(HueLight light, boolean state) {
//        hueApiManager.queueSetLightState(light, state);
//        light.setState(state);
//    }

//    public void setLightsState(boolean state) {
//        for (HueLight hueLight : this.hueLights) {
//            hueApiManager.queueSetLightState(hueLight, state);
//            hueLight.setState(state);
//        }
//    }

//    public void setLightColor(HueLight light, float hue, float saturation, float brightness) {
//        setLightState(light, true);
//        hueApiManager.queueSetLightColor(light, new CustomColors(hue, saturation, brightness));
//        light.setColor(hue, saturation, brightness);
//    }

//    public void setLightsColor(float hue, float saturation, float brightness) {
//        setLightsState(true);
//        for (HueLight hueLight : this.hueLights) {
//            hueApiManager.queueSetLightColor(hueLight, new CustomColors(hue, saturation, brightness));
//            hueLight.setColor(hue, saturation, brightness);
//        }
//    }

}
