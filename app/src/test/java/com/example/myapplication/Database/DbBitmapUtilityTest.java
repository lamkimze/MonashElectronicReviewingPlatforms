package com.example.myapplication.Database;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.graphics.Bitmap;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DbBitmapUtilityTest {

    /**
     * Test method for DbBitmapUtility.getBytes(Bitmap).
     * This method tests the conversion of a Bitmap object to a byte array.
     */
    @Test
    public void testGetBytes() {
        // Create a sample bitmap with specified width, height, and configuration
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        // Fill the bitmap with a solid color (red)
        bitmap.eraseColor(android.graphics.Color.RED);

        // Convert the bitmap to a byte array using the DbBitmapUtility.getBytes method
        byte[] byteArray = null;
        try {
            byteArray = DbBitmapUtility.getBytes(bitmap);
        } catch (IOException e) {
            fail("IOException occurred while converting bitmap to byte array");
        }

        // Check if the byte array is not null
        assertNotNull(byteArray);
        // Check if the byte array has a length greater than 0
        assertTrue(byteArray.length > 0);

        // Verify the internal logic: the byte array should be the same as the one produced by ByteArrayOutputStream
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] expectedByteArray = stream.toByteArray();
        assertArrayEquals(expectedByteArray, byteArray);
    }

    /**
     * Test method for DbBitmapUtility.getBitmap(byte[]).
     * This method tests the conversion of a byte array back to a Bitmap object.
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

        // Verify the internal logic: the pixels of the original and converted bitmaps should be the same
        int[] originalPixels = new int[100 * 100];
        originalBitmap.getPixels(originalPixels, 0, 100, 0, 0, 100, 100);
        int[] convertedPixels = new int[100 * 100];
        convertedBitmap.getPixels(convertedPixels, 0, 100, 0, 0, 100, 100);
        assertArrayEquals(originalPixels, convertedPixels);
    }
}