package com.leonv.hueapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.leonv.hueapp.fragments.DetailFragment;
import com.leonv.hueapp.fragments.GroupDetailFragment;
import com.leonv.hueapp.fragments.GroupsFragment;
import com.leonv.hueapp.fragments.LampsFragment;
import com.leonv.hueapp.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private LightViewModel lightViewModel;
    private HueApiManager hueApiManager;

    private static final String LOGTAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.lightViewModel = new ViewModelProvider(this).get(LightViewModel.class);
        this.lightViewModel.getSelected().observe(this, this::pressedLight);
        this.lightViewModel.getSelectedGroupLiveData().observe(this, this::pressedGroup);

        this.hueApiManager = new HueApiManager(this, this.lightViewModel);

        this.fragmentManager = getSupportFragmentManager();

        setLinkButton(this.hueApiManager.isLinked());

        if (this.hueApiManager.isLinked()) {
            this.hueApiManager.queueGetLights();
        }

        FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFragment, LampsFragment.class, null , "lampsFragment");
        fragmentTransaction.commit();

    }

    public void pressedLight(HueLight hueLight) {
        Log.i(LOGTAG, "Pressed " + hueLight.getName());
        FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFragment, DetailFragment.class, null , "detailFragment");
        fragmentTransaction.addToBackStack("detailFragment");
        fragmentTransaction.commit();
    }

    public void pressedGroup(HueGroup hueGroup) {
        Log.i(LOGTAG, "Pressed " + hueGroup.getName());
        FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFragment, GroupDetailFragment.class, null , "detailFragment");
        fragmentTransaction.addToBackStack("detailFragment");
        fragmentTransaction.commit();
    }

    public void linkButtonPressed(View view) {

        FragmentTransaction transaction = this.fragmentManager.beginTransaction();

        if (this.hueApiManager.isLinked()) {
            Log.i(LOGTAG, "Unlinking");
            this.hueApiManager.unLink();
            this.lightViewModel.clearLights();
            setLinkButton(false);
            transaction.replace(R.id.mainFragment, LampsFragment.class, null, "lampsFragment");
            transaction.commit();

        } else {
            Log.i(LOGTAG, "Linking");
            this.hueApiManager.queueGetLink();
            transaction.replace(R.id.mainFragment, LampsFragment.class, null, "lampsFragment");
            transaction.commit();

            this.lightViewModel.getIsLinked().observe(this, this::setLinkButton);
        }

    }

    public void setLinkButton(boolean isLinked) {
        Button button = findViewById(R.id.linkButton);
        if (isLinked) {
            button.setText(R.string.unlinkText);
        } else {
            button.setText(R.string.linkText);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {

            FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.mainFragment, SettingsFragment.class, null , "settingsFragment");
            fragmentTransaction.addToBackStack("settingsFragment");
            fragmentTransaction.commit();

            return true;
        }
        else if (item.getItemId() == R.id.action_groups) {

            FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.mainFragment, GroupsFragment.class, null , "groupsFragment");
            fragmentTransaction.addToBackStack("groupsFragment");
            fragmentTransaction.commit();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}