package com.example.myapplication;

import android.graphics.Bitmap;
import android.transition.Slide;

public class SlideItem {

    private Bitmap image;

    SlideItem(Bitmap image){
        this.image = image;
    }

    public Bitmap getImage(){
        return image;
    }
}
