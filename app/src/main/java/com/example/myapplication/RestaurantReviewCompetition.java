package com.example.myapplication;

public class RestaurantReviewCompetition {
    private int daily_review = 0;
    private int goldMedalNo = 0;
    private int silverMedalNo = 0;
    private int bronzeMedalNo = 0;
    protected int prev_ranking = 0;


    public int getDaily_review() {
        return daily_review;
    }

    public void setDaily_review(int daily_review) {
        this.daily_review = daily_review;
    }

    public void setPrev_ranking(int daily_review) {
        this.daily_review = daily_review;
    }

    public void setGoldMedalNo(int goldMedalNo) {
        this.goldMedalNo = goldMedalNo;
    }

    public void setSilverMedalNo(int silverMedalNo) {
        this.silverMedalNo = silverMedalNo;
    }

    public void setBronzeMedalNo(int bronzeMedalNo) {
        this.bronzeMedalNo = bronzeMedalNo;
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

    public int getPrev_ranking() {
        return daily_review;
    }

}

