package com.leonv.hueapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import java.util.ArrayList;

public class LightManagerUnitTest {

    @Test
    public void addHueLightsTest() {

        HueLight hueLight = mock(HueLight.class);

        LightManager lightManager = new LightManager();
        lightManager.addHueLight(hueLight);
        lightManager.addHueLight(hueLight);

        assertEquals(lightManager.getHueLights().size(), 1);

    }


    @Test
    public void setHueLightsTest() {

        HueLight hueLight = new HueLight("0", "Light", mock(CustomColors.class), true, mock(HueApiManager.class));
        HueLight hueLight1 = new HueLight("1", "Light1", mock(CustomColors.class), true, mock(HueApiManager.class));

        ArrayList<HueLight> hueLights = new ArrayList<>();
        hueLights.add(hueLight);
        hueLights.add(hueLight1);

        LightManager lightManager = new LightManager();
        lightManager.setHueLights(hueLights);

        assertEquals(lightManager.getHueLights(), hueLights);

    }

    @Test
    public void clearHueLightsTest() {

        HueLight hueLight  = new HueLight("0", "Light", mock(CustomColors.class), true, mock(HueApiManager.class));
        HueLight hueLight1  = new HueLight("1", "Light1", mock(CustomColors.class), true, mock(HueApiManager.class));

        ArrayList<HueLight> hueLights = new ArrayList<>();
        hueLights.add(hueLight);
        hueLights.add(hueLight1);

        LightManager lightManager = new LightManager();
        lightManager.clearHueLights();

        assertNotEquals(lightManager.getHueLights(), hueLights);

    }




}
