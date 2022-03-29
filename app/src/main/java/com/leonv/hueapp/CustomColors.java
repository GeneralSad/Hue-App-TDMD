package com.leonv.hueapp;

import android.graphics.Color;
import android.util.Log;

import java.util.HashMap;

public class CustomColors {

    private int hue;
    private int saturation;
    private int brightness;

    private static final String LOGTAG = CustomColors.class.getName();

    public CustomColors() {
    }

    public CustomColors(int hexColor) {
        float[] hsv = new float[3];
        Color.colorToHSV(hexColor, hsv);
        this.hue = (int)(hsv[0] / 365 * 65535);
        this.saturation = (int)(hsv[1] * 255);
        this.brightness = (int)(hsv[2] * 255);
        Log.i(LOGTAG, this.hue + ", " + this.saturation + ", " + this.brightness);
    }

    public void setAPIValues(int hue, int saturation, int brightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
    }

    public int getHexValue() {
        float[] hsv= new float[3];
        hsv[0] = (this.hue / 65535f) * 365f;
        hsv[1] = this.saturation / 254f;
        hsv[2] = this.brightness / 254f;
        return Color.HSVToColor(hsv);
    }

    public float getHue() {
        return hue;
    }

    public void setHue(int hue) {
        this.hue = hue;
    }

    public float getSaturation() {
        return saturation;
    }

    public void setSaturation(int saturation) {
        this.saturation = saturation;
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

}
