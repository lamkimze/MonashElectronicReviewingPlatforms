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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class businessRegistrationPage extends AppCompatActivity {

    TextInputEditText etFirstName, etLastName, etUserName, etPassword, etConPassword, etEmail;
    String stringFirstName, stringLastName, stringUserName, stringPassword, stringConPassword, stringEmail;
    TextInputEditText operatingStart, operatingEnd;
    Date timeOperatingStart, timeOperatingEnd;
    String operatingDay;


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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_business_registration_page);

        etFirstName = findViewById(R.id.bisRegFirstName);
        etLastName = findViewById(R.id.bisRegLastName);
        etUserName = findViewById(R.id.bisRegUsername);
        etPassword = findViewById(R.id.bisRegPassword);
        etConPassword = findViewById(R.id.bisRegConPassword);
        etEmail = findViewById(R.id.bisRegEmail);

        stringFirstName = etFirstName.getText().toString();
        stringLastName = etLastName.getText().toString();
        stringPassword = etPassword.getText().toString();
        stringConPassword = etConPassword.getText().toString();
        stringEmail = etEmail.getText().toString();

        autoCompleteTextView = findViewById(R.id.days);

        operatingStart = findViewById(R.id.operatingStart);
        operatingEnd = findViewById(R.id.operatingEnd);

        adapterItems = new ArrayAdapter<String>(this, R.layout.days, items);
        autoCompleteTextView.setAdapter(adapterItems);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new OperatingTimeRecyclerAdapter();
        recyclerAdapter.setData(listOperatingTime);
        recyclerView.setAdapter(recyclerAdapter);
;
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                operatingDay = adapterView.getItemAtPosition(position).toString();
            }
        });

    }

    public void onClickSubmitButton(View view){
        Intent loginIntent = new Intent(this, loginPage.class);
        startActivity(loginIntent);
    }

    public void onClickAddButton(View view) {

        Toast.makeText(this, "Add button clicked!", Toast.LENGTH_SHORT).show();

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

            if(timeOperatingEnd.after(timeOperatingStart)){
                // Assuming operatingDay is set somewhere else in your code
                operatingTime newTime = new operatingTime(operatingDay, timeOperatingStart, timeOperatingEnd);
                addOperatingTimeToRecyclerView(newTime);
            }
            else{
                Toast.makeText(this, "End Date must after Start Date!!", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            Toast.makeText(this, "Invalid time format. Please use HH:mm.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
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

    private void addOperatingTimeToRecyclerView(operatingTime time){
        listOperatingTime.add(time);
        recyclerAdapter.notifyDataSetChanged();
        clearField();
    }

    private void clearField(){
        operatingStart.setText("");
        operatingEnd.setText("");
        autoCompleteTextView.setText(null);
        autoCompleteTextView.setFocusable(false);

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

    private void autoFillRestaurantDetail(String restaurantName){

    }


}