package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class dashboardReviewCardAdapter extends RecyclerView.Adapter<dashboardReviewCardAdapter.ReviewHolder> {


    // CardAdapter Class
    private Context context2;
    private ArrayList<dashboardReviewCard> reviews;

    // Constructor
    public dashboardReviewCardAdapter(Context context2, ArrayList<dashboardReviewCard> reviews) {
        this.context2 = context2;
        this.reviews = reviews;
    }
    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context2).inflate(R.layout.review_card_dashboard, parent, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        dashboardReviewCard review = reviews.get(position);
        holder.setDetails(review);
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
        Button replyButton;
        Button reportButton;
        RatingBar ratingBar;
        TextView usernameView;
        TextView commentReviewContentView;
        TextView dateView;





        ReviewHolder(View itemView){
            super(itemView);
            profilePicView = itemView.findViewById(R.id.userPhoto);
            titleView = itemView.findViewById(R.id.CommentReviewTitle);
            ratingBar = itemView.findViewById(R.id.detailReviewRating);
            usernameView = itemView.findViewById(R.id.reviewUsername);
            commentReviewContentView = itemView.findViewById(R.id.commentReviewContent);
            dateView = itemView.findViewById(R.id.commentTimeStamp);

        }

        void setDetails(dashboardReviewCard review){
            profilePicView.setImageBitmap(review.getProfilePic());
            titleView.setText(review.getReviewTitle());
            ratingBar.setRating(review.getReviewRating());
            usernameView.setText(review.getReviewUsername());
            commentReviewContentView.setText(review.getReviewText());
            dateView.setText(review.getReviewDate());

        }
    }


}
