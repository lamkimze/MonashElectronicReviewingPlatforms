package com.example.myapplication.Database;

import static java.lang.String.format;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.myapplication.Restaurant;
import com.password4j.Password;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
//    increment the version number if you change the schema
    private static final int DATABASE_VERSION = 16;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME,  null, DATABASE_VERSION);
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
                "bus_name TEXT NOT NULL, " +
                "bus_addr TEXT NOT NULL, " +
                "bus_ph_nb TEXT, " +
                "bus_email TEXT, " +
                "website_url TEXT, " +
                "bus_hours TEXT, " +
                "bus_cuisine_type TEXT, " +
                "bus_image BLOB);";
        db.execSQL(createBusinessTable);
        // Create review table
        String createReviewTable = "CREATE TABLE review (" +
                "review_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "bus_id INTEGER NOT NULL, " +
                "star_rating DECIMAL(1,2) NOT NULL, " +
                "review_title TEXT, " +
                "review_text TEXT, " +
                "review_date DATE DEFAULT (datetime('now','localtime')), " +
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

        // Insert initial data
        String insertBusinessData = "INSERT INTO business (bus_name, bus_addr, bus_ph_nb, bus_email, website_url, bus_hours, bus_cuisine_type) VALUES\n" +
                "('Artichoke & Whitebait', 'Ground Floor, Campus Centre, 21 Chancellors Walk, Clayton campus', '95432523', 'artichokewhitebait@gmail.com', 'artichokeandwhitebait.com.au', 'Mon–Fri: 7am – 5pm', 'Australian'),\n" +
                "('Boost Juice Clayton', 'Northern Plaza, Campus Centre, 21 Chancellors Walk, Clayton campus', NULL, NULL, 'boostjuice.com.au', 'Mon–Thu: 8am – 6pm,Fri: 8am – 5.30pm,Sat–Sun: 12–4pm', 'Beverage'),\n" +
                "('Café Cinque Lire', 'The Strip, 15 Innovation Walk, Clayton campus', '954007780', 'cinqueliremonash@yahoo.com.au', NULL, 'Mon–Thu: 6.30am – 5pm, Fri: 6.30am – 6.30pm', 'Italian'),\n" +
                "('Church of Secular Coffee', '32 Exhibition Walk, Clayton campus', '99050888', 'churchofsecularcoffe@monash.edu', NULL, 'Mon–Fri: 8am – 3pm', 'Cafe'),\n" +
                "('Coffee Point', 'Ground Floor, Campus Centre, 21 Chancellors Walk, Clayton campus', '99055714', 'cater@monashcatering.com.au', 'www.monashcateringonline.com.au', 'Mon–Fri: 7:30am – 5pm', 'Cafe'),\n" +
                "('The Count''s', 'The Ian Potter Centre for Performing Arts, 48 Exhibition Walk, Clayton campus', '90686150', 'enquiries@thecounts.com.au', 'www.monash.edu/performing-arts-centres/eat-drink-clayton-campus', 'Mon–Fri: 11am – 3pm', 'Cafe'),\n" +
                "('The Den', 'Cellar West Room, Campus Centre, 21 Chancellor''s Walk, Clayton campus', '95446611', NULL, NULL, 'Mon–Fri: 7:30am – 6:30pm', 'Cafe'),\n" +
                "('Grafalis Cafe', 'Ground Floor, Campus Centre, 21 Chancellors Walk, Clayton campus', '0406278975', 'grafalismonash@gmail.com', NULL, 'Mon–Fri: 7:30am – 5:30pm', 'Cafe'),\n" +
                "('Guzman y Gomez', 'Ground Floor, Campus Centre, Clayton campus', '99881409', 'monash@gyg.com.au', 'guzmanygomez.com', 'Mon–Sat: 8am – 10pm, Sun: 9am - 10pm', 'Mexican'),\n" +
                "('The Halls Café', 'Halls of Residence, 58 College Walk, Clayton campus', '99056498', 'mrs.rsto@monash.edu', 'mrs.monash.edu.au/oncampus/operations/cafe.html', 'Mon-Fri: 12pm - 2:30pm', 'Cafe'),\n" +
                "('Joe''s Pizzeria', '28 Sports Walk, Logan Hall, Clayton campus', '95586546', 'joesloganhall@gmail.com', 'www.joesloganhall.com.au', 'Mon–Fri: 11am – 9pm, Sat–Sun: 12pm – 8pm', 'Italian'),\n" +
                "('Ma Long Kitchen and Dumplings', 'Ma Long Kitchen and Dumplings', '95487173', 'info@malongkitchen.com.au', 'www.malongkitchen.com.au', 'Mon–Fri: 9.30am – 6pm', 'Chinese'),\n" +
                "('Monash Meeting Point', 'Ground Floor, Campus Centre, 21 Chancellors Walk, Clayton campus', '99055714', 'cater@monashcatering.com.au', 'www.monashcateringonline.com.au', 'Mon–Fri: 7:30am – 5pm', 'International'),\n" +
                "('The Monash Merchant', '28 Sports Walk, Clayton campus', '99052292 / 99052289', 'supermarket@monash.edu', NULL, 'Mon–Fri: 9am – 9:30pm, Sat–Sun: 11am – 6pm', 'Convenience Store'),\n" +
                "('Neptune''s Seafood Catch', 'Ground Floor, Campus Centre, 21 Chancellors Walk, Clayton Campus', '0421942161', 'larry.ma.catering.group@gmail.com', NULL, 'Mon-Fri: 8am - 7pm', 'British'),\n" +
                "('Nesso Café', 'Ground Floor, Learning and Teaching Building', '0430210102', 'nessocatering@gmail.com', 'www.cafenessoltb.com.au', 'Mon–Fri: 7am – 5:30pm', 'Cafe'),\n" +
                "('Noodle Noodle', 'Ground Floor, Campus Centre, 21 Chancellors Walk, Clayton campus', '99058058', NULL, NULL, 'Mon–Fri: 8am – 7pm', 'Chinese'),\n" +
                "('Noodle Plus', 'Ground Floor, Learning and Teaching Building', NULL, NULL, NULL, 'Mon–Tue: 10am – 6.30pm', 'Chinese'),\n" +
                "('PappaRich', 'Ground Floor, Campus Centre, 21 Chancellors Walk', '96454667', NULL, 'papparich.net.au', 'Mon–Sun: 10am – 7pm', 'Malaysian'),\n" +
                "('Peri Peri', 'Ground Floor, Campus Centre, 21 Chancellors Walk, Clayton campus', '99055714', 'cater@monashcatering.com.au', 'www.monashcateringonline.com.au', 'Mon–Fri: 8:30am – 5:30pm', 'Portugese'),\n" +
                "('Roll''d', 'Ground Floor, Campus Centre, 21 Chancellors Walk, Clayton campus', NULL, 'info@rolld.com.au', 'rolld.com.au', 'Mon–Fri: 8am – 3pm', 'Vietnamese'),\n" +
                "('Sammy''s (Clayton)', 'Ground Floor, Monash Sport, 42 Scenic Boulevard, Clayton campus', NULL, NULL, NULL, 'Mon–Fri: 8am – 3pm, Sat: 8am – 1pm', 'Cafe'),\n" +
                "('Schnitz', '28 Sports Walk, Clayton campus', '95435609', 'feedback@schnitz.com.au', 'schnitz.com.au', 'Mon–Sun: 10am – 9pm', 'German'),\n" +
                "('Secret Garden Eatery', '13 College Walk, Andrew Hargrave Library, Clayton campus', '95487362', 'info@secretgardeneatery.com.au', 'secretgardeneatery.com.au', 'Mon-Fri: 7am - 4pm', 'Cafe'),\n" +
                "('Sharetea', '32 Exhibition Walk, Clayton campus', NULL, 'marketing@sharetea.com.au', NULL, 'Mon–Fri: 10:30am – 6pm', 'Beverage'),\n" +
                "('Sir John''s', 'Level 1, Campus Centre, 21 Chancellors Walk, Clayton campus', '99053035', 'msa-sirjohns@monash.edu', 'www.sirjohnsbar.com', 'Mon: 12am – 5pm, Tue–Thu: 12am – 7pm, Fri: 12am – 5pm', 'Bar'),\n" +
                "('Subway', 'Ground Floor, Campus Centre, 21 Chancellors Walk, Clayton campus', '85540500', 'monashuni@subcatering.com.au', 'www.subway.com.au', 'Mon–Fri: 9am – 5pm', 'American'),\n" +
                "('Sushi Sushi', 'Ground Floor, Campus Centre, 21 Chancellors Walk, Clayton campus', NULL, 'info@sushisushi.com.au', 'sushisushi.com.au', 'Mon–Fri: 8am – 6pm', 'Japanese'),\n" +
                "('Swift''s Café', 'Matheson Library, 40 Exhibition Walk, Clayton campus', '90686150', 'enquiries@thecounts.com.au', NULL, 'Mon–Fri: 8am – 4pm, Sat-Sun: 11am - 2pm', 'Cafe'),\n" +
                "('Wholefoods', 'Level 1, Campus Centre, 21 Chancellors Walk, Clayton campus', '99024350', 'msa-wholefoods@monash.edu', 'monashwholefoods.org', 'Cafe: Mon–Fri: 9am – 4pm, Kitchen: Mon–Fri: 11:30am – 2:30pm', 'Vegetarian');";
        db.execSQL(insertBusinessData);

        String hashedPassword = Password.hash("password123").withScrypt().getResult();
        String insertCustomerData = format("INSERT INTO user (username, password, email, first_name, last_name) VALUES\n" +
                "('jdoe001', '%s', 'jdoe001@student.monash.edu', 'John', 'Doe'),\n" +
                "('asmith002', '%s', 'asmith002@student.monash.edu', 'Alice', 'Smith'),\n" +
                "('bwong003', '%s', 'bwong003@student.monash.edu', 'Ben', 'Wong'),\n" +
                "('charris004', '%s', 'charris004@student.monash.edu', 'Carol', 'Harris'),\n" +
                "('dlee005', '%s', 'dlee005@student.monash.edu', 'David', 'Lee'),\n" +
                "('emma006', '%s', 'emma006@student.monash.edu', 'Emma', 'Brown'),\n" +
                "('fclark007', '%s', 'fclark007@student.monash.edu', 'Frank', 'Clark'),\n" +
                "('gwhite008', '%s', 'gwhite008@student.monash.edu', 'Grace', 'White'),\n" +
                "('hking009', '%s', 'hking009@student.monash.edu', 'Henry', 'King'),\n" +
                "('ijones010', '%s', 'ijones010@student.monash.edu', 'Irene', 'Jones');\n",
                hashedPassword,
                hashedPassword,
                hashedPassword,
                hashedPassword,
                hashedPassword,
                hashedPassword,
                hashedPassword,
                hashedPassword,
                hashedPassword,
                hashedPassword
                );
        db.execSQL(insertCustomerData);



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
        onCreate(db);
    }


}
