package com.example.myapplication;

import java.util.ArrayList;

public class restaurantRegistered {
    ArrayList<Restaurant> restaurants;

    public restaurantRegistered(ArrayList<Restaurant> restaurants){
        this.restaurants = restaurants;
    }

    public void add(Restaurant newRestaurant){
        restaurants.add(newRestaurant);
    }

    public Restaurant searchRestaurant(String restaurantName){
        for (int i = 0; i < restaurants.size(); i++) {
            if(restaurants.get(i).getName().equals(restaurantName)){
                return restaurants.get(i);
            }
        }
        return null;
    }
}
