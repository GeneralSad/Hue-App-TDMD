package com.leonv.hueapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import top.defaults.colorpicker.ColorPickerPopup;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private LightViewModel lightViewModel;
    private HueApiManager hueApiManager;

    private int pickedColor = 0;

    private static final String LOGTAG = HueApiManager.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.hueApiManager = new HueApiManager(this);

        this.lightViewModel = new ViewModelProvider(this).get(LightViewModel.class);
        this.lightViewModel.getSelected().observe(this, this::pressedLight);

        this.fragmentManager = getSupportFragmentManager();
    }

    public void pressedLight(HueLight hueLight) {
        Log.i(LOGTAG, "Pressed " + hueLight.getName());
        FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFragment, DetailFragment.class, null, "detailFragment");
        fragmentTransaction.addToBackStack("detailFragment");
        fragmentTransaction.commit();
    }

    public void linkButtonPressed(View view) {
        Log.i(LOGTAG, "Linking");
        this.hueApiManager.queueGetLink();
        FragmentTransaction transaction = this.fragmentManager.beginTransaction();
        transaction.replace(R.id.mainFragment, LampsFragment.class, null, "lampsFragment");
        transaction.commit();

        this.lightViewModel.getIsLinkVisible().observe(this, this::setLinkVisible);

    }

    public void setLinkVisible(boolean isLinkVisible) {
        Log.i(LOGTAG, "Link: " + isLinkVisible);
        Button button = findViewById(R.id.linkButton);
        if (isLinkVisible) {
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.GONE);
        }
    }

    public void onPickColorPressed(View view) {
        new ColorPickerPopup.Builder(getApplicationContext())
                .initialColor(Color.RED)
                .enableBrightness(true)
                .enableAlpha(true)
                .okTitle("Choose")
                .cancelTitle("Cancel")
                .showIndicator(true)
                .showValue(true)
                .build()
                .show(view, new ColorPickerPopup.ColorPickerObserver() {
                    @Override
                    public void
                    onColorPicked(int color) {
                        pickedColor = color;
//                        view.findViewById(R.id.colorPreview).setBackgroundColor(color);
//                        view.findViewById(R.id.colorPickerView).setBackgroundColor(pickedColor);
                        lightViewModel.getSelectedLight().setColor(new CustomColors(color));
                        Log.i(LOGTAG, Integer.toString(color));
                    }
                });
    }

    public void onToggleLampPressed(View view) {
        Button toggleButton = view.findViewById(R.id.detailToggleLampButton);
        if (this.lightViewModel.getSelectedLight().getState()) {
            toggleButton.setText(R.string.TurnOn);
        } else {
            toggleButton.setText(R.string.TurnOff);
        }

    }

}