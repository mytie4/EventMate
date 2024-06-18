package com.example.assignment1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventRecyclerAdapter extends  RecyclerView.Adapter<EventRecyclerAdapter.CustomViewHolder>{

    ArrayList<Event> data = new ArrayList<Event>();

    public void setData(ArrayList<Event> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card_layout,parent,false);
        CustomViewHolder viewHolder = new CustomViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.tvEventID.setText(data.get(position).getEventID());
        holder.tvCatID.setText(data.get(position).getCatID());
        holder.tvEventName.setText(data.get(position).getName());
        holder.tvTickets.setText(String.valueOf(data.get(position).getTicket()));

        if (data.get(position).isActive()){
            holder.tvIsActive.setText("Active");
        }else{
            holder.tvIsActive.setText("Inactive");
        }

        // Set the click listener to launch Google Search Activity
        holder.itemView.setOnClickListener(v -> {

            String selectedName = data.get(position).getName();

            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, EventGoogleResult.class);
            intent.putExtra("eventName", selectedName);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (this.data != null) { // if data is not null
            return this.data.size(); // then return the size of ArrayList
        }

        // else return zero if data is null
        return 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView tvEventID;
        public TextView tvEventName;
        public TextView tvCatID;
        public TextView tvTickets;
        public TextView tvIsActive;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEventID = itemView.findViewById(R.id.tv_eID);
            tvEventName = itemView.findViewById(R.id.tv_catname);
            tvCatID = itemView.findViewById(R.id.tv_catID);
            tvTickets = itemView.findViewById(R.id.tv_ticket);
            tvIsActive = itemView.findViewById(R.id.tv_active);
        }
    }
}
