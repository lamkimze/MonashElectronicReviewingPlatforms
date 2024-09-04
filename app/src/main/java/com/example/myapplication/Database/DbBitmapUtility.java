package com.example.myapplication.Database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DbBitmapUtility {
    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        stream.close(); // close the stream
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getBlob(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
