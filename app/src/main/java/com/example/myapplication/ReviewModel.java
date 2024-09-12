package com.example.myapplication;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class ReviewModel {

//    Kim
    String uDp;
    int likes, dislike;
    int reviewId;
    String position;
    String timestamp;
    ArrayList<ReviewModel> reply;
    ArrayList<String> tags;
    String pImage;

    String reviewTitle;
    int reviewRating;
    ArrayList<Bitmap> reviewImages;
    String reviewText;
    int reviewerId;
    int businessId;


    public ReviewModel(int reviewRating, String reviewTitle, ArrayList<Bitmap> reviewImages, String reviewText, int reviewerId, int businessId) {
        this.reviewRating = reviewRating;
        this.reviewTitle = reviewTitle;
        this.reviewImages = reviewImages;
        this.reviewText = reviewText;
        this.reviewerId = reviewerId;
        this.businessId = businessId;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public int getReviewRating() {
        return reviewRating;
    }

    public ArrayList<Bitmap> getReviewImages() {
        return reviewImages;
    }

    public String getReviewText() {
        return reviewText;
    }

    public int getReviewerId() {
        return reviewerId;
    }

    public int getBusinessId() {
        return businessId;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes += likes;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike += dislike;
    }

    public void setPosition(String position){
        this.position = position;
    }

    public String getPosition()
    {
        return this.position;
    }

    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }

    public String getTimestamp(){
        return timestamp;
    }

    public ArrayList<ReviewModel> getReply() {
        return reply;
    }

    public void setReply(ArrayList<ReviewModel> reply) {
        this.reply = reply;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getuDp() {
        return uDp;
    }

    public void setuDp(String uDp) {
        this.uDp = uDp;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }
}
