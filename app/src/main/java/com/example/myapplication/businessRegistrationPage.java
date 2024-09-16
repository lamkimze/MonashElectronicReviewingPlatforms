package com.example.myapplication;

import static android.provider.MediaStore.Images.Media.insertImage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.CRUD_Business;
import com.example.myapplication.Database.CRUD_Image;
import com.example.myapplication.Database.CRUD_User;
import com.example.myapplication.Database.DatabaseHelper;
import com.example.myapplication.Entities.Owner;
import com.example.myapplication.Enumerables.ImageType;
import com.example.myapplication.databinding.ActivityBusinessRegistrationPageBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class businessRegistrationPage extends AppCompatActivity {
    ImageView picButton;
    ImageView businessPic;
    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;

    ActivityBusinessRegistrationPageBinding binding;
    TextInputEditText etRestaurantName, etAddress, etRestaurantURL, etPhoneNumber, etCuisineType;
    TextInputEditText etFirstName, etLastName, etUserName, etPassword, etConPassword, etEmail;
    TextInputEditText operatingStart, operatingEnd;

    String stringRestaurantName, stringAddress, stringRestaurantURL, stringPhoneNumber, stringCuisineType;
    String stringFirstName, stringLastName, stringUserName, stringPassword, stringConPassword, stringEmail;

    Date timeOperatingStart, timeOperatingEnd;
    String operatingDay;
    DatabaseHelper dbHelper;
    CRUD_Business crudBusiness;
    CRUD_User crudUser;
    CRUD_Image crudImage;
    int businessId;
    boolean exist = false;

    ArrayList<Restaurant> restaurants = new ArrayList<>();
    String[] items = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;

    ArrayList<operatingTime> listOperatingTime = new ArrayList<>();
    OperatingTimeRecyclerAdapter recyclerAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBusinessRegistrationPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize UI elements
        etFirstName = findViewById(R.id.bisRegFirstName);
        etLastName = findViewById(R.id.bisRegLastName);
        etUserName = findViewById(R.id.bisRegUsername);
        etPassword = findViewById(R.id.bisRegPassword);
        etConPassword = findViewById(R.id.bisRegConPassword);
        etEmail = findViewById(R.id.bisRegEmail);
        etRestaurantName = findViewById(R.id.restaurantName);
        etAddress = findViewById(R.id.restaurantAddress);
        etRestaurantURL = findViewById(R.id.restaurantWebsite);
        etPhoneNumber = findViewById(R.id.restaurantPhone);
        etCuisineType = findViewById(R.id.cuisineType);

        autoCompleteTextView = findViewById(R.id.days);
        operatingStart = findViewById(R.id.operatingStart);
        operatingEnd = findViewById(R.id.operatingEnd);

        adapterItems = new ArrayAdapter<>(this, R.layout.days, items);
        autoCompleteTextView.setAdapter(adapterItems);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new OperatingTimeRecyclerAdapter();
        recyclerAdapter.setData(listOperatingTime);
        recyclerView.setAdapter(recyclerAdapter);

        // Register the launcher for image picking
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                if (data != null && data.getData() != null) {
                    selectedImageUri = data.getData();
                    // Update the ImageView with the selected image
                    businessPic.setImageURI(selectedImageUri);
                    try {
                        Bitmap bitmap = getBitmapFromUri(selectedImageUri);
                        crudImage.insertImage(10, ImageType.BUSINESS, bitmap);  // Assuming linkingID = 1
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Set the content view
        setContentView(R.layout.activity_business_registration_page);


        picButton = findViewById(R.id.userPicUpload);
        businessPic = findViewById(R.id.imageView);

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

//        businessPic.buildDrawingCache();
//        Bitmap bitmap = businessPic.getDrawingCache();
//        crudImage.insertImage(1, ImageType.BUSINESS, bitmap);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set listeners
        setListeners();

        // Initialize database
        initializeDatabase();
    }

    private void setListeners() {
        // OnFocusChangeListener for Restaurant Name
        etRestaurantName.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                stringRestaurantName = etRestaurantName.getText().toString();
                if (!stringRestaurantName.isEmpty()) {
                    exist = autoFillRestaurantDetail(stringRestaurantName);
                    if (!exist) {
                        etAddress.setText(""); // Clear the address field if restaurant is not found
                    }
                }
            }
        });

        // OnItemClickListener for AutoCompleteTextView
        autoCompleteTextView.setOnItemClickListener((adapterView, view, position, l) -> {
            operatingDay = adapterView.getItemAtPosition(position).toString();
        });
    }

    private void initializeDatabase() {
//        new Thread(() -> {
        try {
            dbHelper = new DatabaseHelper(this);
            crudBusiness = new CRUD_Business(dbHelper);
            crudUser = new CRUD_User(dbHelper);
            crudImage = new CRUD_Image(dbHelper);
            ArrayList<Restaurant> dbRestaurants = crudBusiness.getAllRestaurants();
            restaurants.addAll(dbRestaurants);

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
//        }).start();
    }

    public void onClickBusinessSubmit(View view) {
        detailsChecking();
    }

    public void onClickAddButton(View view) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        try {
            String startText = operatingStart.getText().toString();
            String endText = operatingEnd.getText().toString();

            if (startText.isEmpty() || endText.isEmpty()) {
                Toast.makeText(this, "Please enter both start and end times.", Toast.LENGTH_SHORT).show();
                return;
            }

            timeOperatingStart = dateFormat.parse(startText);
            timeOperatingEnd = dateFormat.parse(endText);

            if (timeOperatingEnd.after(timeOperatingStart)) {
                operatingTime newTime = new operatingTime(operatingDay, timeOperatingStart, timeOperatingEnd);
                addOperatingTimeToRecyclerView(newTime);
            } else {
                Toast.makeText(this, "End time must be after start time!", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            Toast.makeText(this, "Invalid time format. Please use HH:mm.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void detailsChecking() {
        stringFirstName = etFirstName.getText().toString();
        stringLastName = etLastName.getText().toString();
        stringUserName = etUserName.getText().toString();
        stringPassword = etPassword.getText().toString();
        stringConPassword = etConPassword.getText().toString();
        stringEmail = etEmail.getText().toString();
        stringRestaurantURL = etRestaurantURL.getText().toString();
        stringPhoneNumber = etPhoneNumber.getText().toString();
        stringCuisineType = etCuisineType.getText().toString();
        stringAddress = etAddress.getText().toString();

        if (!stringFirstName.isEmpty() && !stringLastName.isEmpty()) {
            if (stringFirstName.matches("^[a-zA-Z ]*$") && stringLastName.matches("^[a-zA-Z ]*$")) {
                if (!stringPassword.isEmpty() && !stringConPassword.isEmpty()) {
                    if (stringPassword.equals(stringConPassword)) {
                        businessDetailChecking();
                    } else {
                        Toast.makeText(this, "Password and Confirm Password must be the same!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Please insert your password and confirm password!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Please insert a proper name!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Please insert your first name and last name!", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isValidURL(String url) {
        String urlPattern = "^[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.(com|au)$\n";
        return url.matches(urlPattern);
    }

    private void businessDetailChecking() {
//        if (isValidURL(stringRestaurantURL)) {
            if (stringCuisineType.matches("^[a-zA-Z ]*$")) {
                registeredBusiness();
                switchLoginPage();
            }
            else{
                Toast.makeText(this, "Invalid Cuisine Type", Toast.LENGTH_LONG).show();
            }
//        }
//        else{
//            Toast.makeText(this, "InValid URL!", Toast.LENGTH_LONG).show();
//        }
    }

    private void registeredBusiness() {
        Owner newOwner = new Owner(stringUserName, stringEmail, stringFirstName, stringLastName);
        boolean isInserted = crudUser.createUser(newOwner, stringPassword);
//        newOwner.setId(crudUser.getOwnerID(newOwner));
//        crudUser.assignOwnerToBus(newOwner, );
        if(isInserted){
            if (exist) {
                crudBusiness.updateRestaurantDetail(stringRestaurantName, stringAddress, stringPhoneNumber, stringEmail, stringRestaurantURL, recyclerAdapter.databaseFormat(), stringCuisineType);
            } else {
                crudBusiness.createBusiness(stringRestaurantName.trim(), stringAddress.trim(),
                        stringPhoneNumber.trim(), stringEmail.trim(), stringRestaurantURL.trim(),
                        recyclerAdapter.databaseFormat().trim(), stringCuisineType.trim());
            }
            Toast.makeText(this, "Registration Successful!", Toast.LENGTH_LONG).show();
        }
    }

    private void addOperatingTimeToRecyclerView(operatingTime time) {
        listOperatingTime.add(time);
        recyclerAdapter.notifyDataSetChanged();
        clearField();
    }

    private void switchLoginPage() {
        Intent switchIntent = new Intent(getApplicationContext(), loginPage.class);
        startActivity(switchIntent);
    }

    private boolean autoFillRestaurantDetail(String restaurantName) {
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getName().equalsIgnoreCase(restaurantName)) {
                etAddress.setText(restaurant.getAddress());
                etCuisineType.setText(restaurant.getCuisine());
                etPhoneNumber.setText(restaurant.getPhone());
                etRestaurantURL.setText(restaurant.getWebsite());
                businessId = restaurant.getId();

                return true;
            }
        }
        return false;
    }

    private void clearField() {
        operatingStart.setText("");
        operatingEnd.setText("");
        autoCompleteTextView.setText(null);
        autoCompleteTextView.setFocusable(false);
    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // For newer Android versions (API level 28 and above)
            ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), uri);
            return ImageDecoder.decodeBitmap(source);
        } else {
            // For older Android versions
            return MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        }
    }
}
