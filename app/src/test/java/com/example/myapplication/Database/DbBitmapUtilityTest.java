package com.example.myapplication.Database;

import static org.junit.Assert.*;

import android.graphics.Bitmap;

import org.junit.Test;

import java.io.IOException;

public class DbBitmapUtilityTest {

    /**
     * Test method for DbBitmapUtility.getBytes(Bitmap).
     * This method tests the conversion of a Bitmap object to a byte array.
     * What is being tested?: The getBytes method of the DbBitmapUtility class is being tested.
     * What is the input?: A Bitmap object with a specified width, height, and configuration.
     * What is the expected output?: A byte array that represents the Bitmap object.
     * How is the output validated?: The byte array is checked to ensure it is not null and has a length greater than 0.
     * How is the test result presented?: The test passes if the byte array is not null and has a length greater than 0.
     * How is it tested?: The test creates a sample bitmap, converts it to a byte array, and checks the byte array.
     */
    @Test
    public void testGetBytes() {
        // Create a sample bitmap with specified width, height, and configuration
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        // Fill the bitmap with a solid color (red)
        bitmap.eraseColor(android.graphics.Color.RED);
        byte[] byteArray = null;
        // Convert the bitmap to a byte array using the DbBitmapUtility.getBytes method
        try {
            byteArray = DbBitmapUtility.getBytes(bitmap);
        } catch (IOException e) {
            fail("IOException occurred while converting bitmap to byte array");
        }
        // Check if the byte array is not null
        assertNotNull(byteArray);
        // Check if the byte array has a length greater than 0
        assertTrue(byteArray.length > 0);
    }

    /**
     * Test method for DbBitmapUtility.getBitmap(byte[]).
     * This method tests the conversion of a byte array back to a Bitmap object.
     * What is being tested?: The getBitmap method of the DbBitmapUtility class is being tested.
     * What is the input?: A byte array that represents a Bitmap object.
     * What is the expected output?: A Bitmap object that is reconstructed from the byte array.
     * How is the output validated?: The reconstructed Bitmap object is checked to ensure it is not null and has the same dimensions as the original.
     * How is the test result presented?: The test passes if the reconstructed Bitmap object is not null and has the same dimensions as the original.
     * How is it tested?: The test converts a sample bitmap to a byte array, then converts the byte array back to a bitmap and checks the dimensions.
     */
    @Test
    public void testGetBitmap() {
        // Create a sample bitmap with specified width, height, and configuration
        Bitmap originalBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        // Fill the bitmap with a solid color (red)
        originalBitmap.eraseColor(android.graphics.Color.RED);

        // Convert the bitmap to a byte array using the DbBitmapUtility.getBytes method
        byte[] byteArray = null;
        try {
            byteArray = DbBitmapUtility.getBytes(originalBitmap);
        } catch (IOException e) {
            fail("IOException occurred while converting bitmap to byte array");
        }
        // Convert the byte array back to a bitmap using the DbBitmapUtility.getBitmap method
        Bitmap convertedBitmap = DbBitmapUtility.getBitmap(byteArray);

        // Check if the converted bitmap is not null and has the same dimensions as the original
        assertNotNull(convertedBitmap);
        assertEquals(originalBitmap.getWidth(), convertedBitmap.getWidth());
        assertEquals(originalBitmap.getHeight(), convertedBitmap.getHeight());
    }
}