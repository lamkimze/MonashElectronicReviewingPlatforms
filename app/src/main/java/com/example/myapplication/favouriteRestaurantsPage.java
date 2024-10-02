package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.CRUD_Business;
import com.example.myapplication.Database.CRUD_Image;
import com.example.myapplication.Database.CRUD_Review;
import com.example.myapplication.Database.CRUD_User;
import com.example.myapplication.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class favouriteRestaurantsPage extends AppCompatActivity {

//    private RecyclerView recyclerView;
//    private List<RestaurantItem> items;
//    private ItemAdapter itemAdapter;
//    private SearchView searchView;
    DatabaseHelper dbHelper;
    CRUD_Business crudBusiness;
    CRUD_User crudUser;
    CRUD_Image crudImage;
    CRUD_Review crudReview;
    ArrayList<Integer> favoriteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favourite_restaurants_page);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        initializeDatabase();
        List<RestaurantItem> items = new ArrayList<>();

        // Loop through the favorite business IDs and add them to the 'items' list
        for (int busId : favoriteList) {
            // Fetch the business details using CRUD_Business (assuming you have a method getBusinessById)
            Restaurant business = crudBusiness.getRestaurant(busId);
            if (business != null) {
                // Assuming the Restaurant class has a method to get name, rating, and image
                String businessName = business.getName();
                String rating = String.format("%.1f ⭐", crudBusiness.getBusinessStarRating(business));
                Bitmap imageRes = crudImage.getBusinessImage(busId);  // Assuming you have a method to get the image

                // Add the business to the items list
                items.add(new RestaurantItem(businessName, rating, imageRes));
            }
        }

        TextView numFav = findViewById(R.id.totalFavourite);
        TextView name = findViewById(R.id.displayName);
        numFav.setText(items.size() + " places");
        name.setText(crudUser.getFullName(1));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(getApplicationContext(),items));



//        searchView = findViewById(R.id.searchView);
//        searchView.clearFocus();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
////                itemAdapter.getFilter().filter(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
////                itemAdapter.getFilter().filter(newText);
//                filterList(newText);
//                return false;
//            }
//        });

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

//    private void filterList(String text) {
//        List<Item> filteredList = new ArrayList<>();
//        for(Item item: itemList){
//            if(item.getItemName().toLowerCase().contains(text.toLowerCase())){
//                filteredList.add(item);
//            }
//        }
//        if(filteredList.isEmpty()){
//            Toast.makeText(this, "No item found", Toast.LENGTH_SHORT).show();
//        }
//    }
private void initializeDatabase() {

    try {
        dbHelper = new DatabaseHelper(this);
        crudBusiness = new CRUD_Business(dbHelper);
        crudUser = new CRUD_User(dbHelper);
        crudImage = new CRUD_Image(dbHelper);
        crudReview = new CRUD_Review(dbHelper);
        favoriteList = crudUser.getFavoriteBusinesses(1);



        // Notify success on the main thread
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(this, "Database initialized successfully!", Toast.LENGTH_LONG).show()
        );
    } catch (Exception e) {
        e.printStackTrace();

        // Notify failure on the main thread
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(this, "Database initialization failed!", Toast.LENGTH_LONG).show()
        );
    }
}
}