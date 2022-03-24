package com.leonv.hueapp;

import java.util.HashMap;

public class HueLight {

    private String id;
    private String name;
    CustomColors color;
    private boolean isOn;

    private HueApiManager hueApiManager;

    public HueLight(String id, String name, CustomColors color, boolean isOn, HueApiManager hueApiManager) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.isOn = isOn;
        this.hueApiManager = hueApiManager;
    }

    String getID() {
        return this.id;
    }

    String getName() {
        return this.name;
    }

    boolean getState() {
        return isOn;
    }

    void setState(boolean isOn) {
        this.hueApiManager.queueSetLightState(this, isOn);
        this.isOn = isOn;
    }

    void toggle() {
        this.isOn = !this.isOn;
        this.hueApiManager.queueSetLightState(this, this.isOn);
    }

    HashMap<String, Integer> getColor() {
        return this.color.getValuesHSBHashMap();
    }

    void setColor(int hue, int saturation, int brightness) {
        this.color = new CustomColors(hue, saturation, brightness);
        this.hueApiManager.queueSetLightColor(this, this.color);
    }

    void setColor(CustomColors color) {
        this.color = color;
        this.hueApiManager.queueSetLightColor(this, this.color);
    }

    @Override
    public String toString() {
        return this.id + ": {Name: " + this.name +
                "\nHSB: " + this.color.getHue() + ", " + this.color.getSaturation() + ", " + this.color.getBrightness() +
                "\nState: " + this.isOn + "}";
    }

}
