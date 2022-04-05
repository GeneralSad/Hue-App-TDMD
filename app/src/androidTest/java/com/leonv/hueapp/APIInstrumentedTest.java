package com.leonv.hueapp;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class APIInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void apigetLightsTest() {
        LightViewModel lightViewModel = new ViewModelProvider(mainActivity.getActivity()).get(LightViewModel.class);
        HueApiManager hueApiManager = new HueApiManager(mainActivity.getActivity(), lightViewModel);
        hueApiManager.queueGetLink();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse(lightViewModel.getLightManager().getHueLights().isEmpty());
    }
}