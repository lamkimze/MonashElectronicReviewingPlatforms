package com.example.myapplication.Database;

import static java.lang.String.format;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.example.myapplication.Enumerables.ImageType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class CRUD_Image {
    DatabaseHelper dbHelper;
    Locale locale = Locale.getDefault();
    CRUD_Image(DatabaseHelper dbHelper) {this.dbHelper = dbHelper;}


    // Create methods
    /**
     * Inserts an image into the database
     * @param linkingID The foreign key that links the image to another table
     * @param imageType The type of image
     * @param image A bitmap of the image
     * @throws IOException If the image cannot be converted to a byte array
     */
    public void insertImage(int linkingID, ImageType imageType, Bitmap image) throws IOException {
        // Set up the database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Convert the image to a byte array
        byte[] imageBytes = DbBitmapUtility.getBytes(image);
        // Get the table name and foreign key
        String tableName = imageType.getTableName();
        String foreignKey = imageType.getForeignKey();

        // Insert the image into the database
        ContentValues contentValues = new ContentValues();
        contentValues.put(foreignKey, linkingID);
        contentValues.put("image_data", imageBytes);
        db.insert(tableName, null, contentValues);
    }

    public void insertImages(int linkingID, ArrayList<Bitmap> images, ImageType imageType) throws IOException {
        for (Bitmap image : images) {
            insertImage(linkingID, imageType, image);
        }
    }

    // Read methods

    /**
     * Retrieves an image from the database
     * @param image_id The id of the image
     * @param imageType The type of image (business or review)
     * @return A bitmap of the image
     */
    public Bitmap getImage(int image_id, ImageType imageType) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String tableName = imageType.getTableName();

        // Get the image from the database
        String query = format(locale,"SELECT image_data FROM %s WHERE image_id = %d", tableName, image_id);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            byte[] imageBytes = cursor.getBlob(0);
            cursor.close();
            return DbBitmapUtility.getBlob(imageBytes);
        }
        else {
            cursor.close();
            return null;
        }
    }

    /**
     * Retrieves all images that are linked to a review or business
     * @param linkingID The id of the review or business
     * @param imageType The type of image (business or review)
     * @return An array list of bitmaps of the images
     */
    public ArrayList<Integer> getImageIDs(int linkingID, ImageType imageType) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String tableName = imageType.getTableName();
        String foreignKey = imageType.getForeignKey();

        String query = format(locale,"SELECT image_id FROM %s WHERE %s = %d", tableName, foreignKey, linkingID);

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Integer> imageIDs = new ArrayList<>();

        while (cursor.moveToNext()) {
            imageIDs.add(cursor.getInt(0));
        }

        cursor.close();
        return imageIDs;
    }

    /**
     * Retrieves all images that are linked to a review or business
     * @param linkingID The id of the review or business
     * @param imageType The type of image (business or review)
     * @return An array list of bitmaps of the images
     */
    public ArrayList<Bitmap> getImages(int linkingID, ImageType imageType) {
        ArrayList<Integer> imageIDs = getImageIDs(linkingID, imageType);
        ArrayList<Bitmap> images = new ArrayList<>();

        for (int imageID : imageIDs) {
            images.add(getImage(imageID, imageType));
        }

        return images;
    }

}
