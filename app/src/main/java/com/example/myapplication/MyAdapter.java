package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{


    Context context;
    List<RestaurantItem> items;

    public MyAdapter(Context context, List<RestaurantItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_restaurant_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nameView.setText(items.get(position).getName());
        holder.ratingView.setText(items.get(position).getRating());
        holder.imageView.setImageBitmap(items.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
