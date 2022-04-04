package com.leonv.hueapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GroupsFragment extends Fragment {

    private LightViewModel lightViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.lightViewModel = new ViewModelProvider(requireActivity()).get(LightViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);

        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            Context context = view.getContext();

            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            GroupItemRecyclerViewAdapter groupItemRecyclerViewAdapter = new GroupItemRecyclerViewAdapter(lightViewModel.getGroupManager().getHueGroups(), lightViewModel);
            recyclerView.setAdapter(groupItemRecyclerViewAdapter);

//            this.lightViewModel.addUpdatedListener(groupItemRecyclerViewAdapter::notifyItemInserted);
        }

        return view;
    }
}
