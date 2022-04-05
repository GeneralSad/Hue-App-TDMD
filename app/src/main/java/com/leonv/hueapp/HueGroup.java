package com.leonv.hueapp;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HueGroup {

    private String id;
    private String name;
    private CustomColors color;
    private boolean isOn;
    private ArrayList<String> hueLights;

    public HueGroup(String id, String name, CustomColors color, boolean isOn, ArrayList<String> hueLights) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.isOn = isOn;
        this.hueLights = hueLights;
    }

    public HueGroup() {
        this.hueLights = new ArrayList<>();
    }

    public HueGroup(String id, String name) {
        this.id = id;
        this.name = name;
        this.hueLights = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CustomColors getColor() {
        return color;
    }

    public void setColor(CustomColors color) {
        this.color = color;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public void addHueLight(String lightId)
    {
        if(this.hueLights.contains(lightId)) {
            return;
        }
        this.hueLights.add(lightId);
    }

    public void setHueLight(ArrayList<String> hueLightIds)
    {
        this.hueLights.clear();
        this.hueLights.addAll(hueLightIds);
    }

    public ArrayList<String> getHueLights() {
        return hueLights;
    }

    @Override
    public String toString() {
        return "HueGroup{" +
                "name='" + name + '\'' +
                '}';
    }
}
