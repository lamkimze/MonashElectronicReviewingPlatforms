package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class loginPage extends AppCompatActivity {
    EditText etUserName, etPassword;
    String registeredUsername, registeredPassword, stringUserName, stringPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        restoredUserData();
//        etUserName = findViewById(R.id.loginUsernameInput);
        etPassword = findViewById(R.id.loginPasswordInput);


    }

    public void onClickLogin(View view){
        stringUserName = etUserName.getText().toString();
        stringPassword = etPassword.getText().toString();
        if(registeredUsername.equals(stringUserName) && registeredPassword.equals(stringPassword)){
            Toast.makeText(this, "Login Successful !!", Toast.LENGTH_LONG).show();
            Intent loginIntent = new Intent(this, dashboardPage.class);
            startActivity(loginIntent);
        }
    }

    public void onClickRegister(View view){
        Intent registerIntent = new Intent(this, userRegistrationPage.class);
        startActivity(registerIntent);
    }

    public void restoredUserData(){
        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATAFILE", MODE_PRIVATE);
        registeredUsername = sharedPreferences.getString("USERNAME", "DEFAULT_VALUE");
        registeredPassword = sharedPreferences.getString("PASSWORD", "DEFAULT_VALUE");

    }
}