package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.CRUD_Business;
import com.example.myapplication.Database.CRUD_Image;
import com.example.myapplication.Database.CRUD_Review;
import com.example.myapplication.Database.DatabaseHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompetitionRecyclerAdapter extends RecyclerView.Adapter<CompetitionRecyclerAdapter.CustomViewHolder> {

    List<Restaurant> data = new ArrayList<Restaurant>();
    CRUD_Review crudReview;
    CRUD_Business crudBusiness;
    CRUD_Image crudImage;
    DatabaseHelper dbHelper;
    int userid;

    public void setData(ArrayList<Restaurant> data){
        this.data = data;
    }

    public void setFilteredList(List<Restaurant> filteredList){
        this.data = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.competition_card_layout, parent, false);
        CompetitionRecyclerAdapter.CustomViewHolder viewHolder = new CustomViewHolder(v);

        try{
            dbHelper = new DatabaseHelper(parent.getContext());
            crudBusiness = new CRUD_Business(dbHelper);
            crudImage = new CRUD_Image(dbHelper);
            crudReview = new CRUD_Review(dbHelper);
        }catch (Exception e) {
            e.printStackTrace();

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        ranking = data;

        Restaurant currentRestaurant = data.get(position);

        holder.tvRestaurantName.setText(String.valueOf(currentRestaurant.getName()));
        holder.averageRatingBar.setRating(currentRestaurant.getStars());
        holder.tvCuisineType.setText(currentRestaurant.getCuisine());
        holder.tvTotalReviews.setText(String.valueOf(crudReview.getReviews(currentRestaurant.getId()).size()));

        List<String[]> allTags = new ArrayList<>();
        ArrayList<ReviewModel> reviewModels = crudReview.getReviews(currentRestaurant.getId());
        Log.e("busId", String.valueOf(currentRestaurant.getId()));
        if(reviewModels != null){
            for (ReviewModel review: reviewModels) {
                if(review.getTags() != null && review.getTags().length > 0){
                    allTags.add(review.getTags());
                }
            }
        }
        Log.e("reviews", allTags.toString());



        Map<String, Integer> tagCountMap = new HashMap<>();

        // Count the frequency of each tag
        for (String[] tags : allTags) {
            for (String tag : tags) {
                tagCountMap.put(tag, tagCountMap.getOrDefault(tag, 0) + 1);
            }
        }

        // Create a list from the map entries and sort by count
        List<Map.Entry<String, Integer>> sortedTags = new ArrayList<>(tagCountMap.entrySet());
        sortedTags.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())); // Sort in descending order

        // Retrieve the top N tags
        List<String> topTags = new ArrayList<>();
        for (int i = 0; i < Math.min(3, sortedTags.size()); i++) {
            topTags.add(sortedTags.get(i).getKey());
        }

        // Set the top tags safely
        try{
            holder.tag1.setText(topTags.get(0));
        } catch (Exception e) {
            holder.tag1.setVisibility(View.GONE);
        }

        try{
            holder.tag2.setText(topTags.get(1));
        } catch (Exception e) {
            holder.tag2.setVisibility(View.GONE);
        }

        try {
            holder.tag3.setText(topTags.get(2));
        } catch (Exception e) {
            holder.tag3.setVisibility(View.GONE);
        }



        Bitmap businessImage = crudImage.getBusinessImage(currentRestaurant.getId());
        if (businessImage != null) {
            holder.ivRestaurantImage.setImageBitmap(businessImage);
        } else {
            // Set a placeholder image if the bitmap is null
            holder.ivRestaurantImage.setImageResource(R.drawable.default_icon);
        }

        int annotationArrow;
        if(data.get(position).getPrev_ranking() == 0){
            annotationArrow = R.drawable.equal;
        }else if(data.get(position).getPrev_ranking() > 0){
            annotationArrow = R.drawable.rises;
        }else{
            annotationArrow = R.drawable.drop;
        }

        Bitmap annotationBitmap = BitmapFactory.decodeResource(holder.itemView.getResources(), annotationArrow);
        Bitmap scaledAnnotationBitmap = Bitmap.createScaledBitmap(annotationBitmap, 100, 100, true);
        Drawable annotationDrawable= new BitmapDrawable(holder.itemView.getResources(), scaledAnnotationBitmap);
        holder.ivRankingAnnotation.setImageDrawable(annotationDrawable);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int businessId = data.get(position).getId();
                Context context = holder.itemView.getContext();
                Intent toDetail = new Intent(context, restaurantDetailPage.class);
                toDetail.putExtra("busId", businessId);
                toDetail.putExtra("userId", userid);
                context.startActivity(toDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(this.data != null){
            return this.data.size();
        }
        return 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        public TextView tvRestaurantName;
        public TextView tvCuisineType;
        public TextView tvTotalReviews;
        public TextView tag1, tag2, tag3;
        public RatingBar averageRatingBar;
        public ImageView ivRestaurantImage;
        public ImageView ivRankingAnnotation;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRestaurantName = itemView.findViewById(R.id.restaurantName);
            ivRestaurantImage = itemView.findViewById(R.id.restaurantImage);
            ivRankingAnnotation = itemView.findViewById(R.id.placeChangeAnnotation);
            tvCuisineType = itemView.findViewById(R.id.restaurantListCuisine);
            tvTotalReviews = itemView.findViewById(R.id.totalReviews);
            tag1 = itemView.findViewById(R.id.listTag1);
            tag2 = itemView.findViewById(R.id.listTag2);
            tag3 = itemView.findViewById(R.id.listTag3);
            averageRatingBar = itemView.findViewById(R.id.listRatingBar);

        }

    }

}
