package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.CRUD_Image;
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
    DatabaseHelper databaseHelper;
    int busId;

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
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        int uid = postList.get(position).getReviewerId();
        User currentUser = crudUser.getUser(uid);
        Bitmap profilePicture = currentUser.getProfilePicture();
        String uDp = postList.get(position).getuDp();
        String uName = currentUser.getUsername();
        String uEmail = currentUser.getEmail();
        Position userPosition = crudUser.getUserPoistion(uid);
        String[] tag = postList.get(position).getTags();
        String pTitle = postList.get(position).getReviewTitle();
        String pDescription = postList.get(position).getReviewText();
        String pImage = postList.get(position).getpImage();
        String postTime = postList.get(position).getTimestamp();
        float rating = postList.get(position).getReviewRating();

        if(tag != null){
            String itemTags = "";
            for (int i = 0; i < tag.length; i++) {
                itemTags += "#";
                itemTags += tag[i];
                itemTags += " ";
            }
            holder.itemTags.setText(itemTags);
        }else{
            holder.itemTags.setVisibility(View.GONE);
        }

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

//        Calendar calendar = Calendar.getInstance(Locale.getDefault());
//        calendar.setTimeInMillis(Long.parseLong(postTime));
//        String pTime = DateFormat.format("dd/mm/yyyy hh:mm aa", calendar).toString();


//        set Data
        holder.uNameTv.setText(uName);
        holder.pTitleTv.setText(pTitle);
        holder.pDescriptionTv.setText(pDescription);
        holder.ratingBar.setRating(rating);

        if(profilePicture != null){
            Log.e("Profile Pic", String.valueOf(profilePicture));
            holder.uPictureIv.setImageBitmap(Bitmap.createScaledBitmap(profilePicture, 50, 50, false));
        }

        try{
            if(userPosition.getBusinessID() == busId){
                holder.posTagTv.setText(userPosition.getPositionName());
            }else{
                holder.posTagTv.setVisibility(View.GONE);
            }
        }catch (Exception e){

        }


//        set post image
        if(pImage == null){
            holder.pImageIv.setVisibility(View.GONE);
        }else{
            try{
                Picasso.get().load(pImage).into(holder.pImageIv);
            }catch (Exception e){
            }
        }


//        handle button click
        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "More", Toast.LENGTH_SHORT).show();
            }
        });

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Like", Toast.LENGTH_SHORT).show();
            }
        });

        holder.commentBVtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Comment", Toast.LENGTH_SHORT).show();
            }
        });

        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Share", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView uPictureIv, pImageIv;
        TextView uNameTv, pTimeTv, pTitleTv, pDescriptionTv, pLikesTv, posTagTv, itemTags;
        ImageButton moreBtn;
        Button likeBtn, commentBVtn, shareBtn;
        RatingBar ratingBar;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            uPictureIv = itemView.findViewById(R.id.uPictureIv);
            pImageIv = itemView.findViewById(R.id.pImageTv);
            uNameTv = itemView.findViewById(R.id.uNameTv);
            pTimeTv = itemView.findViewById(R.id.pTimeTv);
            pTitleTv = itemView.findViewById(R.id.pTitleTv);
            pDescriptionTv = itemView.findViewById(R.id.pDescriptionTv);
            pLikesTv = itemView.findViewById(R.id.pLikesTv);
            moreBtn = itemView.findViewById(R.id.moreBtn);
            likeBtn = itemView.findViewById(R.id.agreeBtn);
            commentBVtn = itemView.findViewById(R.id.commentBtn);
            shareBtn = itemView.findViewById(R.id.disagreeBtn);
            ratingBar = itemView.findViewById(R.id.detailReviewRating);
            posTagTv = itemView.findViewById(R.id.positionTag);
            itemTags = itemView.findViewById(R.id.itemTag);

        }
    }


}