package com.example.eventmate;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CustomViewHolder>{
    ArrayList<Category> data = new ArrayList<Category>();
    public void setData(ArrayList<Category> data) {
        this.data = data;
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card_layout,parent,false);
        CustomViewHolder viewHolder = new CustomViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.tvCatID.setText(data.get(position).getCatID());
        holder.tvCatName.setText(data.get(position).getName());
        holder.tvEventCount.setText(String.valueOf(data.get(position).getEventCount()));

        if (data.get(position).isActive()){
            holder.tvIsActive.setText("Active");
        } else{
            holder.tvIsActive.setText("Inactive");
        }

        // Set the click listener to launch Google Maps Activity
        holder.itemView.setOnClickListener(v -> {

            String selectedLocation = data.get(position).getEventLocation();
            String selectedName = data.get(position).getName();

            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, GoogleMapActivity.class);
            intent.putExtra("eventLocation", selectedLocation);
            intent.putExtra("categoryName", selectedName);
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

        public TextView tvCatID;
        public TextView tvCatName;
        public TextView tvEventCount;

        public TextView tvIsActive;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCatID = itemView.findViewById(R.id.tv_catid);
            tvCatName = itemView.findViewById(R.id.tv_catname);
            tvEventCount = itemView.findViewById(R.id.tv_ecount);
            tvIsActive = itemView.findViewById(R.id.tv_active);
        }
    }
}
