package com.example.myapplication;

import java.util.Comparator;

public class RestaurantReviewCompetition {
    private static int accumulatedReview = 0;
    private static int accumulateMonth = 0;
    private int monthlyReview = 0;
    private int seasonReview = 0;
    private int halfYearReview = 0;
    private int yearlyReview = 0;
    private int goldMedalNo = 0;
    private int silverMedalNo = 0;
    private int bronzeMedalNo = 0;
    protected int placeChange = 0;


    public static int getAccumulateMonth() {
        return accumulateMonth;
    }

    public int getPlaceChange() {
        return placeChange;
    }

    public void incrementReview(){
        accumulatedReview ++;

    }

    public int getAccumulatedReview() {
        return accumulatedReview;
    }

    public int getGoldMedalNo() {
        return goldMedalNo;
    }

    public int getSilverMedalNo() {
        return silverMedalNo;
    }

    public int getBronzeMedalNo() {
        return bronzeMedalNo;
    }

    public int getMonthlyReview() {
        return monthlyReview;
    }

    public int getSeasonReview() {
        return seasonReview;
    }

    public int getHalfYearReview() {
        return halfYearReview;
    }

    public int getYearlyReview() {
        return yearlyReview;
    }

}

