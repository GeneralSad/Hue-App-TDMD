package com.leonv.hueapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.LinkedList;
import java.util.List;

interface ItemAddedListener {
    void onLightsUpdated(int index);
}

public class LightViewModel extends ViewModel implements OnItemClickListener {

    private static final String LOGTAG = LightViewModel.class.getName();

    private MutableLiveData<HueLight> selectedLight = new MutableLiveData<>();
    private List<ItemAddedListener> listeners = new LinkedList<>();
    private LightManager lightManager = new LightManager();
    private GroupManager groupManager = new GroupManager();
    private MutableLiveData<Boolean> isLinked = new MutableLiveData<>();

    private void notifyUpdatedListeners(int index) {
        for (ItemAddedListener itemAddedListener : listeners) {
            itemAddedListener.onLightsUpdated(index);
        }
    }

    public boolean addUpdatedListener(ItemAddedListener itemAddedListener) {
        if (!this.listeners.contains(itemAddedListener)) {
            this.listeners.add(itemAddedListener);
            return true;
        }
        return false;
    }

    public MutableLiveData<Boolean> getIsLinked() {
        return this.isLinked;
    }

    public void setIsLinked(boolean isLinked) {
        this.isLinked.postValue(isLinked);
    }

    public LiveData<HueLight> getSelected() {
        return this.selectedLight;
    }

    public HueLight getSelectedLight() {
        return this.selectedLight.getValue();
    }

    public void addLight(HueLight hueLight) {
        lightManager.addHueLight(hueLight);
        notifyUpdatedListeners(lightManager.getHueLights().size());
    }

    public void clearLights() {
        lightManager.clearHueLights();
    }

    public LightManager getLightManager() {
        return lightManager;
    }

    public GroupManager getGroupManager()
    {
        return groupManager;
    }

    @Override
    public void onItemClick(int index) {
        this.selectedLight.setValue(lightManager.getHueLights().get(index));
    }
}
