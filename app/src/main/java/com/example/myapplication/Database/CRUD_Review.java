package com.example.myapplication.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.Enumerables.ImageType;
import com.example.myapplication.ReviewModel;

import java.util.ArrayList;

public class CRUD_Review {
    DatabaseHelper dbHelper;
    CRUD_Review(DatabaseHelper dbHelper) {this.dbHelper = dbHelper;}

    // Create Methods

    /**
     * Method to create a review in the database
     * @param review the review to be created
     */
    public void createReview(ReviewModel review) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("bus_id", review.getBusinessId());
        values.put("customer_id", review.getReviewerId());
        values.put("rating", review.getReviewRating());
        values.put("title", review.getReviewTitle());
        values.put("text", review.getReviewText());

        db.insert("review", null, values);
    }



    // Read Methods
    @SuppressLint("Range")
    public ReviewModel getReview(int reviewId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        CRUD_Image crudImage = new CRUD_Image(dbHelper);
        String selectReview = "SELECT * FROM review WHERE review_id = " + reviewId + ";";
        Cursor cursor = db.rawQuery(selectReview, null);
        cursor.moveToFirst();

        ReviewModel reviewModel =  new ReviewModel(cursor.getInt(cursor.getColumnIndex("rating")),
                cursor.getString(cursor.getColumnIndex("title")),
                crudImage.getImages(reviewId, ImageType.REVIEW),
                cursor.getString(cursor.getColumnIndex("text")),
                cursor.getInt(cursor.getColumnIndex("customer_id")),
                cursor.getInt(cursor.getColumnIndex("bus_id")));
        cursor.close();
        return reviewModel;
    }

    /**
     * Method to get all the review IDs for a business
     * @param businessId the business to get the reviews for
     * @return an array list of all the review IDs
     */
    @SuppressLint("Range")
    public ArrayList<Integer> getReviewIDs(int businessId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectReviews = "SELECT review_id FROM review WHERE bus_id = " + businessId + ";";
        Cursor cursor = db.rawQuery(selectReviews, null);
        ArrayList<Integer> reviewIDs = new ArrayList<>();
        while (cursor.moveToNext()) {
            reviewIDs.add(cursor.getInt(cursor.getColumnIndex("review_id")));
        }
        cursor.close();
        return reviewIDs;
    }

    /**
     * Method to get all the reviews for a business
     * @param businessId the business to get the reviews for
     * @return an array list of all the reviews
     */
    public ArrayList<ReviewModel> getReviews(int businessId) {
        ArrayList<ReviewModel> reviews = new ArrayList<>();
        ArrayList<Integer> reviewIDs = getReviewIDs(businessId);
        for (int reviewID : reviewIDs) {
            reviews.add(getReview(reviewID));
        }
        return reviews;
    }



}
