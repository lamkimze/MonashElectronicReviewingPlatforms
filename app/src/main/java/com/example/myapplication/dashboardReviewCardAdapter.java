package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.CRUD_Image;
import com.example.myapplication.Database.CRUD_Review;
import com.example.myapplication.Database.CRUD_User;
import com.example.myapplication.Database.DatabaseHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class dashboardReviewCardAdapter extends RecyclerView.Adapter<dashboardReviewCardAdapter.ReviewHolder> {


    // CardAdapter Class
    private Context context2;
    private ArrayList<dashboardReviewCard> reviews;
    CRUD_User crudUser;
    CRUD_Image crudImage;
    CRUD_Review crudReview;
    DatabaseHelper databaseHelper;
    private int userId;

    // Constructor
    public dashboardReviewCardAdapter(Context context2, ArrayList<dashboardReviewCard> reviews, int userId) {
        this.context2 = context2;
        this.reviews = reviews;
        this.userId = userId;
    }
    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context2).inflate(R.layout.review_card_dashboard, parent, false);
        try {
            databaseHelper = new DatabaseHelper(parent.getContext());
            crudUser = new CRUD_User(databaseHelper);
            crudImage = new CRUD_Image(databaseHelper);
            crudReview = new CRUD_Review(databaseHelper);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, @SuppressLint("RecyclerView") int position) {
        dashboardReviewCard review = reviews.get(position);
        holder.setDetails(review);

        ArrayList<Integer> likeList = reviews.get(position).getLikes();
        ArrayList<Integer> disLikeList = reviews.get(position).getDislikes();

        if (likeList.contains(userId)) {
            holder.likeButton.setImageResource(R.drawable.thumb_up); // Active like icon
        } else {
            holder.likeButton.setImageResource(R.drawable.unclicked_thumbs_up); // Default like icon
        }

        if (disLikeList.contains(userId)) {
            holder.dislikeButton.setImageResource(R.drawable.thumb_down); // Active dislike icon
        } else {
            holder.dislikeButton.setImageResource(R.drawable.uncliked_thumbs_down); // Default dislike icon
        }

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Integer> likeList = reviews.get(position).getLikes();
                ArrayList<Integer> disLikeList = reviews.get(position).getDislikes();
//                Log.e("likes inside", String.valueOf(likeList.contains(userId)));

//                user liked the review and tap again
                if(likeList.contains(userId)){
                    likeList.remove(Integer.valueOf(userId));
                    holder.likeButton.setImageResource(R.drawable.unclicked_thumbs_up); // Default like icon
                }else{
                    likeList.add(userId);
                    holder.likeButton.setImageResource(R.drawable.thumb_up); // Active like icon
                    if(disLikeList.contains(userId)){
                        disLikeList.remove(Integer.valueOf(userId));
                        holder.dislikeButton.setImageResource(R.drawable.unclicked_thumbs_up); // Reset dislike button
                    }
                }
                crudReview.updateLikes(reviews.get(position).getReviewId(), likeList);
                crudReview.updateDisLikes(reviews.get(position).getReviewId(), disLikeList);
                notifyDataSetChanged();
            }
        });


        holder.dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Integer> likeList = reviews.get(position).getLikes();
                ArrayList<Integer> disLikeList = reviews.get(position).getDislikes();
//                Log.e("likes inside", String.valueOf(likeList.contains(userId)));

//                user liked the review and tap again
                if(disLikeList.contains(userId)){
                    disLikeList.remove(Integer.valueOf(userId));
                    holder.dislikeButton.setImageResource(R.drawable.uncliked_thumbs_down); // Default like icon
                }else{
                    disLikeList.add(userId);
                    holder.dislikeButton.setImageResource(R.drawable.thumb_down); // Active like icon
                    if(likeList.contains(userId)){
                        likeList.remove(Integer.valueOf(userId));
                        holder.likeButton.setImageResource(R.drawable.unclicked_thumbs_up); // Reset dislike button
                    }
                }
                crudReview.updateLikes(reviews.get(position).getReviewId(), likeList);
                crudReview.updateDisLikes(reviews.get(position).getReviewId(), disLikeList);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    // View holder class whose objects represent each list item
    class ReviewHolder extends RecyclerView.ViewHolder {
        private ImageView profilePicView;
        private TextView titleView;
        TextView ratingView;
        TextView textView;
        ImageButton likeButton;
        ImageButton dislikeButton;
        Button replyButton;
        Button reportButton;
        RatingBar ratingBar;
        TextView usernameView;
        TextView commentReviewContentView;
        TextView dateView;
        ImageView reviewImage;
        TextView businessName;
        TextView likeCount;
        TextView dislikeCount;





        ReviewHolder(View itemView){
            super(itemView);
            profilePicView = itemView.findViewById(R.id.userPhoto);
            titleView = itemView.findViewById(R.id.CommentReviewTitle);
            ratingBar = itemView.findViewById(R.id.detailReviewRating);
            usernameView = itemView.findViewById(R.id.reviewUsername);
            commentReviewContentView = itemView.findViewById(R.id.commentReviewContent);
            dateView = itemView.findViewById(R.id.commentTimeStamp);
            reviewImage = itemView.findViewById(R.id.commentImages);
            businessName = itemView.findViewById(R.id.reviewTags);
            likeCount = itemView.findViewById(R.id.likeCounter);
            dislikeCount = itemView.findViewById(R.id.dislikeCounters);
            likeButton = itemView.findViewById(R.id.likeButton);
            dislikeButton = itemView.findViewById(R.id.dislikeButton);

        }

        void setDetails(dashboardReviewCard review) {
            profilePicView.setImageBitmap(review.getProfilePic());
            titleView.setText(review.getReviewTitle());
            ratingBar.setRating(review.getReviewRating());
            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
            usernameView.setText(review.getReviewUsername());
            commentReviewContentView.setText(review.getReviewText());
//            dateView.setText(review.getReviewDate());
            try {
                SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date postDate = dateTime.parse(review.getReviewDate());
                Date currentDate = new Date();
                long differenceInMillis = currentDate.getTime() - postDate.getTime();

                long seconds = differenceInMillis / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                long days = hours / 24;

                String timeAgo;

                if(seconds < 60){
                    timeAgo = seconds + " seconds ago";
                }else if (minutes < 60) {
                    timeAgo = minutes + " minutes ago";
                } else if (hours < 24) {
                    timeAgo = hours + " hours ago";
                } else {
                    timeAgo = days + " days ago";
                }

                dateView.setText(timeAgo);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            businessName.setText(review.getBusinessName());

            likeCount.setText(String.valueOf(review.getLikes().size()));
            dislikeCount.setText(String.valueOf(review.getDislikes().size()));

            // Handle review images
            ArrayList<Bitmap> reviewImages = review.getReviewImages();
            if (reviewImages != null && !reviewImages.isEmpty()) {
                // If there are images, set the first image in the array
                reviewImage.setImageBitmap(reviewImages.get(0));
                reviewImage.setVisibility(View.VISIBLE); // Ensure the image view is visible

            } else {
                // If no images are found, make the ImageView invisible
                reviewImage.setVisibility(View.GONE); // Or View.INVISIBLE based on your design preference
            }
        }


    }



}
