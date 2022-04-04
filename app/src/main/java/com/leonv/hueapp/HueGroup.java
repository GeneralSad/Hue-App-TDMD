package com.leonv.hueapp;

public class HueGroup {
    private String name;
    private CustomColors color;
    private boolean isOn;

    public HueGroup(String name, CustomColors color, boolean isOn) {
        this.name = name;
        this.color = color;
        this.isOn = isOn;
    }

    public HueGroup() {
    }

    public HueGroup(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "HueGroup{" +
                "name='" + name + '\'' +
                '}';
    }
}
