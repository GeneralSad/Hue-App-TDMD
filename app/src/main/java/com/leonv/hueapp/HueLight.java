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

    public String getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public boolean getState() {
        return isOn;
    }

    public void setState(boolean state){
        this.isOn = state;
    }

    public void toggle() {
        this.isOn = !this.isOn;
        this.hueApiManager.queueSetLightState(this, this.isOn);
    }

    public int getColor() {
        return this.color.getHexValue();
    }

    public void setColor(CustomColors color) {
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
