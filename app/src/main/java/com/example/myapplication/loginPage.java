package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.example.myapplication.Database.CRUD_User;
import com.example.myapplication.Database.DatabaseHelper;
import com.example.myapplication.Entities.User;
import com.example.myapplication.databinding.ActivityLoginPageBinding;
import com.google.gson.Gson;

public class loginPage extends AppCompatActivity {
    ActivityLoginPageBinding binding;
    String stringUserName, stringPassword;
    DatabaseHelper dbHelper;
    CRUD_User crudUser;
    User loginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {
            dbHelper = new DatabaseHelper(this);
            crudUser = new CRUD_User(dbHelper); // or whatever initialization is required
        }catch (Exception e) {
            e.printStackTrace();
        }

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringUserName = binding.loginUsernameInput.getText().toString();
                stringPassword = binding.loginPasswordInput.getText().toString();

                if(stringUserName.equalsIgnoreCase("") || stringPassword.equalsIgnoreCase("")){
                    Toast.makeText(loginPage.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                }
                else{
                    loginUser = new User(stringUserName);
                    if(crudUser.verifyLogin(loginUser, stringPassword)){
                        Toast.makeText(loginPage.this, "Login Successful !!", Toast.LENGTH_LONG).show();
                        sharedPreference();
                        Intent loginIntent = new Intent(getApplicationContext(), dashboardPage.class);
                        startActivity(loginIntent);
                    }
                    else{
                        Toast.makeText(loginPage.this, "Invalid Credential", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }


    public void sharedPreference(){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(loginUser);
        editor.putString("MyObject", json);
        editor.commit();
        editor.apply();
    }


    public void onclickRegisterPersonal(View view){
        Intent personalIntent = new Intent(this, userRegistrationPage.class);
        startActivity(personalIntent);
    }

    public void onClickRegisteredBusiness(View view){
        Intent businessIntent = new Intent(this, businessRegistrationPage.class);
        startActivity(businessIntent);
    }

}