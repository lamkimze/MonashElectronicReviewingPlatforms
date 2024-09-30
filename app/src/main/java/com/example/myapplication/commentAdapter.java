package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.CRUD_Image;
import com.example.myapplication.Database.CRUD_Review;
import com.example.myapplication.Database.CRUD_User;
import com.example.myapplication.Database.DatabaseHelper;
import com.example.myapplication.Entities.User;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.MyHolder> {
    Context context;
    List<ReviewModel> postList = new ArrayList<>();
    CRUD_User crudUser;
    CRUD_Image crudImage;
    CRUD_Review crudReview;
    DatabaseHelper databaseHelper;
    int busId, userId;

    public commentAdapter() {
    }

    public void setData(ArrayList<ReviewModel> data){
        this.postList = data;
    }

    public void setFilteredList(List<ReviewModel> filteredList){
        this.postList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_posts, parent, false);
        try {
            databaseHelper = new DatabaseHelper(parent.getContext());
            crudUser = new CRUD_User(databaseHelper);
            crudImage = new CRUD_Image(databaseHelper);
            crudReview = new CRUD_Review(databaseHelper);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        int uid = postList.get(position).getReviewerId();
        User currentUser = crudUser.getUser(uid);
        Bitmap profilePicture = currentUser.getProfilePicture();
        String uName = currentUser.getUsername();
        Position userPosition = crudUser.getUserPoistion(uid);
        String pTitle = postList.get(position).getReviewTitle();
        String pImage = postList.get(position).getpImage();
        String postTime = postList.get(position).getTimestamp();
        float rating = postList.get(position).getReviewRating();
        int commentNo = crudReview.getResponses(postList.get(position).reviewId).size();
        int likesNo = postList.get(position).getLikes().size();
        int disLikesNo = postList.get(position).getDislike().size();

//        convert timestamp to dd/mm/yyyy hh:mm am/pm
        try {
            SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date postDate = dateTime.parse(postTime);
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

            holder.pTimeTv.setText(timeAgo);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


//        set Data
        holder.uNameTv.setText(uName);
        holder.pTitleTv.setText(pTitle);
        holder.ratingBar.setRating(rating);
        holder.likeBtn.setText(String.valueOf(likesNo) + " Likes ");
        holder.commentBtn.setText(String.valueOf(commentNo) + " Comments ");
        holder.disagreeBtn.setText(String.valueOf(disLikesNo) + " Dislikes ");

        ArrayList<Integer> likeList = postList.get(position).getLikes();
        ArrayList<Integer> disLikeList = postList.get(position).getDislike();

        if (likeList.contains(userId)) {
            holder.likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.thumb_up, 0, 0, 0); // Active like icon
        } else {
            holder.likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unclicked_thumbs_up, 0, 0, 0); // Default like icon
        }

        if (disLikeList.contains(userId)) {
            holder.disagreeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.thumb_down, 0, 0, 0); // Active dislike icon
        } else {
            holder.disagreeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.uncliked_thumbs_down, 0, 0, 0); // Default dislike icon
        }


        if(profilePicture != null){
            Log.e("Profile Pic", String.valueOf(profilePicture));
            holder.uPictureIv.setImageBitmap(Bitmap.createScaledBitmap(profilePicture, 50, 50, false));
        }

        try{
            if(userPosition != null &&userPosition.getBusinessID() == busId){
                holder.posTagTv.setText(userPosition.getPositionName());
            }else{
                holder.posTagTv.setVisibility(View.GONE);
            }
        }catch (Exception e){
        }



//        handle button click
        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreOptions(holder.moreBtn, uid, userId, postList.get(position).getReviewId(), pImage);
            }
        });

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Integer> likeList = postList.get(position).getLikes();
                ArrayList<Integer> disLikeList = postList.get(position).getDislike();
//                Log.e("likes inside", String.valueOf(likeList.contains(userId)));

//                user liked the review and tap again
                if(likeList.contains(userId)){
                    likeList.remove(Integer.valueOf(userId));
                    holder.likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unclicked_thumbs_up, 0, 0, 0); // Default like icon
                }else{
                    likeList.add(userId);
                    holder.likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.thumb_up, 0, 0, 0); // Active like icon
                    if(disLikeList.contains(userId)){
                        disLikeList.remove(Integer.valueOf(userId));
                        holder.disagreeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.uncliked_thumbs_down, 0, 0, 0); // Reset dislike button
                    }
                }
                crudReview.updateLikes(postList.get(position).getReviewId(), likeList);
                crudReview.updateDisLikes(postList.get(position).getReviewId(), disLikeList);
                notifyDataSetChanged();
            }
        });

        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reviewDetailIntent = new Intent(context, samplePostDetailActivity.class);
                reviewDetailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                reviewDetailIntent.putExtra("busId", busId);
                reviewDetailIntent.putExtra("userId", userId);
                reviewDetailIntent.putExtra("reviewId", postList.get(position).getReviewId());
                context.startActivity(reviewDetailIntent);
            }
        });

        holder.disagreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Integer> likeList = postList.get(position).getLikes();
                ArrayList<Integer> disLikeList = postList.get(position).getDislike();
//                Log.e("likes inside", String.valueOf(likeList.contains(userId)));

//                user liked the review and tap again
                if(disLikeList.contains(userId)){
                    disLikeList.remove(Integer.valueOf(userId));
                    holder.disagreeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.uncliked_thumbs_down, 0, 0, 0); // Default like icon
                }else{
                    disLikeList.add(userId);
                    holder.disagreeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.thumb_down, 0, 0, 0); // Active like icon
                    if(likeList.contains(userId)){
                        likeList.remove(Integer.valueOf(userId));
                        holder.likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unclicked_thumbs_up, 0, 0, 0); // Reset dislike button
                    }
                }
                crudReview.updateLikes(postList.get(position).getReviewId(), likeList);
                crudReview.updateDisLikes(postList.get(position).getReviewId(), disLikeList);
                notifyDataSetChanged();
            }
        });
    }

    private void setLikes(final MyHolder holder, final String postKey){
    }

    private void showMoreOptions(ImageButton moreBtn, int userId, int myId, final int pId, final String pImage){
        PopupMenu popupMenu = new PopupMenu(context, moreBtn, Gravity.END);

        if(userId == myId){
            popupMenu.getMenu().add(Menu.NONE, 0, 0, "Delete");
            popupMenu.getMenu().add(Menu.NONE, 1, 0, "Edit");

        }
        popupMenu.getMenu().add(Menu.NONE, 2, 0, "View Detail");

        popupMenu.setOnMenuItemClickListener((menuItem -> {
            int id = menuItem.getItemId();
            if(id == 0){
                beginDelete(pId);
            }
            else if(id == 1){
                Intent intent = new Intent(context, userReviewCreationPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("key", "editPost");
                intent.putExtra("reviewId", pId);
                intent.putExtra("busId", busId);
                intent.putExtra("userId", userId);
                context.startActivity(intent);
            }
            else if(id == 2){
                Intent reviewDetailIntent = new Intent(context, samplePostDetailActivity.class);
                reviewDetailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                reviewDetailIntent.putExtra("busId", busId);
                reviewDetailIntent.putExtra("userId", userId);
                reviewDetailIntent.putExtra("reviewId", pId);
                context.startActivity(reviewDetailIntent);
            }
            return false;
        }));

        popupMenu.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void beginDelete(int pId) {
        crudReview.deleteReview(pId);
        crudImage.deleteReviewImages(pId);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView uPictureIv;
        TextView uNameTv, pTimeTv, pTitleTv, posTagTv;
        ImageButton moreBtn;
        Button likeBtn, commentBtn, disagreeBtn;
        RatingBar ratingBar;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            uPictureIv = itemView.findViewById(R.id.uPictureIv);
            uNameTv = itemView.findViewById(R.id.uNameTv);
            pTimeTv = itemView.findViewById(R.id.pTimeTv);
            pTitleTv = itemView.findViewById(R.id.pTitleTv);
            moreBtn = itemView.findViewById(R.id.moreBtn);
            likeBtn = itemView.findViewById(R.id.agreeBtn);
            commentBtn = itemView.findViewById(R.id.commentBtn);
            disagreeBtn = itemView.findViewById(R.id.disagreeBtn);
            ratingBar = itemView.findViewById(R.id.detailReviewRating);
            posTagTv = itemView.findViewById(R.id.positionTag);
        }
    }


}