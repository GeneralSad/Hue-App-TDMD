package com.leonv.hueapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

interface ItemAddedListener {
    void onLightsUpdated(int index);
}

interface ListClearedListener {
    void onLightsCleared();
}

public class LightViewModel extends ViewModel implements OnItemClickListener {

    private static final String LOGTAG = LightViewModel.class.getName();

    private MutableLiveData<HueLight> selectedLight = new MutableLiveData<>();
    private List<ItemAddedListener> listeners = new LinkedList<>();
    private ListClearedListener listClearedListener;
    private LightManager lightManager = new LightManager();
    private MutableLiveData<Boolean> isLinkVisible = new MutableLiveData<>();

    public void setListClearedListener(ListClearedListener listClearedListener) {
        this.listClearedListener = listClearedListener;
    }

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

    public MutableLiveData<Boolean> getIsLinkVisible() {
        return this.isLinkVisible;
    }

    public void setIsLinkVisible(boolean isLinkVisible) {
        this.isLinkVisible.postValue(isLinkVisible);
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

    public LightManager getLightManager() {
        return lightManager;
    }

    @Override
    public void onItemClick(int index) {
        this.selectedLight.setValue(lightManager.getHueLights().get(index));
    }
}
