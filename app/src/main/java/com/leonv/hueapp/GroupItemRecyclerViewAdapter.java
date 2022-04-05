package com.leonv.hueapp;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class GroupItemRecyclerViewAdapter extends RecyclerView.Adapter<GroupItemRecyclerViewAdapter.ViewHolder> {

    private static final String LOGTAG = GroupItemRecyclerViewAdapter.class.getName();

    private final List<HueGroup> hueGroups;
    private OnItemClickListener onItemClickListener;

    public GroupItemRecyclerViewAdapter(ArrayList<HueGroup> items, OnItemClickListener onItemClickListener) {
        this.hueGroups = items;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.groups_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.hueGroupName.setText(hueGroups.get(position).getName());
        holder.hueGroup = hueGroups.get(position);
    }

    @Override
    public int getItemCount() {
        return hueGroups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView hueGroupName;
        public HueGroup hueGroup;

        public ViewHolder(View view) {
            super(view);
            hueGroupName = view.findViewById(R.id.group_name_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(getBindingAdapterPosition());
        }
    }
}