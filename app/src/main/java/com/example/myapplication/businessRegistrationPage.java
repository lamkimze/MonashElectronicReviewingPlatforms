package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class businessRegistrationPage extends AppCompatActivity {

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

        autoCompleteTextView = findViewById(R.id.auto_complete_textview);

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

            // Assuming operatingDay is set somewhere else in your code
            operatingTime newTime = new operatingTime(operatingDay, timeOperatingStart, timeOperatingEnd);
            addOperatingTimeToRecyclerView(newTime);
        } catch (ParseException e) {
            Toast.makeText(this, "Invalid time format. Please use HH:mm.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
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


}