package com.example.myapplication;

import androidx.paging.PagingData;
import com.example.myapplication.dataType.RestaurantFilterModel;
import com.example.myapplication.Restaurant;
import org.reactivestreams.Publisher;

public interface RestaurantRepository {

    Publisher<PagingData<Restaurant>> getRestaurants(RestaurantFilterModel restaurantFilterModel);
}
