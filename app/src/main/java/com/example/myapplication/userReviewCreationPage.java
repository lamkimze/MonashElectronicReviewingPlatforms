package com.example.myapplication;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.CRUD_Business;
import com.example.myapplication.Database.CRUD_Image;
import com.example.myapplication.Database.CRUD_Review;
import com.example.myapplication.Database.DatabaseHelper;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class userReviewCreationPage extends AppCompatActivity {

//    read all the inputs
    RecyclerView recyclerView;
    TextView textView;
    TextView restaurantName;
    TextView reviewTitle;
    TextView reviewContent;
    ImageButton pick;
    int busId, reviewerId, reviewId;
    String key;
    RatingBar ratingBar;
    TagsInputEditText tagsInputEditText;
    RecyclerAdapter adapter;
    Button submitButton;
    showImages imageAdapter;

    //    Converting to respective data style
    float rating;
    String stringTags;
    String stringReviewTitle;
    String stringReviewContent;
    ArrayList<Uri> uri = new ArrayList<>();

    //    Database initialization
    DatabaseHelper dbHelper;
    CRUD_Review crudReview;
    CRUD_Business crudBusiness;
    CRUD_Image crudImage;
    Restaurant reviewed_restaurant;

    public static final int Read_Permission = 101;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_review_creation_page);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Review Creation");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Initialize fields
        busId = getIntent().getExtras().getInt("busId");
        reviewerId = getIntent().getExtras().getInt("userId");
        reviewId = getIntent().getExtras().getInt("reviewId", 0);
        key = getIntent().getExtras().getString("key", "createPost");

        loadData(); // Initialize CRUD operations
        reviewed_restaurant = crudBusiness.getRestaurant(busId);

        // Setting up views
        restaurantName = findViewById(R.id.rcRestaurantName);
        restaurantName.setText(reviewed_restaurant.getName());
        reviewTitle = findViewById(R.id.reviewTitle);
        reviewContent = findViewById(R.id.reviewDetail);
        ratingBar = findViewById(R.id.reviewCreateRatingBar);
        tagsInputEditText = findViewById(R.id.tagsET);
        textView = findViewById(R.id.totalPhotos);
        recyclerView = findViewById(R.id.recyclerView_Gallery_Images);
        pick = findViewById(R.id.imageButton);
        submitButton = findViewById(R.id.reviewSubmit);

        // Setting up RecyclerView
        adapter = new RecyclerAdapter(uri);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(adapter);

        // Check for read storage permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Read_Permission);
        }

        // If editing a post, populate the fields with existing data
        if (key.equals("editPost")) {
            ReviewModel editReview = crudReview.getReview(reviewId);
            ratingBar.setRating(editReview.getReviewRating());
            reviewTitle.setText(editReview.getReviewTitle());
            reviewContent.setText(editReview.getReviewText());

            StringBuilder tagsBuilder = new StringBuilder();
            for (String tag : editReview.getTags()) {
                tagsBuilder.append(tag);
                tagsBuilder.append(" ");  // Use the defined separator
            }

            tagsInputEditText.setText(tagsBuilder.toString());

            // Fetching images associated with the review
            List<Bitmap> reviewImages = editReview.getReviewImages();
            for (Bitmap image : reviewImages) {
                Uri imageUri = getImageUriFromBitmap(image); // Convert Bitmap to Uri
                uri.add(imageUri);  // Add each image Uri to the list
            }

            adapter.notifyDataSetChanged(); // Notify adapter that data has changed
            textView.setText(String.format(Locale.ENGLISH, "Photos (%d)", uri.size()));
        }

        // Image picker button click listener
        pick.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        });

        // Submit button click listener to save review
        submitButton.setOnClickListener(view -> {
            stringReviewTitle = reviewTitle.getText().toString();
            stringReviewContent = reviewContent.getText().toString();
            rating = ratingBar.getRating();
            stringTags = tagsInputEditText.getText().toString();

            ReviewModel reviewModel = new ReviewModel(rating, stringReviewTitle, new ArrayList<>(), stringReviewContent, reviewerId, busId);

            // Add images to review
            for (Uri imageUri : uri) {
                try {
                    Bitmap image = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        image = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), imageUri));
                    } else {
                        image = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    }
                    reviewModel.addReviewImage(image);
                } catch (Exception e) {
                    Toast.makeText(this, "Error adding image to review: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("userReviewCreationPage", "Error adding image to review: " + e.getMessage());
                }
            }

            // Save review to database
            try {
                reviewModel.setTags(stringTags.split(" "));
                if (key.equals("editPost")) {
                    crudReview.replaceReview(reviewId, reviewModel);
                } else {
                    crudReview.createReview(reviewModel);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("userReviewCreationPage", "Error creating review: " + e.getMessage());
            }

            // Start the next activity
            Intent submitIntent = new Intent(getApplicationContext(), restaurantDetailPage.class);
            submitIntent.putExtra("busID", busId);
            submitIntent.putExtra("userID", reviewerId);
            startActivity(submitIntent);
        });
    }

    //
//    private void showCuisineProdDialog(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setTitle("Select Cuisine/Products");
//        builder.setCancelable(false);
//
//        builder.setMultiChoiceItems(cuisineArray, selectedCuisineProd, new DialogInterface.OnMultiChoiceClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//                if (isChecked){
//                    cuisineProdList.add(which);
//                }else{
//                    cuisineProdList.remove(which);
//                }
//            }
//        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // create string builder
//                StringBuilder stringBuilder = new StringBuilder();
//                for (int i=0; i < cuisineProdList.size(); i++){
//                    stringBuilder.append(cuisineArray[cuisineProdList.get(i)]);
//
//                    // check condition
//                    if (i != cuisineProdList.size() - 1){
//                        // when i is not equal to cuisine list size then add a comma
//                        stringBuilder.append(", ");
//                    }
//                    // setting selected cuisine to textview
//                    tvCuisineProd.setText(stringBuilder.toString());
//                }
//
//            }
//        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        }).setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int which) {
//                for (int i = 0; i < selectedCuisineProd.length; i++){
//                    selectedCuisineProd[i] = false;
//                    cuisineProdList.clear();
//                    tvCuisineProd.setText("");
//                }
//
//            }
//        });
//        builder.show();
//    }
//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    uri.add(imageUri);  // Add each Uri to the list
                }
                adapter.notifyDataSetChanged();
                textView.setText(String.format(Locale.ENGLISH,"Photos (%d)", uri.size()));
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();  // Properly handle a single image Uri
                uri.add(imageUri);  // Add the Uri to the list
                adapter.notifyDataSetChanged();
                textView.setText(String.format(Locale.ENGLISH,"Photos (%d)", uri.size()));
            }
        }
    }


    private void loadData() {
        try {
            dbHelper = new DatabaseHelper(this);
            crudBusiness = new CRUD_Business(dbHelper);
            crudReview = new CRUD_Review(dbHelper);
            crudImage = new CRUD_Image(dbHelper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private Uri getImageUriFromBitmap(Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }


}