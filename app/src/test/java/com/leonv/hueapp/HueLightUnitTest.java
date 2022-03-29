package com.leonv.hueapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class HueLightUnitTest {

    final static String id = "0";
    final static String name = "HueLight";
    final static boolean state = true;
    final static CustomColors customColor = new CustomColors() {
        @Override
        public int getHexValue() {
            return 0xffffff;
        }
    };

    HueLight hueLight;

    @Before
    public void onTestStart()
    {
        hueLight = new HueLight(id, name, customColor, state, mock(HueApiManager.class));
    }

    @Test
    public void getIdHueLightsTest() {
        assertEquals(id, hueLight.getID());
    }

    @Test
    public void getNameHueLightsTest() {
        assertEquals(name, hueLight.getName());
    }

    @Test
    public void getStateHueLightsTest() {
        assertTrue(hueLight.getState());
    }

    @Test
    public void ToggleHueLightsTest() {
        assertTrue(hueLight.getState());
        hueLight.toggle();
        assertFalse(hueLight.getState());
    }

    @Test
    public void getColorHueLightsTest() {
        assertEquals(customColor.getHexValue(), hueLight.getColor());
    }

    @Test
    public void setColorHueLightsTest() {

        CustomColors newCustomColor = new CustomColors() {
            @Override
            public int getHexValue() {
                return 0xaaaaaa;
            }
        };

        hueLight.setColor(newCustomColor);
        assertEquals(hueLight.getColor(), newCustomColor.getHexValue());
    }

    @Test
    public void toStringHueLightsTest() {
        System.out.println(customColor.toString());
        String str = "0: {Name: HueLight\n" +
                "HSB: 0.0, 0.0, 0.0\n" +
                "State: true}";
        assertEquals(str, hueLight.toString());
    }

}
