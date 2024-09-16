package com.example.myapplication.Database;

import static java.lang.String.*;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.Enumerables.ImageType;
import com.example.myapplication.ReviewModel;

import java.util.ArrayList;
import java.util.Locale;

public class CRUD_Review {
    DatabaseHelper dbHelper;
    Locale locale = Locale.getDefault();
    public CRUD_Review(DatabaseHelper dbHelper) {this.dbHelper = dbHelper;}

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

        // Get the review ID
        String selectReview = format(locale,
                "SELECT review_id FROM review WHERE bus_id = %d AND customer_id = %d;",
                review.getBusinessId(), review.getReviewerId()
        );
        Cursor cursor = db.rawQuery(selectReview, null);
        cursor.moveToFirst();
        int reviewIndex = cursor.getColumnIndex("review_id");
        if (reviewIndex != -1) {
            int reviewId = cursor.getInt(reviewIndex);
            cursor.close();
            CRUD_Image crudImage = new CRUD_Image(dbHelper);
            try {
                crudImage.addImagesToReview(reviewId, review.getReviewImages());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    // Read Methods

    public ReviewModel getReview(int reviewId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        CRUD_Image crudImage = new CRUD_Image(dbHelper);
        String selectReview = format(locale,"SELECT * FROM review WHERE review_id = %d;", reviewId);
        Cursor cursor = db.rawQuery(selectReview, null);
        cursor.moveToFirst();

        int ratingIndex, titleIndex, textIndex, customerIndex, businessIndex;
        ratingIndex = cursor.getColumnIndex("rating");
        titleIndex = cursor.getColumnIndex("title");
        textIndex = cursor.getColumnIndex("text");
        customerIndex = cursor.getColumnIndex("customer_id");
        businessIndex = cursor.getColumnIndex("bus_id");

        // If the indexes are not found, return null
        if (ratingIndex == -1 || titleIndex == -1 || textIndex == -1 || customerIndex == -1 || businessIndex == -1) {
            cursor.close();
            return null;
        } else {
            ReviewModel reviewModel =  new ReviewModel(cursor.getInt(ratingIndex),
                    cursor.getString(titleIndex),
                    crudImage.getReviewImages(reviewId),
                    cursor.getString(textIndex),
                    cursor.getInt(customerIndex),
                    cursor.getInt(businessIndex));
            cursor.close();
            return reviewModel;
        }
    }

    /**
     * Method to get all the review IDs for a business
     * @param businessId the business to get the reviews for
     * @return an array list of all the review IDs
     */
    public ArrayList<Integer> getReviewIDs(int businessId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectReviews = format(locale,"SELECT review_id FROM review WHERE bus_id = %d;", businessId);
        Cursor cursor = db.rawQuery(selectReviews, null);
        ArrayList<Integer> reviewIDs = new ArrayList<>();
        while (cursor.moveToNext()) {
            int columnIndex = cursor.getColumnIndex("review_id");
            if (columnIndex != -1) {
                reviewIDs.add(cursor.getInt(columnIndex));
            }
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
