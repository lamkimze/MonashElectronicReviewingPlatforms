package com.example.myapplication;

import static android.provider.MediaStore.Images.Media.insertImage;

import static com.example.myapplication.Enumerables.ImageType.BUSINESS;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Enumerables.ImageType;
import com.github.dhaval2404.imagepicker.ImagePicker;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class profilePageEditor extends AppCompatActivity {
    ImageView profilePic;
    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register the launcher for image picking
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                if (data != null && data.getData() != null) {
                    selectedImageUri = data.getData();
                    // Update the ImageView with the selected image
                    profilePic.setImageURI(selectedImageUri);
                }
            }
        });

        // Set the content view
        setContentView(R.layout.activity_profile_page_editor);

        // Initialize the ImageView
        profilePic = findViewById(R.id.profilePicEdit);

        // Set the click listener for the profile picture
        profilePic.setOnClickListener(v -> {
            ImagePicker.with(this)
                    .cropSquare()  // Allow cropping to a square
                    .compress(512) // Compress the image to 512kb
                    .maxResultSize(512, 512) // Set the max result size
                    .createIntent(intent -> {
                        imagePickLauncher.launch(intent);
                        return null;
                    });
        });

//        profilePic.buildDrawingCache();
//        Bitmap bmap = profilePic.getDrawingCache();

//        insertImage(1, BUSINESS, bmap);

        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}