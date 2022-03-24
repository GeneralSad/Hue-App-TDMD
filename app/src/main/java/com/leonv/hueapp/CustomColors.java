package com.leonv.hueapp;

import android.util.Log;

import java.util.HashMap;

public class CustomColors {

    private int hue;
    private int saturation;
    private int brightness;

    private static final String LOGTAG = CustomColors.class.getName();

    public CustomColors() {
    }

    public CustomColors(int hue, int saturation, int brightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
    }

    public CustomColors(HashMap<String, Integer> HSBValues) {
        this.hue = HSBValues.get("hue");
        this.saturation = HSBValues.get("saturation");
        this.brightness = HSBValues.get("brightness");
    }

    public CustomColors(int hexColor) {
        String colorStr = Integer.toHexString(hexColor);
        this.hue = Math.abs(~Integer.valueOf(colorStr.substring( 4, 8 ), 16)) - 1;
        this.saturation = Integer.valueOf(colorStr.substring( 0, 2 ), 16);
        this.brightness = Integer.valueOf(colorStr.substring( 2, 4 ), 16);
        Log.i(LOGTAG, colorStr);
        Log.i(LOGTAG, this.hue + ", " + this.saturation + ", " + this.brightness);
    }

    public void setAPIValues(int hue, int saturation, int brightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
    }

    public void setAPIValues(int hexColor) {
        String colorStr = Integer.toString(hexColor);
        this.hue = Integer.valueOf(colorStr.substring( 1, 3 ), 16);
        this.saturation = Integer.valueOf(colorStr.substring( 3, 5 ), 16);
        this.brightness = Integer.valueOf(colorStr.substring( 5, 7 ), 16);
    }

    public HashMap<String, Float> getAPIValues() {
        HashMap<String, Float> APIValues = new HashMap<>();
        APIValues.put("hue", this.hue / 365f * 65535f);
        APIValues.put("saturation", this.saturation * 254f);
        APIValues.put("brightness", this.brightness * 254f);

        return APIValues;
    }

    //Set the HSB values with a hashmap
    public void setValuesHSB(HashMap<String, Integer> HSBValues) {
        this.hue = HSBValues.get("hue");
        this.saturation = HSBValues.get("saturation");
        this.brightness = HSBValues.get("brightness");
    }

    //Get the HSB values in an hashmap
    public HashMap<String, Integer> getValuesHSBHashMap() {
        HashMap<String, Integer> HSBValues = new HashMap<>();
        HSBValues.put("hue", this.hue);
        HSBValues.put("saturation", this.saturation);
        HSBValues.put("brightness", this.brightness);

        return HSBValues;
    }

    public float getHue() {
        return hue;
    }

    public float getAPIHue() {
        return this.hue;
    }

    public void setHue(int hue) {
        this.hue = hue;
    }

    public float getSaturation() {
        return saturation;
    }

    public float getAPISaturation() {
        return this.saturation;
    }

    public void setSaturation(int saturation) {
        this.saturation = saturation;
    }

    public float getBrightness() {
        return brightness;
    }

    public float getAPIBrightness() {
        return this.brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }
}
