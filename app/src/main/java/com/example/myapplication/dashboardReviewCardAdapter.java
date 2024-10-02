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
import com.example.myapplication.Entities.User;
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
            holder.likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.thumb_up, 0, 0, 0); // Active like icon
        } else {
            holder.likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unclicked_thumbs_up, 0, 0, 0); // Default like icon
        }

        if (disLikeList.contains(userId)) {
            holder.dislikeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.thumb_down, 0, 0, 0); // Active dislike icon
        } else {
            holder.dislikeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.uncliked_thumbs_down, 0, 0, 0); // Default dislike icon
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
                    holder.likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unclicked_thumbs_up, 0, 0, 0); // Default like icon
                }else{
                    likeList.add(userId);
                    holder.likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.thumb_up, 0, 0, 0); // Active like icon
                    if(disLikeList.contains(userId)){
                        disLikeList.remove(Integer.valueOf(userId));
                        holder.dislikeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unclicked_thumbs_up, 0, 0, 0); // Reset dislike button
                    }
                }
                crudReview.updateLikes(reviews.get(position).getReviewId(), likeList);
                crudReview.updateDisLikes(reviews.get(position).getReviewId(), disLikeList);
                notifyDataSetChanged();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int businessId = review.getBusinessId();
                Context context = holder.itemView.getContext();
                Intent reviewDetail = new Intent(context, samplePostDetailActivity.class);
                reviewDetail.putExtra("busId", businessId);
                reviewDetail.putExtra("userId", userId);
                reviewDetail.putExtra("reviewId", review.getReviewId());
                context.startActivity(reviewDetail);
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
                    holder.dislikeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.uncliked_thumbs_down, 0, 0,  0); // Default like icon
                }else{
                    disLikeList.add(userId);
                    holder.dislikeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.thumb_down, 0, 0, 0); // Active like icon
                    if(likeList.contains(userId)){
                        likeList.remove(Integer.valueOf(userId));
                        holder.likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unclicked_thumbs_up, 0, 0, 0); // Reset dislike button
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
        Button likeButton;
        Button dislikeButton;
        Button reportButton;
        RatingBar ratingBar;
        TextView usernameView;
        TextView commentReviewContentView;
        TextView dateView;
        ImageView reviewImage;
        TextView positionTags;


        ReviewHolder(View itemView){
            super(itemView);
            profilePicView = itemView.findViewById(R.id.userPhoto);
            titleView = itemView.findViewById(R.id.CommentReviewTitle);
            ratingBar = itemView.findViewById(R.id.detailReviewRating);
            usernameView = itemView.findViewById(R.id.reviewUsername);
            commentReviewContentView = itemView.findViewById(R.id.commentReviewContent);
            dateView = itemView.findViewById(R.id.commentTimeStamp);
            reviewImage = itemView.findViewById(R.id.commentImages);
            likeButton = itemView.findViewById(R.id.likeButton);
            dislikeButton = itemView.findViewById(R.id.dislikeButton);
            positionTags = itemView.findViewById(R.id.reviewPosition);

        }

        void setDetails(dashboardReviewCard review) {
            profilePicView.setImageBitmap(review.getProfilePic());
            titleView.setText(review.getReviewTitle());
            ratingBar.setRating(review.getReviewRating());
            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
            usernameView.setText(review.getReviewUsername());
            commentReviewContentView.setText(review.getReviewText());

            Position userPosition = crudUser.getUserPoistion(userId);
            if(userPosition != null){
                positionTags.setText(userPosition.getPositionName());
            }else{
                positionTags.setVisibility(View.GONE);
            }

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

            likeButton.setText(String.valueOf(review.getLikes().size()));
            if(review.getLikes().contains(userId)){
                likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.thumb_up, 0, 0, 0); // Default like icon
            }
            dislikeButton.setText(String.valueOf(review.getDislikes().size()));
            if(review.getDislikes().contains(userId)){
                dislikeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.thumb_down, 0, 0, 0); // Default like icon
            }

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
