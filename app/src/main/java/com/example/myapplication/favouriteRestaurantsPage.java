package com.example.myapplication;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class favouriteRestaurantsPage extends AppCompatActivity {

//    private RecyclerView recyclerView;
//    private List<RestaurantItem> items;
//    private ItemAdapter itemAdapter;
//    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favourite_restaurants_page);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        List<RestaurantItem> items = new ArrayList<RestaurantItem>();
        items.add(new RestaurantItem("Guzman y Gomez", "4.5", R.drawable.guzmanygomez));
        items.add(new RestaurantItem("Noodle Plus", "3.0", R.drawable.gyg_banner1));
        items.add(new RestaurantItem("Guzman y Gomez", "4.5", R.drawable.guzmanygomez));
        items.add(new RestaurantItem("Noodle Plus", "3.0", R.drawable.gyg_banner1));
        items.add(new RestaurantItem("Guzman y Gomez", "4.5", R.drawable.guzmanygomez));
        items.add(new RestaurantItem("Noodle Plus", "3.0", R.drawable.gyg_banner1));
        items.add(new RestaurantItem("Guzman y Gomez", "4.5", R.drawable.guzmanygomez));
        items.add(new RestaurantItem("Noodle Plus", "3.0", R.drawable.gyg_banner1));
        items.add(new RestaurantItem("Guzman y Gomez", "4.5", R.drawable.guzmanygomez));
        items.add(new RestaurantItem("Noodle Plus", "3.0", R.drawable.gyg_banner1));
        items.add(new RestaurantItem("Guzman y Gomez", "4.5", R.drawable.guzmanygomez));
        items.add(new RestaurantItem("Noodle Plus", "3.0", R.drawable.gyg_banner1));
        items.add(new RestaurantItem("Guzman y Gomez", "4.5", R.drawable.guzmanygomez));
        items.add(new RestaurantItem("Noodle Plus", "3.0", R.drawable.gyg_banner1));

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
}