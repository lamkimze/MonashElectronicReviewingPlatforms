package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends BaseAdapter {
    private final Context context;
    private final List<Restaurant> restaurants;

    public ListAdapter(Context context, List<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    @Override
    public int getCount() {
        return restaurants.size();
    }

    @Override
    public Object getItem(int position) {
        return restaurants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the convertView is null, which means we need to create a new view
        if (convertView == null) {
            // Inflate a new view from the list_item layout resource
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        // Get the Restaurant object at the specified position in the list
        Restaurant restaurant = restaurants.get(position);

//        Get the restaurant icon
        ImageView iconImageView = convertView.findViewById(R.id.restaurantIcon);
        iconImageView.setImageResource(restaurant.getImageResource());

        // Display the restaurant name in the TextView with the ID restaurantName
        TextView nameTextView = convertView.findViewById(R.id.restaurantName);
        nameTextView.setText(restaurant.getName());

        // Display the restaurant location in the TextView with the ID restaurantLocation
        TextView locationTextView = convertView.findViewById(R.id.restaurantLocation);
        locationTextView.setText(restaurant.getLocation());


        return convertView;
    }
}
