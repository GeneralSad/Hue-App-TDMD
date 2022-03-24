package com.leonv.hueapp;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> {

    private static final String LOGTAG = HueApiManager.class.getName();

    private final List<HueLight> hueLights;
    private OnItemClickListener onItemClickListener;

    public ItemRecyclerViewAdapter(ArrayList<HueLight> items, OnItemClickListener onItemClickListener) {
        this.hueLights = items;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_lamps, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.lightName.setText(hueLights.get(position).getName());
        holder.hueLight = hueLights.get(position);
    }

    @Override
    public int getItemCount() {
        return hueLights.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView lightName;
        public HueLight hueLight;

        public ViewHolder(View view) {
            super(view);
            lightName = view.findViewById(R.id.light_name_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(getBindingAdapterPosition());
        }
    }
}