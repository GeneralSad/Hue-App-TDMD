package com.leonv.hueapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private SharedPreferences sharedPreferences;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = this.requireActivity();
        sharedPreferences = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        String IPAddress = sharedPreferences.getString("IPAddress", "");

        EditText settingsInputView = view.findViewById(R.id.inputSettingsIpAddress);
        settingsInputView.setText(IPAddress);

        Button settingsSaveButton = view.findViewById(R.id.buttonSaveSettings);
        settingsSaveButton.setOnClickListener(this::onSaveButtonPressed);

        return view;
    }

    public void onSaveButtonPressed(View view)
    {
        EditText settingsInputView = this.requireActivity().findViewById(R.id.inputSettingsIpAddress);
        String IPAddress = settingsInputView.getText().toString();

        SharedPreferences.Editor sharedPreferencesEditor =  sharedPreferences.edit();
        sharedPreferencesEditor.putString("IPAddress", IPAddress);
        sharedPreferencesEditor.apply();

        FragmentManager fragmentManager = this.requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFragment, LampsFragment.class, null , "lampsFragment");
        fragmentTransaction.commit();
    }
}