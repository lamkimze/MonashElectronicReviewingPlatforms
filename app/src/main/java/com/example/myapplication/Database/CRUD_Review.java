package com.example.myapplication.Database;

import static java.lang.String.*;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.graphics.Bitmap;

import com.example.myapplication.Enumerables.ImageType;
import com.example.myapplication.Response;
import com.example.myapplication.ReviewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
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


        Gson gson = new Gson();
        String[] allTags = review.getTags();
        ArrayList<Integer> allLikes = review.getLikes();
        ArrayList<Integer> allDisLikes = review.getDislike();

        String gsonTags = gson.toJson(allTags);
        String gsonLikes = gson.toJson(allLikes);
        String gsonDislikes = gson.toJson(allDisLikes);

        contentValues.put("review_tags", gsonTags);
        contentValues.put("likes_user", gsonLikes);
        contentValues.put("dislike_user", gsonDislikes);
        int reviewID = (int) db.insert("review", null, contentValues);
        CRUD_Image crudImage = new CRUD_Image(dbHelper);
        try {
            crudImage.addImagesToReview(reviewID, review.getReviewImages());
        } catch (Exception e) {
            e.printStackTrace();
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

        int likesIndex, disLikeIndex, tagsIndex, reviewIdIndex, ratingIndex, titleIndex, textIndex, customerIndex, businessIndex, timestampIndex;
        ratingIndex = cursor.getColumnIndex("star_rating");
        titleIndex = cursor.getColumnIndex("review_title");
        textIndex = cursor.getColumnIndex("review_text");
        customerIndex = cursor.getColumnIndex("user_id");
        businessIndex = cursor.getColumnIndex("bus_id");
        timestampIndex = cursor.getColumnIndex("review_date");
        reviewIdIndex = cursor.getColumnIndex("review_id");
        tagsIndex = cursor.getColumnIndex("review_tags");
        likesIndex = cursor.getColumnIndex("likes_user");
        disLikeIndex = cursor.getColumnIndex("dislike_user");

        int[] indices = {likesIndex, disLikeIndex, tagsIndex, reviewIdIndex, timestampIndex,
                         ratingIndex, titleIndex, textIndex, customerIndex, businessIndex};



        // check if any of the indices are -1
        if (Arrays.stream(indices).anyMatch(i -> i == -1)) {
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
            reviewModel.setReviewerId(cursor.getInt(customerIndex));
            reviewModel.setReviewId(cursor.getInt(reviewIdIndex));
            reviewModel.setTimestamp(cursor.getString(timestampIndex));

            Gson gson = new Gson();
            Type stringType = new TypeToken<String[]>() {}.getType();
            Type intType = new TypeToken<ArrayList<Integer>>() {}.getType();
            String[] tags = gson.fromJson(cursor.getString(tagsIndex), stringType);
            ArrayList<Integer> likes = gson.fromJson(cursor.getString(likesIndex), intType);
            ArrayList<Integer> dislikes = gson.fromJson(cursor.getString(disLikeIndex), intType);
            reviewModel.setTags(tags);
            reviewModel.setLikes(likes);
            reviewModel.setDislike(dislikes);

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
                cursor.getInt(reviewIndex),
                cursor.getInt(userIndex),
                cursor.getString(textIndex)
        );response.setResponseID(cursor.getInt(responseIndex));
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

    public void deleteReview(int reviewId){
        deleteAllResponse(reviewId);
        SQLiteDatabase db =dbHelper.getWritableDatabase();
        String whereClause = format(locale, "review_id=%d", reviewId);
        db.delete("review", whereClause, null);
    }

    public void deleteResponse(int responseId){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = format(locale, "response_id=%d", responseId);
        db.delete("response", whereClause, null);
    }

    public void deleteAllResponse(int reviewId){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = format(locale, "review_id=%d", reviewId);
        db.delete("response", whereClause, null);
    }

    public void replaceReview(int reviewId, ReviewModel review) {
        // insert the review into the database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", review.getReviewerId());
        contentValues.put("bus_id", review.getBusinessId());
        contentValues.put("star_rating", review.getReviewRating());
        contentValues.put("review_title", review.getReviewTitle());
        contentValues.put("review_text", review.getReviewText());

        Gson gson = new Gson();
        String[] allTags = review.getTags();
        ArrayList<Integer> likes = review.getLikes();
        ArrayList<Integer> dislike = review.getDislike();

        String gsonLikes = gson.toJson(likes);
        String gsonDislikes = gson.toJson(dislike);
        String gsonTags = gson.toJson(allTags);

        String whereClause = format(locale, "review_id=%d", reviewId);

        contentValues.put("likes_user", gsonLikes);
        contentValues.put("dislike_user", gsonDislikes);
        contentValues.put("review_tags", gsonTags);
        db.update("review", contentValues, whereClause, null);

        // Insert the images into the database

            CRUD_Image crudImage = new CRUD_Image(dbHelper);
            try {
                crudImage.addImagesToReview(reviewId, review.getReviewImages());
            } catch (Exception e) {
                db.close();
                e.printStackTrace();
            }
    }

    public boolean updateLikes(int reviewId, ArrayList<Integer> likeList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // Serialize the updated list back to JSON
        Gson gson = new Gson();
        String gsonLikes = gson.toJson(likeList);
        String whereClause = String.format(locale, "review_id=%d", reviewId);

        // Update the likes_user field in the database
        contentValues.put("likes_user", gsonLikes);
        db.update("review", contentValues, whereClause, null);
        return true;
    }


    public boolean updateDisLikes(int reviewId, ArrayList<Integer> dislikeList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // Serialize the updated list back to JSON
        Gson gson = new Gson();
        String gsonDisLike = gson.toJson(dislikeList);
        String whereClause = String.format(locale, "review_id=%d", reviewId);

        // Update the dislikes_user field in the database
        contentValues.put("dislike_user", gsonDisLike);
        db.update("review", contentValues, whereClause, null);
        return true;
    }


//    /**
//     * Method to get the reviewerId
//     *
//     */
//    public int getReviewerId(ReviewModel review){
//        int userId;
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//
//
//
//        return userId;
//    }
public ArrayList<ReviewModel> getLatestReviews() {
    ArrayList<ReviewModel> reviews = new ArrayList<>();
    SQLiteDatabase db = dbHelper.getReadableDatabase();
    CRUD_Image crudImage = new CRUD_Image(dbHelper);

    // SQL query to select the 5 latest reviews ordered by review_date in descending order
    String selectLatestReviews = "SELECT * FROM review ORDER BY review_date DESC LIMIT 5;";
    Cursor cursor = null;

    try {
        // Execute query
        cursor = db.rawQuery(selectLatestReviews, null);

        // Check if cursor is not null and contains data
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Check and get the index of each column
                int reviewIdIndex = cursor.getColumnIndex("review_id");
                int ratingIndex = cursor.getColumnIndex("star_rating");
                int titleIndex = cursor.getColumnIndex("review_title");
                int textIndex = cursor.getColumnIndex("review_text");
                int customerIndex = cursor.getColumnIndex("user_id");
                int businessIndex = cursor.getColumnIndex("bus_id");
                int likesIndex = cursor.getColumnIndex("likes_user"); // New: Likes column
                int dislikesIndex = cursor.getColumnIndex("dislike_user"); // New: Dislikes column

                // Verify that the necessary columns are available
                if (reviewIdIndex != -1 && ratingIndex != -1 && titleIndex != -1 &&
                        textIndex != -1 && customerIndex != -1 && businessIndex != -1 &&
                        likesIndex != -1 && dislikesIndex != -1) {

                    // Get review ID to fetch associated images
                    int reviewId = cursor.getInt(reviewIdIndex);
                    ArrayList<Bitmap> reviewImages = crudImage.getReviewImages(reviewId);

                    // Deserialize likes and dislikes JSON strings into ArrayList<Integer>
                    Gson gson = new Gson();
                    Type intType = new TypeToken<ArrayList<Integer>>() {}.getType();

                    ArrayList<Integer> likes = gson.fromJson(cursor.getString(likesIndex), intType);
                    ArrayList<Integer> dislikes = gson.fromJson(cursor.getString(dislikesIndex), intType);

                    // Create a ReviewModel object
                    ReviewModel reviewModel = new ReviewModel(
                            cursor.getFloat(ratingIndex),
                            cursor.getString(titleIndex),
                            reviewImages,
                            cursor.getString(textIndex),
                            cursor.getInt(customerIndex),
                            cursor.getInt(businessIndex)
                    );

                    // Set review metadata
                    reviewModel.setReviewerId(cursor.getInt(customerIndex));
                    reviewModel.setReviewId(cursor.getInt(reviewIdIndex));
                    reviewModel.setLikes(likes);  // Set likes
                    reviewModel.setDislike(dislikes);  // Set dislikes

                    // Add the review to the list
                    reviews.add(reviewModel);
                }
            } while (cursor.moveToNext());
        }
    } catch (Exception e) {
        e.printStackTrace();  // Log the exception for debugging
    } finally {
        if (cursor != null) {
            cursor.close();  // Always close the cursor to avoid memory leaks
        }
    }

    return reviews;
}

    /**
     * Method to get the review date from the database
     * @param reviewId the ID of the review to get the date for
     * @return the date of the review as a String, or null if not found
     */
    public String getReviewDate(int reviewId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // SQL query to select the review date based on the review ID
        String selectReviewDate = format(locale, "SELECT review_date FROM review WHERE review_id = %d;", reviewId);
        Cursor cursor = db.rawQuery(selectReviewDate, null);

        // If a review is found, return the review date
        if (cursor.moveToFirst()) {
            int reviewDateIndex = cursor.getColumnIndex("review_date");
            if (reviewDateIndex != -1) {
                String reviewDate = cursor.getString(reviewDateIndex);
                cursor.close();
                return reviewDate;
            }
        }

        // Close the cursor and return null if no review is found
        cursor.close();
        return null;  // Indicating no review found
    }





}
