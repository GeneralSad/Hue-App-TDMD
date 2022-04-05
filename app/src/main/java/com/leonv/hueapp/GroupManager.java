package com.leonv.hueapp;

import java.util.ArrayList;

public class GroupManager {

    private final ArrayList<HueGroup> hueGroups = new ArrayList<>();

    public GroupManager() {
    }

    public ArrayList<HueGroup> getHueGroups() {
        return this.hueGroups;
    }

    public void setHueGroups(ArrayList<HueGroup> hueGroups) {
        this.hueGroups.clear();
        this.hueGroups.addAll(hueGroups);
    }

    public void addHueGroup(HueGroup hueGroup) {
        if (!this.hueGroups.contains(hueGroup)) {
            this.hueGroups.add(hueGroup);
        }
    }

    public void clearHueGroups() {
        this.hueGroups.clear();
    }
}
