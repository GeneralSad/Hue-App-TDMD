package com.leonv.hueapp.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.leonv.hueapp.CustomColors;
import com.leonv.hueapp.LightViewModel;
import com.leonv.hueapp.R;

import top.defaults.colorpicker.ColorPickerPopup;

public class DetailFragment extends Fragment {

    private static final String LOGTAG = DetailFragment.class.getName();

    private LightViewModel lightViewModel;
    private View fragmentView;

    public DetailFragment() {
    }

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.fragmentView = inflater.inflate(R.layout.fragment_detail, container, false);

        this.lightViewModel = new ViewModelProvider(requireActivity()).get(LightViewModel.class);

        this.fragmentView.findViewById(R.id.colorPreview).setBackgroundColor(this.lightViewModel.getSelectedLight().getColor());

        TextView lampName = fragmentView.findViewById(R.id.detailLampName);
        lampName.setText(lightViewModel.getSelectedLight().getName());

        Button colorPickerButton = fragmentView.findViewById(R.id.detailPickColorButton);
        colorPickerButton.setOnClickListener(this::onPickColorPressed);

        Button lightToggleButton = fragmentView.findViewById(R.id.detailToggleLampButton);
        lightToggleButton.setText(lightViewModel.getSelectedLight().getState() ? R.string.TurnOff : R.string.TurnOn);
        lightToggleButton.setOnClickListener(this::onToggleLampPressed);

        return fragmentView;
    }

    public void onPickColorPressed(View view) {
        int color = this.lightViewModel.getSelectedLight().getColor();

        new ColorPickerPopup.Builder(getContext())
                .initialColor(color)
                .enableBrightness(true)
                .okTitle("Choose")
                .cancelTitle("Cancel")
                .showIndicator(true)
                .showValue(true)
                .build()
                .show(view, new ColorPickerPopup.ColorPickerObserver() {
                    @Override
                    public void
                    onColorPicked(int color) {
                        fragmentView.findViewById(R.id.colorPreview).setBackgroundColor(color);
                        lightViewModel.getSelectedLight().setColor(new CustomColors(color));
                        Log.i(LOGTAG, Integer.toHexString(color));
                        Log.i(LOGTAG, Integer.toBinaryString(color));
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
        this.lightViewModel.getSelectedLight().toggle();
    }

}