package com.example.myapplication;

import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.Manifest;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.CRUD_Review;
import com.example.myapplication.Database.DatabaseHelper;
import com.example.myapplication.Database.DbBitmapUtility;
import com.google.android.material.card.MaterialCardView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class userReviewCreationPage extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView textView;
    ImageButton pick;

    ArrayList<Uri> uri = new ArrayList<>();
    RecyclerAdapter adapter;

    public static final int Read_Permission = 101;

    MaterialCardView selectCard;
    TextView tvCuisineProd;
    boolean [] selectedCuisineProd;
    ArrayList<Integer> cuisineProdList = new ArrayList<>();
    String [] cuisineArray = {"American","Chinese","Indian","Italian","Japanese","Korean","Mexican","Thai","Vietnamese"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_review_creation_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textView = findViewById(R.id.totalPhotos);
        recyclerView =findViewById(R.id.recyclerView_Gallery_Images);
        pick = findViewById(R.id.imageButton);

        adapter = new RecyclerAdapter(uri);
        recyclerView.setLayoutManager(new GridLayoutManager(userReviewCreationPage.this,4));
        recyclerView.setAdapter(adapter);

        if(ContextCompat.checkSelfPermission(userReviewCreationPage.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(userReviewCreationPage.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Read_Permission);
        }

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

                }
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),1);
            }
        });
        selectCard = findViewById(R.id.selectCard);
        tvCuisineProd = findViewById(R.id.tvCuisineProd);
        selectedCuisineProd = new boolean[cuisineArray.length];

        selectCard.setOnClickListener(v -> {
            showCuisineProdDialog();
        });




        // Submit button
        Button submitButton = findViewById(R.id.button3);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReview();
            }
        });



    }

    private void showCuisineProdDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Cuisine/Products");
        builder.setCancelable(false);

        builder.setMultiChoiceItems(cuisineArray, selectedCuisineProd, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked){
                    cuisineProdList.add(which);
                }else{
                    cuisineProdList.remove(which);
                }
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // create string builder
                StringBuilder stringBuilder = new StringBuilder();
                for (int i=0; i < cuisineProdList.size(); i++){
                    stringBuilder.append(cuisineArray[cuisineProdList.get(i)]);

                    // check condition
                    if (i != cuisineProdList.size() - 1){
                        // when i is not equal to cuisine list size then add a comma
                        stringBuilder.append(", ");
                    }
                    // setting selected cuisine to textview
                    tvCuisineProd.setText(stringBuilder.toString());
                }

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i = 0; i < selectedCuisineProd.length; i++){
                    selectedCuisineProd[i] = false;
                    cuisineProdList.clear();
                    tvCuisineProd.setText("");
                }

            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK){
            if(data.getClipData() != null){
                int count = data.getClipData().getItemCount();

                for(int i = 0; i < count; i++){
                    uri.add(data.getClipData().getItemAt(i).getUri());
                }
                adapter.notifyDataSetChanged();
                textView.setText("Photos ("+uri.size()+")");

            }else if(data.getData() != null){
                String imageURL = data.getData().getPath();
                uri.add(Uri.parse(imageURL));
            }
        }
    }

    private void submitReview() {
        // Get the review information
        int businessId = getIntent().getIntExtra("businessId", -1);
        int customerId = getIntent().getIntExtra("customerId", -1);
        if (businessId == -1 || customerId == -1) {
            Toast.makeText(this, "Error: User ID not found", Toast.LENGTH_SHORT).show();
        }
        float rating = ((RatingBar) findViewById(R.id.ratingBar)).getRating();
        String title =  ((TextView) findViewById(R.id.reviewTitle)).getText().toString();
        String text = ((TextView) findViewById(R.id.reviewDetail)).getText().toString();

        // turn image uris into bitmap array
        ArrayList<Bitmap> images = new ArrayList<>();
        for (Uri imageUri : uri) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                images.add(bitmap);
            } catch (IOException e) {
                Toast.makeText(this, "Error: Image could not be loaded", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // create the review
        ReviewModel review = new ReviewModel(rating, title, images, text, customerId, businessId);
        CRUD_Review crudReview = new CRUD_Review(new DatabaseHelper(this));

        // add the review to the database
        try {
            crudReview.createReview(review);
            Toast.makeText(this, "Review created successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error: Review could not be created", Toast.LENGTH_SHORT).show();
            return;
        }



    }
}