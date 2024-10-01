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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.CRUD_Business;
import com.example.myapplication.Database.CRUD_Image;
import com.example.myapplication.Database.DatabaseHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompetitionRecyclerAdapter extends RecyclerView.Adapter<CompetitionRecyclerAdapter.CustomViewHolder> {

    List<Restaurant> data = new ArrayList<Restaurant>();
    List<Restaurant> ranking = new ArrayList<>();
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
        }catch (Exception e) {
            e.printStackTrace();

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        ranking = data;
        holder.tvRestaurantName.setText(String.valueOf(data.get(position).getName()));
        holder.tvGoldMedal.setText(String.valueOf(data.get(position).getGoldMedalNo()));
        holder.tvSilverMedal.setText(String.valueOf(data.get(position).getSilverMedalNo()));
        holder.tvBronzeMedal.setText(String.valueOf(data.get(position).getBronzeMedalNo()));

//        int current_ranking = -999;
//        ranking.sort(Restaurant.dailyReview);

//        for (int i = 0; i < ranking.size(); i++) {
//            if(ranking.get(i).getId() == data.get(position).getId()){
//                current_ranking = ranking.get(i).getPrev_ranking() + 1;
//                break;
//            }
//
//        }
//        holder.tvPlaceChange.setText(String.valueOf(current_ranking - data.get(position).getPrev_ranking()));

        Restaurant currentRestaurant = data.get(position);
//        currentRestaurant.setPrev_ranking(current_ranking);
//        crudBusiness.updateCompetitionDetail(data.get(position).getId(), currentRestaurant);


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
        public TextView tvGoldMedal;
        public TextView tvSilverMedal;
        public TextView tvBronzeMedal;
        public TextView tvPlaceChange;
        public ImageView ivRestaurantImage;
        public ImageView ivRankingAnnotation;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRestaurantName = itemView.findViewById(R.id.restaurantName);
            tvGoldMedal = itemView.findViewById(R.id.goldMedal);
            tvSilverMedal = itemView.findViewById(R.id.silverMedal);
            tvBronzeMedal = itemView.findViewById(R.id.bronzeMedal);
            tvPlaceChange = itemView.findViewById(R.id.placeChange);
            ivRestaurantImage = itemView.findViewById(R.id.restaurantImage);
            ivRankingAnnotation = itemView.findViewById(R.id.placeChangeAnnotation);
        }

    }

}
