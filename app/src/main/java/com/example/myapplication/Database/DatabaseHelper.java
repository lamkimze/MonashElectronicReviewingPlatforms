package com.example.myapplication.Database;

import static java.lang.String.format;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.Restaurant;
import com.password4j.Password;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
//    increment the version number if you change the schema
    private static final int DATABASE_VERSION = 71;

    private final Context context;


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME,  null, DATABASE_VERSION);
        this.context = context;
    }

    /**
     * This method is called when the database is created for the first time.
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Table creation statements
        // Create user table
        String createUserTable = "CREATE TABLE user (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL, " +
                "email TEXT UNIQUE NOT NULL, " +
                "first_name TEXT NOT NULL, " +
                "last_name TEXT NOT NULL," +
                "position_id INTEGER," +
                "profile_image BLOB," +
                "FOREIGN KEY (position_id) REFERENCES user_position(position_id));";
        db.execSQL(createUserTable);

        // Create userPosition table
        String createUserPositionTable = "CREATE TABLE user_position (" +
                "position_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL," +
                "position_name TEXT NOT NULL," +
                "bus_id INTEGER NOT NULL," +
                "FOREIGN KEY (user_id) REFERENCES user(user_id)," +
                "FOREIGN KEY (bus_id) REFERENCES business(bus_id));";
        db.execSQL(createUserPositionTable);

        // Create business table
        String createBusinessTable = "CREATE TABLE business (" +
                "bus_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "bus_name TEXT UNIQUE NOT NULL, " +
                "bus_addr TEXT NOT NULL, " +
                "bus_ph_nb TEXT, " +
                "bus_email TEXT, " +
                "website_url TEXT, " +
                "bus_hours TEXT, " +
                "bus_cuisine_type TEXT, " +
                "bus_image BLOB);";
        db.execSQL(createBusinessTable);

        // Create new business_image table to store images separately
        String createBusinessImageTable = "CREATE TABLE business_image (" +
                "image_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "bus_id INTEGER NOT NULL, " +
                "bus_image BLOB NOT NULL, " +  // Store the image as BLOB
                "FOREIGN KEY (bus_id) REFERENCES business(bus_id));";
        db.execSQL(createBusinessImageTable);

        // Create review table
        String createReviewTable = "CREATE TABLE review (" +
                "review_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "bus_id INTEGER NOT NULL, " +
                "star_rating DECIMAL(1,2) NOT NULL, " +
                "review_title TEXT, " +
                "review_text TEXT, " +
                "review_date DATE DEFAULT (datetime('now','localtime')), " +
                "review_tags TEXT, " +
                "FOREIGN KEY (user_id) REFERENCES user(user_id), " +
                "FOREIGN KEY (bus_id) REFERENCES business(bus_id));";
        db.execSQL(createReviewTable);

        // Create review image table
        String createReviewImageTable = "CREATE TABLE review_image (" +
                "image_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "review_id INTEGER NOT NULL, " +
                "image_data BLOB NOT NULL, " +  // Store the image data as a BLOB
                "FOREIGN KEY (review_id) REFERENCES review(review_id));";
        db.execSQL(createReviewImageTable);

        // Create response table
        String createResponseTable = "CREATE TABLE response (" +
                "response_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "review_id INTEGER, " +
                "user_id INTEGER, " +
                "response_text TEXT, " +
                "response_date DATE, " +
                "FOREIGN KEY (review_id) REFERENCES review(review_id), " +
                "FOREIGN KEY (user_id) REFERENCES user(user_id));";
        db.execSQL(createResponseTable);


        insertBusinessData(db);
        insertUserData(db);



//        String hashedPassword = Password.hash("password123").withScrypt().getResult();
//        String insertCustomerData = format("INSERT INTO user (username, password, email, first_name, last_name) VALUES\n" +
//                "('jdoe001', '%s', 'jdoe001@student.monash.edu', 'John', 'Doe'),\n" +
//                "('asmith002', '%s', 'asmith002@student.monash.edu', 'Alice', 'Smith'),\n" +
//                "('bwong003', '%s', 'bwong003@student.monash.edu', 'Ben', 'Wong'),\n" +
//                "('charris004', '%s', 'charris004@student.monash.edu', 'Carol', 'Harris'),\n" +
//                "('dlee005', '%s', 'dlee005@student.monash.edu', 'David', 'Lee'),\n" +
//                "('emma006', '%s', 'emma006@student.monash.edu', 'Emma', 'Brown'),\n" +
//                "('fclark007', '%s', 'fclark007@student.monash.edu', 'Frank', 'Clark'),\n" +
//                "('gwhite008', '%s', 'gwhite008@student.monash.edu', 'Grace', 'White'),\n" +
//                "('hking009', '%s', 'hking009@student.monash.edu', 'Henry', 'King'),\n" +
//                "('ijones010', '%s', 'ijones010@student.monash.edu', 'Irene', 'Jones');\n",
//                hashedPassword,
//                hashedPassword,
//                hashedPassword,
//                hashedPassword,
//                hashedPassword,
//                hashedPassword,
//                hashedPassword,
//                hashedPassword,
//                hashedPassword,
//                hashedPassword
//                );
//        db.execSQL(insertCustomerData);



        String insertReview = "INSERT INTO review (user_id, bus_id, star_rating, review_title, review_text, review_date) VALUES\n" +
                "(1, 1, 4.5, 'Excellent Experience', 'The food was amazing and the service was great!', datetime('now','localtime')),\n" +
                "(2, 2, 3.0, 'Good but slow', 'The food was nice, but the service took too long.', datetime('now','localtime')),\n" +
                "(3, 3, 4.0, 'Very nice place', 'I really enjoyed my meal, would come back!', datetime('now','localtime')),\n" +
                "(4, 4, 2.5, 'Mediocre experience', 'The food was bland, nothing special.', datetime('now','localtime')),\n" +
                "(5, 5, 5.0, 'Perfect!', 'Everything was perfect, highly recommend!', datetime('now','localtime')),\n" +
                "(6, 6, 4.0, 'Good food', 'The food was very tasty, but the ambiance could be improved.', datetime('now','localtime')),\n" +
                "(7, 7, 3.5, 'Average', 'The place was okay, food could have been better.', datetime('now','localtime')),\n" +
                "(8, 8, 4.5, 'Awesome!', 'Loved the food and the place was cozy.', datetime('now','localtime')),\n" +
                "(9, 9, 4.0, 'Great food', 'Really enjoyed the meal, will come back!', datetime('now','localtime')),\n" +
                "(10, 10, 2.0, 'Not worth it', 'The food was cold and the service was rude.', datetime('now','localtime')),\n" +
                "(1, 1, 4.2, 'Good vibes', 'Nice place to chill and grab a meal.', datetime('now','localtime')),\n" +
                "(2, 2, 3.8, 'Decent', 'Good meal but could improve on customer service.', datetime('now','localtime')),\n" +
                "(3, 3, 4.7, 'Amazing food!', 'The food was exceptional, would highly recommend!', datetime('now','localtime')),\n" +
                "(4, 4, 2.8, 'Needs improvement', 'Food was okay, but service could be better.', datetime('now','localtime')),\n" +
                "(5, 5, 5.0, 'Fantastic', 'Everything was great, from food to service!', datetime('now','localtime')),\n" +
                "(6, 6, 3.6, 'Quite good', 'Decent food, but ambiance could be better.', datetime('now','localtime')),\n" +
                "(7, 7, 4.4, 'Really good', 'The food was quite tasty, had a good time!', datetime('now','localtime')),\n" +
                "(8, 8, 3.2, 'Not bad', 'It was okay, nothing too special.', datetime('now','localtime')),\n" +
                "(9, 9, 4.1, 'Enjoyable', 'Had a good time, would recommend to friends.', datetime('now','localtime')),\n" +
                "(10, 10, 2.9, 'Could be better', 'Food was okay but the service was a bit slow.', datetime('now','localtime')),\n" +
                "(1, 5, 4.8, 'Wonderful', 'The food was great and the service was excellent!', datetime('now','localtime')),\n" +
                "(2, 6, 4.5, 'Good experience', 'Enjoyed the meal, will come again.', datetime('now','localtime')),\n" +
                "(3, 7, 4.9, 'Fantastic!', 'Best meal I have had in a long time!', datetime('now','localtime')),\n" +
                "(4, 8, 2.4, 'Not great', 'The food was undercooked and bland.', datetime('now','localtime')),\n" +
                "(5, 9, 5.0, 'Loved it!', 'This is my new favorite place!', datetime('now','localtime')),\n" +
                "(6, 10, 3.0, 'Just okay', 'The food was okay, but not worth the price.', datetime('now','localtime')),\n" +
                "(7, 1, 4.6, 'Great time', 'I had a great time here, would come again!', datetime('now','localtime')),\n" +
                "(8, 2, 3.9, 'Pretty good', 'Good food, decent service, overall good.', datetime('now','localtime')),\n" +
                "(9, 3, 4.5, 'Really good', 'Loved the meal and the staff were very friendly.', datetime('now','localtime')),\n" +
                "(10, 4, 3.2, 'Not bad', 'Could be better but still a good time.', datetime('now','localtime')),\n" +
                "(1, 10, 4.0, 'Good place', 'Had a fun time, food was tasty!', datetime('now','localtime')),\n" +
                "(2, 9, 4.8, 'Amazing', 'Everything was fantastic!', datetime('now','localtime')),\n" +
                "(3, 8, 4.7, 'Loved it!', 'Will definitely come back again!', datetime('now','localtime')),\n" +
                "(4, 7, 2.5, 'Disappointing', 'The food was not up to standard.', datetime('now','localtime')),\n" +
                "(5, 6, 4.9, 'Highly recommend', 'Best meal I have had in a while!', datetime('now','localtime')),\n" +
                "(6, 5, 3.4, 'Okayish', 'It was okay but not worth the price.', datetime('now','localtime')),\n" +
                "(7, 4, 4.3, 'Pretty good', 'I enjoyed the food and ambiance!', datetime('now','localtime')),\n" +
                "(8, 3, 4.7, 'Great!', 'Loved the meal, will come back.', datetime('now','localtime')),\n" +
                "(9, 2, 2.6, 'Could be better', 'Not the best experience, food was cold.', datetime('now','localtime')),\n" +
                "(10, 1, 5.0, 'Amazing!', 'Best food I have ever had!', datetime('now','localtime'));\n";
        db.execSQL(insertReview);
        insertReviewImage(1, R.drawable.artichokereview, db);
        insertReviewImage(2, R.drawable.boostjuicereview, db);
        insertReviewImage(3, R.drawable.cafecinquelirereview, db);
        insertReviewImage(4, R.drawable.churchofsecularreview, db);

        insertBusinessImage(1,R.drawable.artichokebus1,db);
        insertBusinessImage(1,R.drawable.artichokebus2,db);
        insertBusinessImage(1,R.drawable.artichokebus3,db);
        insertBusinessImage(1,R.drawable.artichokebus4,db);
    }

    /**
     * Inserts an image from the drawable resources into the review_image table.
     *
     * @param reviewId   The ID of the review to associate the image with.
     * @param drawableId The drawable resource ID of the image.
     * @return true if the image was successfully inserted, false otherwise.
     */
    public boolean insertReviewImage(int reviewId, int drawableId, SQLiteDatabase db) {

        // Retrieve the drawable as a Bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
        if (bitmap == null) {
            return false;  // Return false if the bitmap is null (image not found)
        }

        // Resize the bitmap to avoid large image sizes
        Bitmap resizedBitmap = resizeBitmap(bitmap, 300, 300);  // Resize to 800x800 or any appropriate size

        // Convert the resized Bitmap to byte array
        byte[] imageData = getBitmapAsByteArray(resizedBitmap);

        // Insert the image data into the review_image table
        ContentValues values = new ContentValues();
        values.put("review_id", reviewId);
        values.put("image_data", imageData);

        long result = db.insert("review_image", null, values);
        return result != -1;  // Return true if the insert was successful, otherwise false
    }

    /**
     * Inserts an image for a business into the business_image table.
     *
     * @param busId      The ID of the business to associate the image with.
     * @param drawableId The drawable resource ID of the image.
     * @return true if the image was successfully inserted, false otherwise.
     */
    public boolean insertBusinessImage(int busId, int drawableId, SQLiteDatabase db) {
        // Retrieve the drawable as a Bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
        if (bitmap == null) {
            return false;  // Return false if the bitmap is null (image not found)
        }

        // Resize the bitmap to avoid large image sizes
        Bitmap resizedBitmap = resizeBitmap(bitmap, 600, 400);  // Resize to appropriate size

        // Convert the resized Bitmap to byte array
        byte[] imageData = getBitmapAsByteArray(resizedBitmap);

        // Insert the image data into the business_image table
        ContentValues values = new ContentValues();
        values.put("bus_id", busId);
        values.put("bus_image", imageData);

        long result = db.insert("business_image", null, values);
        return result != -1;  // Return true if the insert was successful, otherwise false
    }

    /**
     * Resizes a bitmap to the specified width and height.
     *
     * @param bitmap The original bitmap.
     * @param width  The desired width.
     * @param height The desired height.
     * @return The resized bitmap.
     */
    private Bitmap resizeBitmap(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    /**
     * This method is called when the database needs to be upgraded.
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop tables and recreate them
        db.execSQL("DROP TABLE IF EXISTS user;");
        db.execSQL("DROP TABLE IF EXISTS user_position;");
        db.execSQL("DROP TABLE IF EXISTS business;");
        db.execSQL("DROP TABLE IF EXISTS review;");
        db.execSQL("DROP TABLE IF EXISTS review_image;");
        db.execSQL("DROP TABLE IF EXISTS response;");
        db.execSQL("DROP TABLE IF EXISTS business_image;");
        onCreate(db);
    }
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    private void insertBusinessData(SQLiteDatabase db) {
        // Prepare the byte arrays for the images
        Bitmap artichokeImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.artichokeandwhitebait);
        byte[] busArtichokeImageByteArray = getBitmapAsByteArray(artichokeImage);

        Bitmap boostImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.boostjuice);
        byte[] busBoostImageByteArray = getBitmapAsByteArray(boostImage);

        Bitmap cinqueImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.cinquelirelogoblack);
        byte[] busCinqueImageByteArray = getBitmapAsByteArray(cinqueImage);

        Bitmap secularImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.churchofsecularcoffee);
        byte[] busSecularImageByteArray = getBitmapAsByteArray(secularImage);

        Bitmap coffeePointImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.coffeepoint);
        byte[] busCoffeePointImageByteArray = getBitmapAsByteArray(coffeePointImage);

        Bitmap theCountsImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.counts);
        byte[] busTheCountsImageByteArray = getBitmapAsByteArray(theCountsImage);

//        Bitmap theDenImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.theden);
//        byte[] busTheDenImageByteArray = getBitmapAsByteArray(theDenImage);

        Bitmap grafalisCafeImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.grafalislogo);
        byte[] busGrafalisCafeImageByteArray = getBitmapAsByteArray(grafalisCafeImage);

        Bitmap guzmanImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.guzmanygomez);
        byte[] busGuzmanImageByteArray = getBitmapAsByteArray(guzmanImage);

        Bitmap theHallsImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.hallscafe);
        byte[] busTheHallsImageByteArray = getBitmapAsByteArray(theHallsImage);

        Bitmap joesPizzeriaImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.joespizzeria);
        byte[] busJoesPizzeriaImageByteArray = getBitmapAsByteArray(joesPizzeriaImage);

        Bitmap maLongKitchenImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.malongkitchen);
        byte[] busMaLongKitchenImageByteArray = getBitmapAsByteArray(maLongKitchenImage);

        Bitmap monashMeetingPointImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.monashmeetingpoint);
        byte[] busMonashMeetingPointImageByteArray = getBitmapAsByteArray(monashMeetingPointImage);

        Bitmap noodleNoodleImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.noodlenoodle);
        byte[] busNoodleNoodleImageByteArray = getBitmapAsByteArray(noodleNoodleImage);

        Bitmap noodlePlusImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.noodleplus);
        byte[] busNoodlePlusImageByteArray = getBitmapAsByteArray(noodlePlusImage);

        Bitmap pappaRichImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.papparich);
        byte[] busPappaRichImageByteArray = getBitmapAsByteArray(pappaRichImage);

        Bitmap periPeriImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.periperi);
        byte[] busPeriPeriImageByteArray = getBitmapAsByteArray(periPeriImage);

        Bitmap rolldImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.rolldlogo);
        byte[] busRolldImageByteArray = getBitmapAsByteArray(rolldImage);

        Bitmap sammyImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.sammyscafelogo);
        byte[] busSammyImageByteArray = getBitmapAsByteArray(sammyImage);

        Bitmap schnitzImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.schnitzlogo);
        byte[] busSchnitzImageByteArray = getBitmapAsByteArray(schnitzImage);

        Bitmap secretGardenImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.secretgarden);
        byte[] busSecretGardenImageByteArray = getBitmapAsByteArray(secretGardenImage);

        Bitmap shareteaImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.sharetea);
        byte[] busShareteaImageByteArray = getBitmapAsByteArray(shareteaImage);

        Bitmap sirJohnsImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.sirjohns);
        byte[] busSirJohnsImageByteArray = getBitmapAsByteArray(sirJohnsImage);

        Bitmap subwayImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.subwaylogo);
        byte[] busSubwayImageByteArray = getBitmapAsByteArray(subwayImage);

        Bitmap sushiSushiImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.sushisushi);
        byte[] busSushiSushiImageByteArray = getBitmapAsByteArray(sushiSushiImage);

        Bitmap swiftsCafeImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.swiftscafe);
        byte[] busSwiftsCafeImageByteArray = getBitmapAsByteArray(swiftsCafeImage);

        Bitmap wholefoodsImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.wholefoods);
        byte[] busWholefoodsImageByteArray = getBitmapAsByteArray(wholefoodsImage);

        // Insert data for 'Artichoke & Whitebait' with an image
        ContentValues values = new ContentValues();
        values.put("bus_name", "Artichoke & Whitebait");
        values.put("bus_addr", "Ground Floor, Campus Centre, 21 Chancellors Walk, Clayton campus");
        values.put("bus_ph_nb", "95432523");
        values.put("bus_email", "artichokewhitebait@gmail.com");
        values.put("website_url", "artichokeandwhitebait.com.au");
        values.put("bus_hours", "Mon–Fri: 7am – 5pm");
        values.put("bus_cuisine_type", "Australian");
        values.put("bus_image", busArtichokeImageByteArray);  // Inserting the image byte array
        db.insert("business", null, values);

        // Insert data for 'Boost Juice Clayton' with an image
        values.clear();
        values.put("bus_name", "Boost Juice Clayton");
        values.put("bus_addr", "Northern Plaza, Campus Centre, 21 Chancellors Walk, Clayton campus");
        values.put("website_url", "boostjuice.com.au");
        values.put("bus_hours", "Mon–Thu: 8am – 6pm, Fri: 8am – 5.30pm, Sat–Sun: 12–4pm");
        values.put("bus_cuisine_type", "Beverage");
        values.put("bus_image", busBoostImageByteArray);  // Inserting the image byte array
        db.insert("business", null, values);

        // Insert data for 'Café Cinque Lire' without an image
        values.clear();
        values.put("bus_name", "Café Cinque Lire");
        values.put("bus_addr", "The Strip, 15 Innovation Walk, Clayton campus");
        values.put("bus_ph_nb", "954007780");
        values.put("bus_email", "cinqueliremonash@yahoo.com.au");
        values.put("bus_hours", "Mon–Thu: 6.30am – 5pm, Fri: 6.30am – 6.30pm");
        values.put("bus_cuisine_type", "Italian");
        values.put("bus_image", busCinqueImageByteArray);  // No image
        db.insert("business", null, values);

        // Insert data for 'Church of Secular Coffee' without an image
        values.clear();
        values.put("bus_name", "Church of Secular Coffee");
        values.put("bus_addr", "32 Exhibition Walk, Clayton campus");
        values.put("bus_ph_nb", "99050888");
        values.put("bus_email", "churchofsecularcoffe@monash.edu");
        values.put("bus_hours", "Mon–Fri: 8am – 3pm");
        values.put("bus_cuisine_type", "Cafe");
        values.put("bus_image", busSecularImageByteArray);  // No image
        db.insert("business", null, values);

        // Insert data for 'Coffee Point' without an image
        values.clear();
        values.put("bus_name", "Coffee Point");
        values.put("bus_addr", "Ground Floor, Campus Centre, 21 Chancellors Walk, Clayton campus");
        values.put("bus_ph_nb", "99055714");
        values.put("bus_email", "cater@monashcatering.com.au");
        values.put("website_url", "www.monashcateringonline.com.au");
        values.put("bus_hours", "Mon–Fri: 7:30am – 5pm");
        values.put("bus_cuisine_type", "Cafe");
        values.put("bus_image", busCoffeePointImageByteArray);  // No image
        db.insert("business", null, values);

        // Insert data for 'The Count's' without an image
        values.clear();
        values.put("bus_name", "The Count's");
        values.put("bus_addr", "The Ian Potter Centre for Performing Arts, 48 Exhibition Walk, Clayton campus");
        values.put("bus_ph_nb", "90686150");
        values.put("bus_email", "enquiries@thecounts.com.au");
        values.put("website_url", "www.monash.edu/performing-arts-centres/eat-drink-clayton-campus");
        values.put("bus_hours", "Mon–Fri: 11am – 3pm");
        values.put("bus_cuisine_type", "Cafe");
        values.put("bus_image", busTheCountsImageByteArray);  // No image
        db.insert("business", null, values);

        // Insert data for 'The Den' without an image
        values.clear();
        values.put("bus_name", "The Den");
        values.put("bus_addr", "Cellar West Room, Campus Centre, 21 Chancellor's Walk, Clayton campus");
        values.put("bus_ph_nb", "95446611");
        values.putNull("bus_image");  // No image
        db.insert("business", null, values);

        // Insert data for 'Grafalis Cafe' without an image
        values.clear();
        values.put("bus_name", "Grafalis Cafe");
        values.put("bus_addr", "Ground Floor, Campus Centre, 21 Chancellors Walk, Clayton campus");
        values.put("bus_ph_nb", "0406278975");
        values.put("bus_email", "grafalismonash@gmail.com");
        values.put("bus_hours", "Mon–Fri: 7:30am – 5:30pm");
        values.put("bus_cuisine_type", "Cafe");
        values.put("bus_image", busGrafalisCafeImageByteArray);   // No image
        db.insert("business", null, values);

        // Insert data for 'Guzman y Gomez' without an image
        values.clear();
        values.put("bus_name", "Guzman y Gomez");
        values.put("bus_addr", "Ground Floor, Campus Centre, Clayton campus");
        values.put("bus_ph_nb", "99881409");
        values.put("bus_email", "monash@gyg.com.au");
        values.put("website_url", "guzmanygomez.com");
        values.put("bus_hours", "Mon–Sat: 8am – 10pm, Sun: 9am - 10pm");
        values.put("bus_cuisine_type", "Mexican");
        values.put("bus_image", busGuzmanImageByteArray);  // No image
        db.insert("business", null, values);

        // Insert data for 'The Halls Café' without an image
        values.clear();
        values.put("bus_name", "The Halls Café");
        values.put("bus_addr", "Halls of Residence, 58 College Walk, Clayton campus");
        values.put("bus_ph_nb", "99056498");
        values.put("bus_email", "mrs.rsto@monash.edu");
        values.put("website_url", "mrs.monash.edu.au/oncampus/operations/cafe.html");
        values.put("bus_hours", "Mon-Fri: 12pm - 2:30pm");
        values.put("bus_cuisine_type", "Cafe");
        values.put("bus_image", busTheHallsImageByteArray);  // No image
        db.insert("business", null, values);

        // Insert data for 'Joe's Pizzeria' without an image
        values.clear();
        values.put("bus_name", "Joe's Pizzeria");
        values.put("bus_addr", "28 Sports Walk, Logan Hall, Clayton campus");
        values.put("bus_ph_nb", "95586546");
        values.put("bus_email", "joesloganhall@gmail.com");
        values.put("website_url", "www.joesloganhall.com.au");
        values.put("bus_hours", "Mon–Fri: 11am – 9pm, Sat–Sun: 12pm – 8pm");
        values.put("bus_cuisine_type", "Italian");
        values.put("bus_image", busJoesPizzeriaImageByteArray);  // No image
        db.insert("business", null, values);

        // Insert data for 'Ma Long Kitchen and Dumplings' without an image
        values.clear();
        values.put("bus_name", "Ma Long Kitchen and Dumplings");
        values.put("bus_addr", "Ground Floor, Campus Centre, 21 Chancellors Walk, Clayton campus");
        values.put("bus_ph_nb", "95487173");
        values.put("bus_email", "info@malongkitchen.com.au");
        values.put("website_url", "www.malongkitchen.com.au");
        values.put("bus_hours", "Mon–Fri: 9.30am – 6pm");
        values.put("bus_cuisine_type", "Chinese");
        values.put("bus_image", busMaLongKitchenImageByteArray);  // No image
        db.insert("business", null, values);

        // Continue inserting all the remaining businesses similarly...
        // Continue inserting all the remaining businesses
        // Insert data for 'Noodle Noodle' without an image
        values.clear();
        values.put("bus_name", "Noodle Noodle");
        values.put("bus_addr", "Ground Floor, Campus Centre, 21 Chancellors Walk, Clayton campus");
        values.put("bus_ph_nb", "99058058");
        values.putNull("bus_email");
        values.putNull("website_url");
        values.put("bus_hours", "Mon–Fri: 8am – 7pm");
        values.put("bus_cuisine_type", "Chinese");
        values.put("bus_image", busNoodleNoodleImageByteArray);   // No image
        db.insert("business", null, values);

        // Insert data for 'Noodle Plus' without an image
        values.clear();
        values.put("bus_name", "Noodle Plus");
        values.put("bus_addr", "Ground Floor, Learning and Teaching Building");
        values.putNull("bus_ph_nb");
        values.putNull("bus_email");
        values.putNull("website_url");
        values.put("bus_hours", "Mon–Tue: 10am – 6.30pm");
        values.put("bus_cuisine_type", "Chinese");
        values.put("bus_image", busNoodlePlusImageByteArray); // No image
        db.insert("business", null, values);

        // Insert data for 'PappaRich' without an image
        values.clear();
        values.put("bus_name", "PappaRich");
        values.put("bus_addr", "Ground Floor, Campus Centre, 21 Chancellors Walk");
        values.put("bus_ph_nb", "96454667");
        values.putNull("bus_email");
        values.put("website_url", "papparich.net.au");
        values.put("bus_hours", "Mon–Sun: 10am – 7pm");
        values.put("bus_cuisine_type", "Malaysian");
        values.put("bus_image", busPappaRichImageByteArray);  // No image
        db.insert("business", null, values);

        // Insert data for 'Peri Peri' without an image
        values.clear();
        values.put("bus_name", "Peri Peri");
        values.put("bus_addr", "Ground Floor, Campus Centre, 21 Chancellors Walk, Clayton campus");
        values.put("bus_ph_nb", "99055714");
        values.put("bus_email", "cater@monashcatering.com.au");
        values.put("website_url", "www.monashcateringonline.com.au");
        values.put("bus_hours", "Mon–Fri: 8:30am – 5:30pm");
        values.put("bus_cuisine_type", "Portugese");
        values.put("bus_image", busPeriPeriImageByteArray);  // No image
        db.insert("business", null, values);

        // Insert data for 'Roll'd' without an image
        values.clear();
        values.put("bus_name", "Roll'd");
        values.put("bus_addr", "Ground Floor, Campus Centre, 21 Chancellors Walk, Clayton campus");
        values.putNull("bus_ph_nb");
        values.put("bus_email", "info@rolld.com.au");
        values.put("website_url", "rolld.com.au");
        values.put("bus_hours", "Mon–Fri: 8am – 3pm");
        values.put("bus_cuisine_type", "Vietnamese");
        values.put("bus_image", busRolldImageByteArray);  // No image
        db.insert("business", null, values);

        // Insert data for 'Sammy's (Clayton)' without an image
        values.clear();
        values.put("bus_name", "Sammy's (Clayton)");
        values.put("bus_addr", "Ground Floor, Monash Sport, 42 Scenic Boulevard, Clayton campus");
        values.putNull("bus_ph_nb");
        values.putNull("bus_email");
        values.putNull("website_url");
        values.put("bus_hours", "Mon–Fri: 8am – 3pm, Sat: 8am – 1pm");
        values.put("bus_cuisine_type", "Cafe");
        values.put("bus_image", busSammyImageByteArray);  // No image
        db.insert("business", null, values);

        // Insert data for 'Schnitz' without an image
        values.clear();
        values.put("bus_name", "Schnitz");
        values.put("bus_addr", "28 Sports Walk, Clayton campus");
        values.put("bus_ph_nb", "95435609");
        values.put("bus_email", "feedback@schnitz.com.au");
        values.put("website_url", "schnitz.com.au");
        values.put("bus_hours", "Mon–Sun: 10am – 9pm");
        values.put("bus_cuisine_type", "German");
        values.put("bus_image", busSchnitzImageByteArray);  // No image
        db.insert("business", null, values);

        // Insert data for 'Secret Garden Eatery' without an image
        values.clear();
        values.put("bus_name", "Secret Garden Eatery");
        values.put("bus_addr", "13 College Walk, Andrew Hargrave Library, Clayton campus");
        values.put("bus_ph_nb", "95487362");
        values.put("bus_email", "info@secretgardeneatery.com.au");
        values.put("website_url", "secretgardeneatery.com.au");
        values.put("bus_hours", "Mon-Fri: 7am - 4pm");
        values.put("bus_cuisine_type", "Cafe");
        values.put("bus_image", busSecretGardenImageByteArray);  // No image
        db.insert("business", null, values);

        // Insert data for 'Sharetea' without an image
        values.clear();
        values.put("bus_name", "Sharetea");
        values.put("bus_addr", "32 Exhibition Walk, Clayton campus");
        values.putNull("bus_ph_nb");
        values.put("bus_email", "marketing@sharetea.com.au");
        values.putNull("website_url");
        values.put("bus_hours", "Mon–Fri: 10:30am – 6pm");
        values.put("bus_cuisine_type", "Beverage");
        values.put("bus_image", busShareteaImageByteArray);   // No image
        db.insert("business", null, values);

        // Insert data for 'Sir John's' without an image
        values.clear();
        values.put("bus_name", "Sir John's");
        values.put("bus_addr", "Level 1, Campus Centre, 21 Chancellors Walk, Clayton campus");
        values.put("bus_ph_nb", "99053035");
        values.put("bus_email", "msa-sirjohns@monash.edu");
        values.put("website_url", "www.sirjohnsbar.com");
        values.put("bus_hours", "Mon: 12am – 5pm, Tue–Thu: 12am – 7pm, Fri: 12am – 5pm");
        values.put("bus_cuisine_type", "Bar");
        values.put("bus_image", busSirJohnsImageByteArray);   // No image
        db.insert("business", null, values);

        // Insert data for 'Subway' without an image
        values.clear();
        values.put("bus_name", "Subway");
        values.put("bus_addr", "Ground Floor, Campus Centre, 21 Chancellors Walk, Clayton campus");
        values.put("bus_ph_nb", "85540500");
        values.put("bus_email", "monashuni@subcatering.com.au");
        values.put("website_url", "www.subway.com.au");
        values.put("bus_hours", "Mon–Fri: 9am – 5pm");
        values.put("bus_cuisine_type", "American");
        values.put("bus_image", busSubwayImageByteArray);   // No image
        db.insert("business", null, values);

        // Insert data for 'Sushi Sushi' without an image
        values.clear();
        values.put("bus_name", "Sushi Sushi");
        values.put("bus_addr", "Ground Floor, Campus Centre, 21 Chancellors Walk, Clayton campus");
        values.putNull("bus_ph_nb");
        values.put("bus_email", "info@sushisushi.com.au");
        values.put("website_url", "sushisushi.com.au");
        values.put("bus_hours", "Mon–Fri: 8am – 6pm");
        values.put("bus_cuisine_type", "Japanese");
        values.put("bus_image", busSushiSushiImageByteArray);   // No image
        db.insert("business", null, values);

        // Insert data for 'Swift's Café' without an image
        values.clear();
        values.put("bus_name", "Swift's Café");
        values.put("bus_addr", "Matheson Library, 40 Exhibition Walk, Clayton campus");
        values.put("bus_ph_nb", "90686150");
        values.put("bus_email", "enquiries@thecounts.com.au");
        values.putNull("website_url");
        values.put("bus_hours", "Mon–Fri: 8am – 4pm, Sat-Sun: 11am - 2pm");
        values.put("bus_cuisine_type", "Cafe");
        values.put("bus_image", busSwiftsCafeImageByteArray);  // No image
        db.insert("business", null, values);

        // Insert data for 'Wholefoods' without an image
        values.clear();
        values.put("bus_name", "Wholefoods");
        values.put("bus_addr", "Level 1, Campus Centre, 21 Chancellors Walk, Clayton campus");
        values.put("bus_ph_nb", "99024350");
        values.put("bus_email", "msa-wholefoods@monash.edu");
        values.put("website_url", "monashwholefoods.org");
        values.put("bus_hours", "Cafe: Mon–Fri: 9am – 4pm, Kitchen: Mon–Fri: 11:30am – 2:30pm");
        values.put("bus_cuisine_type", "Vegetarian");
        values.put("bus_image", busWholefoodsImageByteArray);  // No image
        db.insert("business", null, values);
    }

    private void insertUserData(SQLiteDatabase db) {
        // Prepare the byte arrays for the user profile images
        Bitmap userProfileImage1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.user1);
        byte[] userProfileImageByteArray1 = getBitmapAsByteArray(userProfileImage1);

        Bitmap userProfileImage2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.user2);
        byte[] userProfileImageByteArray2 = getBitmapAsByteArray(userProfileImage2);

        Bitmap userProfileImage3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.user3);
        byte[] userProfileImageByteArray3 = getBitmapAsByteArray(userProfileImage3);

        Bitmap userProfileImage4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.user4);
        byte[] userProfileImageByteArray4 = getBitmapAsByteArray(userProfileImage4);

        Bitmap userProfileImage5 = BitmapFactory.decodeResource(context.getResources(), R.drawable.user5);
        byte[] userProfileImageByteArray5 = getBitmapAsByteArray(userProfileImage5);

        Bitmap userProfileImage6 = BitmapFactory.decodeResource(context.getResources(), R.drawable.user6);
        byte[] userProfileImageByteArray6 = getBitmapAsByteArray(userProfileImage6);

        Bitmap userProfileImage7 = BitmapFactory.decodeResource(context.getResources(), R.drawable.user7);
        byte[] userProfileImageByteArray7 = getBitmapAsByteArray(userProfileImage7);

        Bitmap userProfileImage8 = BitmapFactory.decodeResource(context.getResources(), R.drawable.user8);
        byte[] userProfileImageByteArray8 = getBitmapAsByteArray(userProfileImage8);

        Bitmap userProfileImage9 = BitmapFactory.decodeResource(context.getResources(), R.drawable.user9);
        byte[] userProfileImageByteArray9 = getBitmapAsByteArray(userProfileImage9);

        Bitmap userProfileImage10 = BitmapFactory.decodeResource(context.getResources(), R.drawable.user10);
        byte[] userProfileImageByteArray10 = getBitmapAsByteArray(userProfileImage10);

        // Define a common password to hash
        String commonPassword = "password123";

        // Create ContentValues for each user and insert them into the database
        ContentValues values = new ContentValues();

        // Insert data for User 1
        String hashedPassword1 = Password.hash(commonPassword).withScrypt().getResult();
        values.put("username", "jdoe001");
        values.put("password", hashedPassword1);
        values.put("email", "jdoe001@student.monash.edu");
        values.put("first_name", "John");
        values.put("last_name", "Doe");
        values.putNull("position_id");  // Set position_id as null if not applicable
        values.put("profile_image", userProfileImageByteArray1);
        db.insert("user", null, values);
        values.clear();

        // Insert data for User 2
        String hashedPassword2 = Password.hash(commonPassword).withScrypt().getResult();
        values.put("username", "asmith002");
        values.put("password", hashedPassword2);
        values.put("email", "asmith002@student.monash.edu");
        values.put("first_name", "Alice");
        values.put("last_name", "Smith");
        values.putNull("position_id");
        values.put("profile_image", userProfileImageByteArray2);
        db.insert("user", null, values);
        values.clear();

        // Insert data for User 3
        String hashedPassword3 = Password.hash(commonPassword).withScrypt().getResult();
        values.put("username", "bwong003");
        values.put("password", hashedPassword3);
        values.put("email", "bwong003@student.monash.edu");
        values.put("first_name", "Ben");
        values.put("last_name", "Wong");
        values.putNull("position_id");
        values.put("profile_image", userProfileImageByteArray3);
        db.insert("user", null, values);
        values.clear();

        // Insert data for User 4
        String hashedPassword4 = Password.hash(commonPassword).withScrypt().getResult();
        values.put("username", "charris004");
        values.put("password", hashedPassword4);
        values.put("email", "charris004@student.monash.edu");
        values.put("first_name", "Carol");
        values.put("last_name", "Harris");
        values.putNull("position_id");
        values.put("profile_image", userProfileImageByteArray4);
        db.insert("user", null, values);
        values.clear();

        // Insert data for User 5
        String hashedPassword5 = Password.hash(commonPassword).withScrypt().getResult();
        values.put("username", "dlee005");
        values.put("password", hashedPassword5);
        values.put("email", "dlee005@student.monash.edu");
        values.put("first_name", "David");
        values.put("last_name", "Lee");
        values.putNull("position_id");
        values.put("profile_image", userProfileImageByteArray5);
        db.insert("user", null, values);
        values.clear();

        // Insert data for User 6
        String hashedPassword6 = Password.hash(commonPassword).withScrypt().getResult();
        values.put("username", "emma006");
        values.put("password", hashedPassword6);
        values.put("email", "emma006@student.monash.edu");
        values.put("first_name", "Emma");
        values.put("last_name", "Brown");
        values.putNull("position_id");
        values.put("profile_image", userProfileImageByteArray6);
        db.insert("user", null, values);
        values.clear();

        // Insert data for User 7
        String hashedPassword7 = Password.hash(commonPassword).withScrypt().getResult();
        values.put("username", "fclark007");
        values.put("password", hashedPassword7);
        values.put("email", "fclark007@student.monash.edu");
        values.put("first_name", "Frank");
        values.put("last_name", "Clark");
        values.putNull("position_id");
        values.put("profile_image", userProfileImageByteArray7);
        db.insert("user", null, values);
        values.clear();

        // Insert data for User 8
        String hashedPassword8 = Password.hash(commonPassword).withScrypt().getResult();
        values.put("username", "gwhite008");
        values.put("password", hashedPassword8);
        values.put("email", "gwhite008@student.monash.edu");
        values.put("first_name", "Grace");
        values.put("last_name", "White");
        values.putNull("position_id");
        values.put("profile_image", userProfileImageByteArray8);
        db.insert("user", null, values);
        values.clear();

        // Insert data for User 9
        String hashedPassword9 = Password.hash(commonPassword).withScrypt().getResult();
        values.put("username", "hking009");
        values.put("password", hashedPassword9);
        values.put("email", "hking009@student.monash.edu");
        values.put("first_name", "Henry");
        values.put("last_name", "King");
        values.putNull("position_id");
        values.put("profile_image", userProfileImageByteArray9);
        db.insert("user", null, values);
        values.clear();

        // Insert data for User 10
        String hashedPassword10 = Password.hash(commonPassword).withScrypt().getResult();
        values.put("username", "ijones010");
        values.put("password", hashedPassword10);
        values.put("email", "ijones010@student.monash.edu");
        values.put("first_name", "Irene");
        values.put("last_name", "Jones");
        values.putNull("position_id");
        values.put("profile_image", userProfileImageByteArray10);
        db.insert("user", null, values);
        values.clear();
    }




}
