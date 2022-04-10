package com.leonv.hueapp;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class LightManager {

    private ArrayList<HueLight> hueLights = new ArrayList<>();

    public LightManager() {
    }

    public ArrayList<HueLight> getHueLights() {
        return this.hueLights;
    }

    public void setHueLights(ArrayList<HueLight> hueLights) {
        this.hueLights.clear();
        this.hueLights.addAll(hueLights);
    }

    public void addHueLight(HueLight hueLight) {
        if (!this.hueLights.contains(hueLight)) {
            this.hueLights.add(hueLight);
        }
    }

    public void clearHueLights() {
        this.hueLights = new ArrayList<>();
    }

}
