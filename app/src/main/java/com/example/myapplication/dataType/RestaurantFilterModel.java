package com.example.myapplication.dataType;

import com.example.myapplication.Restaurant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RestaurantFilterModel {

    private final String searchQuery;
    private final SortType sortType;
    private final Set<CuisineType> cuisineTypes;

    public RestaurantFilterModel() {
        this.searchQuery = null;
        this.sortType = SortType.NONE;
        this.cuisineTypes = new HashSet<>();
    }

    public RestaurantFilterModel(String searchQuery, SortType sortType, Set<CuisineType> cuisineTypes) {
        this.searchQuery = searchQuery;
        this.sortType = sortType;
        this.cuisineTypes = cuisineTypes;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public SortType getSortType() {
        return sortType;
    }

    public Set<CuisineType> getCuisineTypes() {
        return cuisineTypes;
    }

    public enum SortType {
        NONE,
        ALPHABETICAL_ASC,
        MEDALS,
        RATING_ASC
    }

    public enum CuisineType {
        CHINESE("Chinese"),
        AMERICAN("American"),
        INDIAN("Indian"),
        MALAYSIAN("Malaysian"),
        VIETNAMESE("Vietnamese"),
        JAPANESE("Japanese"),
        KOREAN("Korean"),
        MEXICAN("Mexican"),
        CAFE("Cafe"),
        BAR("Bar"),
        CONVENIENCE_STORE("Convenience Store");

        private final String oneCuisine;

        CuisineType(String filterCuisine) {
            this.oneCuisine = filterCuisine;
        }

        public String getOneCuisine() {
            return oneCuisine;
        }
    }
}
