package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class userRegistrationPage extends AppCompatActivity {

    TextInputEditText etFirstName, etLastName, etUserName, etPassword, etConPassword, etEmail;
    String stringFirstName, stringLastName, stringUserName, stringPassword, stringConPassword, stringEmail;
    String stringPosition;
    String [] positions = {"Fast Food Attendant", "Busser", "Runner", "Cashier", "DishWasher", "Barista", "Prep Cook",
            "Expeditor", "Pastry Cook", "Server", "Bartender", "Line Cook", "Kitchen Manager", "Sous Chef", "Catering Manager",
    "General Manager", "Maintenance Person", "Executive Chef", "Main Chef"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        etFirstName = findViewById(R.id.regFirstName);
        etLastName = findViewById(R.id.regLastName);
        etUserName = findViewById(R.id.regUsername);
        etPassword = findViewById(R.id.regPassword);
        etConPassword = findViewById(R.id.regConPassword);
        etEmail = findViewById(R.id.bisRegEmail);

        autoCompleteTextView = findViewById(R.id.positions);
        adapterItems = new ArrayAdapter<String>(this, R.layout.positions, positions);
        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l){
                stringPosition = adapterView.getItemAtPosition(position).toString();
            }
        });

    }


    public void onClickSubmit(View view){
        stringFirstName = etFirstName.getText().toString();
        stringLastName = etLastName.getText().toString();
        stringUserName = etUserName.getText().toString();
        stringPassword = etPassword.getText().toString();
        stringConPassword = etConPassword.getText().toString();
        stringEmail = etEmail.getText().toString();

        if(!stringFirstName.equals("") && !stringLastName.equals("")){
            if(stringFirstName.matches("^[a-zA-Z ]*$") && stringLastName.matches("^[a-zA-Z ]*$")){
                if(!stringPassword.equals("") && !stringConPassword.equals("")){
                    if(stringPassword.equals(stringConPassword)){
                        if(stringEmail.matches("^[a-z]{4}[0-9]{4}@student\\.monash\\.edu$")){
                            Toast.makeText(this, "Registration Successful !!", Toast.LENGTH_LONG).show();
                            saveToSharedPreference();
                            switchLoginPage();
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

    public void saveToSharedPreference(){
        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATAFILE", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("USERNAME", stringUserName);
        editor.putString("PASSWORD", stringPassword);

        editor.apply();
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