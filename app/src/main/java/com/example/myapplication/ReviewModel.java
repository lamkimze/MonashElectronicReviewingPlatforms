package com.example.myapplication;

import android.graphics.Bitmap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class ReviewModel {

//    Kim
    String uDp;
    ArrayList<Integer> likes;
    ArrayList<Integer> dislike;
    int reviewId;
    String position;
    String timestamp;
    ArrayList<Response> reply;
    String[] tags;
    String pImage;

    String reviewTitle;
    float reviewRating;
    ArrayList<Bitmap> reviewImages;
    String reviewText;
    int reviewerId;
    int businessId;

    public ReviewModel(float reviewRating, String reviewTitle, ArrayList<Bitmap> reviewImages, String reviewText, int reviewerId, int businessId) {
        this.reviewRating = reviewRating;
        this.reviewTitle = reviewTitle;
        this.reviewImages = reviewImages;
        this.reviewText = reviewText;
        this.reviewerId = reviewerId;
        this.businessId = businessId;
        this.likes = new ArrayList<>();
        this.dislike = new ArrayList<>();
        this.tags = new String[]{};
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public float getReviewRating() {
        return reviewRating;
    }

    public ArrayList<Bitmap> getReviewImages() {
        return reviewImages;
    }

    public void addReviewImage(Bitmap reviewImage) {
        this.reviewImages.add(reviewImage);
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

    public ArrayList<Integer> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<Integer> likes) {
        this.likes = likes;
    }

    public ArrayList<Integer> getDislike() {
        return dislike;
    }

    public void setDislike(ArrayList<Integer> dislike) {
        this.dislike = dislike;
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

    public ArrayList<Response> getReply() {
        return reply;
    }

    public void setReply(ArrayList<Response> reply) {
        this.reply = reply;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
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

    public void setReviewerId(int reviewerId) {
        this.reviewerId = reviewerId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public static Comparator<ReviewModel> likesAscending = new Comparator<ReviewModel>() {
        @Override
        public int compare(ReviewModel r1, ReviewModel r2) {
            int likes1 = r1.getLikes().size();
            int likes2 = r2.getLikes().size();
            return Integer.compare(likes1, likes2);
        }
    };

    public static Comparator<ReviewModel> replyAscending = new Comparator<ReviewModel>() {
        @Override
        public int compare(ReviewModel r1, ReviewModel r2) {
            int replies1 = r1.getReply().size();
            int replies2 = r2.getReply().size();
            return Integer.compare(replies1, replies2);
        }
    };

    public static Comparator<ReviewModel> dislikesAscending = new Comparator<ReviewModel>() {
        @Override
        public int compare(ReviewModel r1, ReviewModel r2) {
            int dislikes1 = r1.getDislike().size();
            int dislikes2 = r2.getDislike().size();
            return Integer.compare(dislikes1, dislikes2);
        }
    };

    public static Comparator<ReviewModel> timeAscending = new Comparator<ReviewModel>() {
        @Override
        public int compare(ReviewModel r1, ReviewModel r2) {
            String time1 = r1.getTimestamp();
            String time2 = r2.getTimestamp();

            SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            try {
                Date formatTime1 = dateTime.parse(time1);
                Date formatTime2 = dateTime.parse(time2);
                return formatTime1.compareTo(formatTime2);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    };

    public static Comparator<ReviewModel> ratingAscending = new Comparator<ReviewModel>() {
        @Override
        public int compare(ReviewModel r1, ReviewModel r2) {
            double rating1 = r1.getReviewRating();
            double rating2 = r2.getReviewRating();
            return Double.compare(rating1, rating2);
        }
    };
}
