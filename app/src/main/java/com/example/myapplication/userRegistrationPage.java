package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Database.CRUD_User;
import com.example.myapplication.Database.DatabaseHelper;
import com.example.myapplication.Entities.User;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;

public class userRegistrationPage extends AppCompatActivity {
    ImageView picButton;
    ImageView profilePic;
    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;

    TextInputEditText etFirstName, etLastName, etUserName, etPassword, etConPassword, etEmail;
    String stringFirstName, stringLastName, stringUserName, stringPassword, stringConPassword, stringEmail;
    String stringPosition;
    String [] positions = {"Fast Food Attendant", "Busser", "Runner", "Cashier", "DishWasher", "Barista", "Prep Cook",
            "Expeditor", "Pastry Cook", "Server", "Bartender", "Line Cook", "Kitchen Manager", "Sous Chef", "Catering Manager",
    "General Manager", "Maintenance Person", "Executive Chef", "Main Chef"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    DatabaseHelper dbHelper;
    CRUD_User crudUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        try {
            dbHelper = new DatabaseHelper(this);
            crudUser = new CRUD_User(dbHelper);
            Toast.makeText(this, "Database initialized successfully!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Database initialization failed!", Toast.LENGTH_LONG).show();
        }


        etFirstName = findViewById(R.id.regFirstName);
        etLastName = findViewById(R.id.regLastName);
        etUserName = findViewById(R.id.regUsername);
        etPassword = findViewById(R.id.regPassword);
        etConPassword = findViewById(R.id.regConPassword);
        etEmail = findViewById(R.id.regEmail);

        autoCompleteTextView = findViewById(R.id.positions);
        adapterItems = new ArrayAdapter<String>(this, R.layout.positions, positions);
        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l){
                stringPosition = adapterView.getItemAtPosition(position).toString();
            }
        });
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
//        setContentView(R.layout.activity_registration_page);


        picButton = findViewById(R.id.userPicUpload);
        profilePic = findViewById(R.id.imageView);

        // Set the click listener for the business picture
        picButton.setOnClickListener(v -> {
            ImagePicker.with(this)
                    .cropSquare()  // Allow cropping to a square
                    .compress(512) // Compress the image to 512kb
                    .maxResultSize(512, 512) // Set the max result size
                    .createIntent(intent -> {
                        imagePickLauncher.launch(intent);
                        return null;
                    });
        });

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

    }


    public void onClickSubmit(View view){
        stringFirstName = etFirstName.getText().toString();
        stringLastName = etLastName.getText().toString();
        stringUserName = etUserName.getText().toString();
        stringPassword = etPassword.getText().toString();
        stringConPassword = etConPassword.getText().toString();
        stringEmail = etEmail.getText().toString();

        Log.d("userRegistrationPage", "Submit button clicked");

        if(!stringFirstName.equals("") && !stringLastName.equals("")){
            if(stringFirstName.matches("^[a-zA-Z ]*$") && stringLastName.matches("^[a-zA-Z ]*$")){
                if(!stringPassword.equals("") && !stringConPassword.equals("")){
                    if(stringPassword.equals(stringConPassword)){
                        if(stringEmail.matches("^[a-z]{4}[0-9]{4}@student\\.monash\\.edu$")){
                            try {
                                User user = new User(stringUserName, stringEmail, stringFirstName, stringLastName);
//                                user.setId(crudUser.getCustomerID(user));
                                boolean isInserted =  crudUser.createUser(user, stringPassword, null);
                                if (isInserted) {
                                    Toast.makeText(this, "Registration Successful !!", Toast.LENGTH_LONG).show();
                                    switchLoginPage();
                                } else {
                                    Toast.makeText(this, "Registration Failed !!", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("userRegistrationPage", "Error inserting user: " + e.getMessage());
                            }

                        }
                        else{
                            Toast.makeText(this, stringEmail, Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(this, "Password and Confirm Password must be the same!!", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(this, "Please insert your password and confirm password!!", Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(this, "Please insert your proper name!!", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this, "Please insert your first name and last name!!", Toast.LENGTH_LONG).show();
        }
    }



    public void switchLoginPage(){
        Intent loginIntent = new Intent(this, loginPage.class);
        startActivity(loginIntent);
    }

//    public void onClickUploadPicture(View view){
//        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
//        }
//    }
}