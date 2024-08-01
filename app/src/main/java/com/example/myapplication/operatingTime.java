package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.util.Log;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class operatingTime {
    private String day;
    private Date startTime;
    private Date endTime;

    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public operatingTime(String day, Date startTime, Date endTime) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        Log.e(TAG, "operatingTime:" + startTime);
        Log.e(TAG, "operatingTime:" + endTime);
    }

    public String getDay() {
        return day;
    }

    public String getStartTime() {
        return timeFormat.format(startTime);
    }

    public String getEndTime() {
        return timeFormat.format(endTime);
    }

}
