package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
//    increment the version number if you change the schema
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        Create our tables
        String createCustomerTable = "CREATE TABLE customer (user_id INTEGER PRIMARY KEY AUTOINCREMENT,username TEXT NOT NULL,passwrd TEXT NOT NULL,email TEXT NOT NULL,first_name TEXT NOT NULL,last_name TEXT NOT NULL,created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";
        db.execSQL(createCustomerTable);

        String createOwnerTable = "CREATE TABLE owner (owner_id INTEGER PRIMARY KEY AUTOINCREMENT,username TEXT NOT NULL,passwrd TEXT NOT NULL,email TEXT NOT NULL,first_name TEXT NOT NULL,last_name TEXT NOT NULL,created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";
        db.execSQL(createOwnerTable);

        String createBusinessTable = "CREATE TABLE business (bus_id INTEGER PRIMARY KEY AUTOINCREMENT,bus_name TEXT NOT NULL,bus_addr TEXT NOT NULL,bus_ph_nb TEXT,bus_email TEXT,website_url TEXT,owner_id INTEGER,bus_hours TEXT,bus_cuisine_type TEXT,FOREIGN KEY (owner_id) REFERENCES owner(owner_id));";
        db.execSQL(createBusinessTable);

        String createReviewTable = "CREATE TABLE review (review_id INTEGER PRIMARY KEY AUTOINCREMENT,user_id INTEGER NOT NULL,bus_id INTEGER NOT NULL,star_rating INTEGER NOT NULL,review_text TEXT,review_date DATE,FOREIGN KEY (user_id) REFERENCES customer(user_id),FOREIGN KEY (bus_id) REFERENCES business(bus_id));";
        db.execSQL(createReviewTable);

        String createImageTable = "CREATE TABLE image (image_id INTEGER PRIMARY KEY AUTOINCREMENT,review_id INTEGER NOT NULL,business_id INTEGER NOT NULL,image_url TEXT NOT NULL,FOREIGN KEY (review_id) REFERENCES review(review_id),FOREIGN KEY (business_id) REFERENCES business(bus_id));";
        db.execSQL(createImageTable);

        String createReviewImageTable = "CREATE TABLE review_image (review_id INTEGER NOT NULL,image_id INTEGER NOT NULL,PRIMARY KEY (review_id, image_id),FOREIGN KEY (review_id) REFERENCES review(review_id),FOREIGN KEY (image_id) REFERENCES image(image_id));";
        db.execSQL(createReviewImageTable);

        String createBusinessImageTable = "CREATE TABLE business_image (bus_id INTEGER NOT NULL,image_id INTEGER NOT NULL,PRIMARY KEY (bus_id, image_id),FOREIGN KEY (bus_id) REFERENCES business(bus_id),FOREIGN KEY (image_id) REFERENCES image(image_id));";
        db.execSQL(createBusinessImageTable);

        String createResponseTable = "CREATE TABLE response (response_id INTEGER PRIMARY KEY AUTOINCREMENT,review_id INTEGER,user_id INTEGER,response_text TEXT,response_date DATE,owner_id INTEGER,FOREIGN KEY (review_id) REFERENCES review(review_id),FOREIGN KEY (user_id) REFERENCES customer(user_id),FOREIGN KEY (owner_id) REFERENCES owner(owner_id));";
        db.execSQL(createResponseTable);

        // Insert initial data
        String insertBusinessData = "INSERT INTO business (bus_name, bus_addr, bus_ph_nb, bus_email, website_url, bus_hours, bus_cuisine_type) VALUES\n" +
                "('Artichoke & Whitebait', 'Ground Floor, Campus Centre, 21 Chancellors Walk, Clayton campus', '95432523', 'artichokewhitebait@gmail.com', 'artichokeandwhitebait.com.au', 'Mon–Fri: 7am – 5pm', 'Australian'),\n" +
                "('Boost Juice Clayton', 'Northern Plaza, Campus Centre, 21 Chancellors Walk, Clayton campus', NULL, NULL, 'boostjuice.com.au', 'Mon–Thu: 8am – 6pm,Fri: 8am – 5.30pm,Sat–Sun: 12–4pm', 'Juice'),\n" +
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
                "('Noodle Noodle', 'Ground Floor, Campus Centre, 21 Chancellors Walk, Clayton campus', '99058058', NULL, NULL, 'Mon–Fri: 8am – 7pm', 'Asian'),\n" +
                "('Noodle Plus', 'Ground Floor, Learning and Teaching Building', NULL, NULL, NULL, 'Mon–Tue: 10am – 6.30pm', 'Asian'),\n" +
                "('PappaRich', 'Ground Floor, Campus Centre, 21 Chancellors Walk', '96454667', NULL, 'papparich.net.au', 'Mon–Sun: 10am – 7pm', 'Malaysian'),\n" +
                "('Peri Peri', 'Ground Floor, Campus Centre, 21 Chancellors Walk, Clayton campus', '99055714', 'cater@monashcatering.com.au', 'www.monashcateringonline.com.au', 'Mon–Fri: 8:30am – 5:30pm', 'Portugese'),\n" +
                "('Roll''d', 'Ground Floor, Campus Centre, 21 Chancellors Walk, Clayton campus', NULL, 'info@rolld.com.au', 'rolld.com.au', 'Mon–Fri: 8am – 3pm', 'Vietnamese'),\n" +
                "('Sammy''s (Clayton)', 'Ground Floor, Monash Sport, 42 Scenic Boulevard, Clayton campus', NULL, NULL, NULL, 'Mon–Fri: 8am – 3pm, Sat: 8am – 1pm', 'Cafe'),\n" +
                "('Schnitz', '28 Sports Walk, Clayton campus', '95435609', 'feedback@schnitz.com.au', 'schnitz.com.au', 'Mon–Sun: 10am – 9pm', 'German'),\n" +
                "('Secret Garden Eatery', '13 College Walk, Andrew Hargrave Library, Clayton campus', '95487362', 'info@secretgardeneatery.com.au', 'secretgardeneatery.com.au', 'Mon-Fri: 7am - 4pm', 'Cafe'),\n" +
                "('Sharetea', '32 Exhibition Walk, Clayton campus', NULL, 'marketing@sharetea.com.au', NULL, 'Mon–Fri: 10:30am – 6pm', 'Taiwanese'),\n" +
                "('Sir John''s', 'Level 1, Campus Centre, 21 Chancellors Walk, Clayton campus', '99053035', 'msa-sirjohns@monash.edu', 'www.sirjohnsbar.com', 'Mon: 12am – 5pm, Tue–Thu: 12am – 7pm, Fri: 12am – 5pm', 'Bar'),\n" +
                "('Subway', 'Ground Floor, Campus Centre, 21 Chancellors Walk, Clayton campus', '85540500', 'monashuni@subcatering.com.au', 'www.subway.com.au', 'Mon–Fri: 9am – 5pm', 'American'),\n" +
                "('Sushi Sushi', 'Ground Floor, Campus Centre, 21 Chancellors Walk, Clayton campus', NULL, 'info@sushisushi.com.au', 'sushisushi.com.au', 'Mon–Fri: 8am – 6pm', 'Japanese'),\n" +
                "('Swift''s Café', 'Matheson Library, 40 Exhibition Walk, Clayton campus', '90686150', 'enquiries@thecounts.com.au', NULL, 'Mon–Fri: 8am – 4pm, Sat-Sun: 11am - 2pm', 'Cafe'),\n" +
                "('Wholefoods', 'Level 1, Campus Centre, 21 Chancellors Walk, Clayton campus', '99024350', 'msa-wholefoods@monash.edu', 'monashwholefoods.org', 'Cafe: Mon–Fri: 9am – 4pm, Kitchen: Mon–Fri: 11:30am – 2:30pm', 'Vegetarian');";
        db.execSQL(insertBusinessData);

        String insertCustomerData = "INSERT INTO customer (username, passwrd, email, first_name, last_name) VALUES\n" +
                "('jdoe001', 'password123', 'jdoe001@student.monash.edu', 'John', 'Doe'),\n" +
                "('asmith002', 'password123', 'asmith002@student.monash.edu', 'Alice', 'Smith'),\n" +
                "('bwong003', 'password123', 'bwong003@student.monash.edu', 'Ben', 'Wong'),\n" +
                "('charris004', 'password123', 'charris004@student.monash.edu', 'Carol', 'Harris'),\n" +
                "('dlee005', 'password123', 'dlee005@student.monash.edu', 'David', 'Lee'),\n" +
                "('emma006', 'password123', 'emma006@student.monash.edu', 'Emma', 'Brown'),\n" +
                "('fclark007', 'password123', 'fclark007@student.monash.edu', 'Frank', 'Clark'),\n" +
                "('gwhite008', 'password123', 'gwhite008@student.monash.edu', 'Grace', 'White'),\n" +
                "('hking009', 'password123', 'hking009@student.monash.edu', 'Henry', 'King'),\n" +
                "('ijones010', 'password123', 'ijones010@student.monash.edu', 'Irene', 'Jones');\n";
        db.execSQL(insertCustomerData);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop tables and recreate them
        db.execSQL("DROP TABLE IF EXISTS review_image");
        db.execSQL("DROP TABLE IF EXISTS business_image");
        db.execSQL("DROP TABLE IF EXISTS image");
        db.execSQL("DROP TABLE IF EXISTS response");
        db.execSQL("DROP TABLE IF EXISTS review");
        db.execSQL("DROP TABLE IF EXISTS business");
        db.execSQL("DROP TABLE IF EXISTS owner");
        db.execSQL("DROP TABLE IF EXISTS customer");
        onCreate(db);
    }

    /**
     * Insert a new business into the database
     * @param bus_name
     * @param bus_addr
     * @param bus_ph_nb
     * @param bus_email
     * @param website_url
     * @param bus_hours
     * @param bus_cuisine_type
     */
    public void insertBusiness(String bus_name, String bus_addr, String bus_ph_nb, String bus_email, String website_url, String bus_hours, String bus_cuisine_type) {
        SQLiteDatabase db = this.getWritableDatabase();
        String insertBusinessData = "INSERT INTO business (bus_name, bus_addr, bus_ph_nb, bus_email, website_url, bus_hours, bus_cuisine_type) VALUES ('" + bus_name + "', '" + bus_addr + "', '" + bus_ph_nb + "', '" + bus_email + "', '" + website_url + "', '" + bus_hours + "', '" + bus_cuisine_type + "');";
        db.execSQL(insertBusinessData);
    }

    @SuppressLint("Range")
    public ArrayList<Restaurant> getAllRestaurants(){
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM business";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Restaurant restaurant = new Restaurant();
                restaurant.setId(cursor.getInt(cursor.getColumnIndex("bus_id")));
                restaurant.setName(cursor.getString(cursor.getColumnIndex("bus_name")));
                restaurant.setAddress(cursor.getString(cursor.getColumnIndex("bus_addr")));
                restaurant.setPhone(cursor.getString(cursor.getColumnIndex("bus_ph_nb")));
                restaurant.setEmail(cursor.getString(cursor.getColumnIndex("bus_email")));
                restaurant.setWebsite(cursor.getString(cursor.getColumnIndex("website_url")));
                restaurant.setHours(cursor.getString(cursor.getColumnIndex("bus_hours")));
                restaurant.setCuisine(cursor.getString(cursor.getColumnIndex("bus_cuisine_type")));
                restaurants.add(restaurant);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return restaurants;
    }

}
