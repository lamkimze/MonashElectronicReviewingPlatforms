package com.example.myapplication.Database;

import static java.lang.String.format;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.example.myapplication.Entities.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class CRUD_Image {
    DatabaseHelper dbHelper;
    Locale locale = Locale.getDefault();
    public CRUD_Image(DatabaseHelper dbHelper) {this.dbHelper = dbHelper;}


    // CRUD methods

    // Read methods

    /**
     * Method to get the profile picture of a user
     * @param user the user to get the profile picture of
     * @return the profile picture of the user
     */
    public Bitmap getProfilePicture(User user) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String username = user.getUsername();
        String selectUser = format(locale, "SELECT * FROM user WHERE username = '%s';", username);
        Cursor cursor = db.rawQuery(selectUser, null);

        cursor.moveToFirst();
        int index = cursor.getColumnIndex("profile_image");
        if (index == -1) {
            cursor.close();
            return null;
        }
        byte[] profileImageBLOB = cursor.getBlob(index);
        if (profileImageBLOB == null) {
            cursor.close();
            return null;
        }
        cursor.close();
        return DbBitmapUtility.getBitmap(profileImageBLOB);
    }

    /**
     * Method to get the image of a business
     * @param businessID the ID of the business
     * @return the image of the business
     */
    public Bitmap getBusinessImage(int businessID) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectBusiness = format(locale, "SELECT * FROM business WHERE bus_id = %d;", businessID);
        Cursor cursor = db.rawQuery(selectBusiness, null);

        cursor.moveToFirst();
        int index = cursor.getColumnIndex("bus_image");
        if (index == -1) {
            cursor.close();
            return null;
        }
        Bitmap image = DbBitmapUtility.getBitmap(cursor.getBlob(index));
        cursor.close();
        return image;
    }

    /**
     * Method to get the images Ids of a review
     * @param reviewID the ID of the review
     * @return a list of the images IDs of the review
     */
    public ArrayList<Integer> getReviewImagesIDs(int reviewID) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectReview = format(locale, "SELECT * FROM review_image WHERE review_id = %d;", reviewID);
        Cursor cursor = db.rawQuery(selectReview, null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex("image_id");
        if (index == -1) {
            cursor.close();
            return null;
        }
        ArrayList<Integer> ids = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            int image = cursor.getInt(index);
            ids.add(image);
            cursor.moveToNext();
        }
        cursor.close();
        return ids;
    }

    /**
     * Method to get the images of a review
     * @param reviewID the ID of the review
     * @param imageID the ID of the image
     * @return the image of the review
     */
    public Bitmap getReviewImage(int reviewID, int imageID) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectReview = format(locale, "SELECT * FROM review_image WHERE review_id = %d AND image_id = %d;", reviewID, imageID);
        Cursor cursor = db.rawQuery(selectReview, null);

        cursor.moveToFirst();
        int index = cursor.getColumnIndex("image_data");
        if (index == -1) {
            cursor.close();
            return null;
        }
        byte[] reviewImageBLOB = cursor.getBlob(index);
        if (reviewImageBLOB == null) {
            cursor.close();
            return null;
        }
        cursor.close();
        return DbBitmapUtility.getBitmap(reviewImageBLOB);
    }

    /**
     * Method to get all the images of a review
     * @param reviewID the ID of the review
     * @return a list of images of the review
     */
    public ArrayList<Bitmap> getReviewImages(int reviewID) {
        ArrayList<Integer> imageIDs = getReviewImagesIDs(reviewID);
        ArrayList<Bitmap> images = new ArrayList<>();
        for (int imageID : imageIDs) {
            images.add(getReviewImage(reviewID, imageID));
        }
        return images;
    }


    // Update methods

    /**
     * Method to update the profile picture of a user
     * @param userID the ID of the user
     * @param image the new profile picture of the user
     */
    public void setUserProfilePicture(int userID, Bitmap image) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        // Convert the image to a byte array
        try {
            byte[] imageBytes = DbBitmapUtility.getBytes(image);
            contentValues.put("profile_image", imageBytes);
            String whereClause = format(locale, "user_id = %d", userID);
            db.update("user", contentValues, whereClause, null);
        } catch (IOException e) {
            return;
        }
    }

    /**
     * Method to update the image of a business
     * @param businessID the ID of the business
     * @param image the new image of the business
     */
    public void setBusinessImage(int businessID, Bitmap image){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        // Convert the image to a byte array
        try {
            byte[] imageBytes = DbBitmapUtility.getBytes(image);
            contentValues.put("bus_image", imageBytes);
            String whereClause = format(locale, "bus_id = %d", businessID);
            db.update("business", contentValues, whereClause, null);
        } catch (IOException e) {
            return;
        }
    }

    /**
     * Method to update the image of a review
     * @param reviewID the ID of the review
     * @param image an image to add to the review
     */
    public void addImageToReview(int reviewID, Bitmap image) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        // Convert the image to a byte array
        try {
            byte[] imageBytes = DbBitmapUtility.getBytes(image);
            contentValues.put("image_data", imageBytes);
            contentValues.put("review_id", reviewID);
            db.insert("review_image", null, contentValues);
        } catch (IOException e) {
            return;
        }
    }

    /**
     * Method to add multiple images to a review
     * @param reviewID the ID of the review
     * @param images an array of images to add to the review
     */
    public void addImagesToReview(int reviewID, ArrayList<Bitmap> images) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (Bitmap image : images) {
            addImageToReview(reviewID, image);
        }
    }
}
