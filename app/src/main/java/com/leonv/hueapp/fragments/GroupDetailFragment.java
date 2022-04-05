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
import com.leonv.hueapp.HueApiManager;
import com.leonv.hueapp.LightViewModel;
import com.leonv.hueapp.R;

import top.defaults.colorpicker.ColorPickerPopup;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class GroupDetailFragment extends Fragment {

    private static final String LOGTAG = GroupDetailFragment.class.getName();

    private LightViewModel lightViewModel;
    private View fragmentView;
    private HueApiManager hueApiManager;

    public GroupDetailFragment() {
    }

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.lightViewModel = new ViewModelProvider(requireActivity()).get(LightViewModel.class);
        this.hueApiManager = new HueApiManager(this.requireContext(), this.lightViewModel);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.fragmentView = inflater.inflate(R.layout.fragment_group_detail, container, false);

        CustomColors color = this.lightViewModel.getSelectedGroup().getColor();
        if(color != null) {
            this.fragmentView.findViewById(R.id.groupColorPreview).setBackgroundColor(color.getHexValue());
        }

        TextView groupName = fragmentView.findViewById(R.id.detailGroupName);
        groupName.setText(lightViewModel.getSelectedGroup().getName());

        Button colorPickerButton = fragmentView.findViewById(R.id.detailPickGroupColorButton);
        colorPickerButton.setOnClickListener(this::onPickColorPressed);

        Button groupToggleButton = fragmentView.findViewById(R.id.detailToggleGroupButton);
        groupToggleButton.setText(lightViewModel.getSelectedGroup().isOn() ? R.string.TurnOff : R.string.TurnOn);
        groupToggleButton.setOnClickListener(this::onToggleGroupPressed);

        return fragmentView;
    }

    public void onPickColorPressed(View view) {
        CustomColors color = this.lightViewModel.getSelectedGroup().getColor();

        new ColorPickerPopup.Builder(getContext())
                .initialColor(color.getHexValue())
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
                        fragmentView.findViewById(R.id.groupColorPreview).setBackgroundColor(color);
                        CustomColors customColors = new CustomColors(color);
                        lightViewModel.getSelectedGroup().setColor(customColors);
                        lightViewModel.getLightManager().getHueLights()
                                .stream()
                                .filter(x -> lightViewModel.getSelectedGroup().getHueLights().contains(x.getID()))
                                .forEach(x -> x.setColor(customColors));

                        hueApiManager.queueSetGroupColor(lightViewModel.getSelectedGroup(), customColors);

                        Log.i(LOGTAG, Integer.toHexString(color));
                        Log.i(LOGTAG, Integer.toBinaryString(color));
                    }
                });
    }

    public void onToggleGroupPressed(View view) {
        Button toggleButton = view.findViewById(R.id.detailToggleGroupButton);
        boolean isOn = this.lightViewModel.getSelectedGroup().isOn();
        if (isOn) {
            toggleButton.setText(R.string.TurnOn);
        } else {
            toggleButton.setText(R.string.TurnOff);
        }
        this.lightViewModel.getSelectedGroup().setOn(!isOn);
        this.hueApiManager.queueSetGroupState(this.lightViewModel.getSelectedGroup(), !isOn);
        lightViewModel.getLightManager().getHueLights()
                .stream()
                .filter(x -> lightViewModel.getSelectedGroup().getHueLights().contains(x.getID()))
                .forEach(x -> x.setState(!isOn));
    }
}