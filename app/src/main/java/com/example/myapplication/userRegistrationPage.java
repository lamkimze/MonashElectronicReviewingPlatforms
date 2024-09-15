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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Database.CRUD_Business;
import com.example.myapplication.Database.CRUD_Image;
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
    CRUD_Business crudBusiness;
    CRUD_Image crudImage;
    Restaurant verifyRestaurant;
    TextView restaurantNameShowing;
    LinearLayout restaurantVerify;
    Button verificationButton;

    TextInputEditText etFirstName, etLastName, etUserName, etPassword, etConPassword, etEmail;
    TextInputEditText etBusId;
    String stringFirstName, stringLastName, stringUserName, stringPassword, stringConPassword, stringEmail;
    String stringPosition;
    String [] positions = {"Fast Food Attendant", "Busser", "Runner", "Cashier", "DishWasher", "Barista", "Prep Cook",
            "Expeditor", "Pastry Cook", "Server", "Bartender", "Line Cook", "Kitchen Manager", "Sous Chef", "Catering Manager",
    "General Manager", "Maintenance Person", "Executive Chef", "Main Chef"};
    AutoCompleteTextView autoCompleteTextView, etPosition;
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
        etBusId = findViewById(R.id.restaurantCode);
        etPosition = findViewById(R.id.positions);
        restaurantNameShowing = findViewById(R.id.restaurantNameShowing);
        restaurantVerify = findViewById(R.id.restaurantVerification);
        restaurantVerify.setVisibility(View.GONE);
        verificationButton = findViewById(R.id.button6);

        autoCompleteTextView = findViewById(R.id.positions);
        adapterItems = new ArrayAdapter<String>(this, R.layout.positions, positions);
        autoCompleteTextView.setAdapter(adapterItems);

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

        verificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String busId = etBusId.getText().toString();
                    verifyRestaurant = crudBusiness.getRestaurant(Integer.parseInt(busId));
                    if(verifyRestaurant != null){
                        restaurantVerify.setVisibility(View.VISIBLE);
                        restaurantNameShowing.setText(verifyRestaurant.getName());
                    }
                }catch (Exception e){

                }
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l){
                stringPosition = adapterView.getItemAtPosition(position).toString();
            }
        });


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
        loadBusiness();

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
                                user.setPosition(stringPosition);
                                boolean isInserted =  crudUser.createUser(user, stringPassword, null);
                                user.setId(crudUser.getUserID(user));
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

    public void loadBusiness(){
        try {
            dbHelper = new DatabaseHelper(this);
            crudBusiness = new CRUD_Business(dbHelper); // or whatever initialization is required
            crudImage = new CRUD_Image(dbHelper);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

}