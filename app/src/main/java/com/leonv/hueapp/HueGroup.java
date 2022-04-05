package com.leonv.hueapp;

public class HueGroup {

    private String id;
    private String name;
    private CustomColors color;
    private boolean isOn;

    public HueGroup(String id, String name, CustomColors color, boolean isOn) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.isOn = isOn;
    }

    public HueGroup() {
    }

    public HueGroup(String id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "HueGroup{" +
                "name='" + name + '\'' +
                '}';
    }
}
