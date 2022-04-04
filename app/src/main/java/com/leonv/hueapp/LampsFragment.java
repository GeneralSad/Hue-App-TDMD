package com.leonv.hueapp;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LampsFragment extends Fragment {

    private LightViewModel lightViewModel;

    public LampsFragment() {
    }

    public static LampsFragment newInstance() {
        return new LampsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.lightViewModel = new ViewModelProvider(requireActivity()).get(LightViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lamps_list, container, false);

        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            Context context = view.getContext();

            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            LightItemRecyclerViewAdapter lightItemRecyclerViewAdapter = new LightItemRecyclerViewAdapter(lightViewModel.getLightManager().getHueLights(), lightViewModel);
            recyclerView.setAdapter(lightItemRecyclerViewAdapter);

            this.lightViewModel.addUpdatedListener(lightItemRecyclerViewAdapter::notifyItemInserted);
        }

        return view;
    }


}