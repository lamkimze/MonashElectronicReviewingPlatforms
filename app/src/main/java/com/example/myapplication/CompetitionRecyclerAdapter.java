package com.example.myapplication;

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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CompetitionRecyclerAdapter extends RecyclerView.Adapter<CompetitionRecyclerAdapter.CustomViewHolder> {

    List<Restaurant> data = new ArrayList<Restaurant>();

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
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.tvRestaurantName.setText(String.valueOf(data.get(position).getName()));
        holder.tvGoldMedal.setText(String.valueOf(data.get(position).getGoldMedalNo()));
        holder.tvSilverMedal.setText(String.valueOf(data.get(position).getSilverMedalNo()));
        holder.tvBronzeMedal.setText(String.valueOf(data.get(position).getBronzeMedalNo()));
        holder.tvPlaceChange.setText(String.valueOf(data.get(position).getPlaceChange()));

        Picasso.get()
                .load(data.get(position).getPictureUrl())
                .error(R.drawable.default_icon) // Optional: add an error image
                .into(holder.ivRestaurantImage);

        int annotationArrow;
        if(data.get(position).getPlaceChange() == 0){
            annotationArrow = R.drawable.equal;
        }else if(data.get(position).getPlaceChange() > 0){
            annotationArrow = R.drawable.rises;
        }else{
            annotationArrow = R.drawable.drop;
        }

        Bitmap annotationBitmap = BitmapFactory.decodeResource(holder.itemView.getResources(), annotationArrow);
        Bitmap scaledAnnotationBitmap = Bitmap.createScaledBitmap(annotationBitmap, 100, 100, true);
        Drawable annotationDrawable= new BitmapDrawable(holder.itemView.getResources(), scaledAnnotationBitmap);
        holder.ivRankingAnnotation.setImageDrawable(annotationDrawable);
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
