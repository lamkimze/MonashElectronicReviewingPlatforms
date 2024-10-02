package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class R_RecyclerViewAdapter extends RecyclerView.Adapter<R_RecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<ReviewModel> reviewModels;

    public R_RecyclerViewAdapter(Context context, ArrayList<ReviewModel> reviewModels){
        this.context = context;
        this .reviewModels = reviewModels;
    }
    /**
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @NonNull
    @Override
    public R_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_card_layout,parent,false);
        return new R_RecyclerViewAdapter.MyViewHolder(view);
    }

    /**
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull R_RecyclerViewAdapter.MyViewHolder holder, int position) {
        ReviewModel review = reviewModels.get(position);
        holder.reviewTitle.setText(review.getReviewTitle());
        holder.reviewRating.setRating(review.getReviewRating());

        // Ensure the images list is not empty before accessing it
        if (!review.getReviewImages().isEmpty()) {
            Bitmap imageBitmap = review.getReviewImages().get(0); // Assuming this returns a Bitmap

            // Set the Bitmap directly
            holder.imageView.setImageBitmap(imageBitmap);
        } else {
            // Handle the case where there are no images
            holder.imageView.setImageResource(R.drawable.boostjuice); // Replace with your default image resource
        }

    }

    /**
     * @return
     */
    @Override
    public int getItemCount() {
        return reviewModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView reviewTitle;
        RatingBar reviewRating;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.reviewImageView);
            reviewTitle = itemView.findViewById(R.id.reviewTextView);
            reviewRating = itemView.findViewById(R.id.reviewRatingBar);
        }
    }
}
