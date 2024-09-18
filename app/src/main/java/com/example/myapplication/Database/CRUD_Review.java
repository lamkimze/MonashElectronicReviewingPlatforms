package com.example.myapplication.Database;

import static java.lang.String.*;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.myapplication.Enumerables.ImageType;
import com.example.myapplication.Response;
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
        // insert the review into the database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", review.getReviewerId());
        contentValues.put("bus_id", review.getBusinessId());
        contentValues.put("star_rating", review.getReviewRating());
        contentValues.put("review_title", review.getReviewTitle());
        contentValues.put("review_text", review.getReviewText());
        db.insert("review", null, contentValues);

        // Insert the images into the database

        // Get the review ID
        String selectReview = format(locale,
                "SELECT review_id FROM review WHERE bus_id = %d AND user_id = %d;",
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

    /**
     * Method to create a response in the database
     * @param response the response to be created
     * @return the response that was created
     */
    public void createResponse(Response response) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("review_id", response.getReviewID());
        contentValues.put("user_id", response.getUserID());
        contentValues.put("response_text", response.getResponseText());
        db.insert("response", null, contentValues);
    }

    // Read Methods

    /**
     * Method to get a review from the database
     * @param reviewId the ID of the review to get
     * @return the review with the given ID
     */
    public ReviewModel getReview(int reviewId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        CRUD_Image crudImage = new CRUD_Image(dbHelper);
        String selectReview = format(locale,"SELECT * FROM review WHERE review_id = %d;", reviewId);
        Cursor cursor = db.rawQuery(selectReview, null);
        cursor.moveToFirst();

        int ratingIndex, titleIndex, textIndex, customerIndex, businessIndex;
        ratingIndex = cursor.getColumnIndex("star_rating");
        titleIndex = cursor.getColumnIndex("rewiew_title");
        textIndex = cursor.getColumnIndex("review_text");
        customerIndex = cursor.getColumnIndex("user_id");
        businessIndex = cursor.getColumnIndex("bus_id");

        // If the indexes are not found, return null
        if (ratingIndex == -1 || titleIndex == -1 || textIndex == -1 || customerIndex == -1 || businessIndex == -1) {
            cursor.close();
            return null;
        } else {
            ReviewModel reviewModel =  new ReviewModel(
                    cursor.getFloat(ratingIndex),
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
     * Method to get a response from the database
     * @param responseID the ID of the response to get
     * @return the response with the given ID
     */
    public Response getResponse(int responseID){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectResponse = format(locale,
                "SELECT * FROM response WHERE response_id = %d;",
                responseID
        );
        Cursor cursor = db.rawQuery(selectResponse, null);
        cursor.moveToFirst();
        int responseIndex, reviewIndex, userIndex, textIndex, dateIndex;
        responseIndex = cursor.getColumnIndex("response_id");
        reviewIndex = cursor.getColumnIndex("review_id");
        userIndex = cursor.getColumnIndex("user_id");
        textIndex = cursor.getColumnIndex("response_text");
        dateIndex = cursor.getColumnIndex("response_date");
        Response response = new Response(
                cursor.getInt(responseIndex),
                cursor.getInt(reviewIndex),
                cursor.getInt(userIndex),
                cursor.getString(textIndex)
        );
        response.setResponseDate(cursor.getString(dateIndex));
        cursor.close();
        return response;
    }


    /**
     * Method to get all the review IDs for a business
     * @param businessId the business to get the reviews for
     * @return an array list of all the review IDs
     */
    public ArrayList<Integer> getReviewIDs(int businessId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectReviews = format(locale,
                "SELECT review_id FROM review WHERE bus_id = %d;",
                businessId
        );
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
     * Method to get all the response IDs for a review
     * @param reviewID the review to get the responses for
     * @return an array list of all the response IDs
     */
    public ArrayList<Integer> getResponseIDs(int reviewID) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectResponses = format(locale,
                "SELECT response_id FROM response WHERE review_id = %d;",
                reviewID
        );
        Cursor cursor = db.rawQuery(selectResponses, null);
        ArrayList<Integer> responseIDs = new ArrayList<>();
        while (cursor.moveToNext()) {
            int columnIndex = cursor.getColumnIndex("response_id");
            if (columnIndex != -1) {
                responseIDs.add(cursor.getInt(columnIndex));
            }
        }
        cursor.close();
        return responseIDs;
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

    /**
     * Method to get all the responses for a review
     * @param reviewID the review to get the responses for
     * @return an array list of all the responses
     */
    public ArrayList<Response> getResponses(int reviewID) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Integer> responseIDs = getResponseIDs(reviewID);
        ArrayList<Response> responses = new ArrayList<>();
        for (int responseID : responseIDs) {
            responses.add(getResponse(responseID));
        }
        return responses;
    }





}
