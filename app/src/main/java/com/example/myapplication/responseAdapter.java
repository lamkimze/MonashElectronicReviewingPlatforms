package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.CRUD_Review;
import com.example.myapplication.Database.CRUD_User;
import com.example.myapplication.Database.DatabaseHelper;
import com.example.myapplication.Entities.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class responseAdapter extends RecyclerView.Adapter<responseAdapter.MyHolder> {

    Context context;
    List<Response> commentList;
    DatabaseHelper dbHelper;
    CRUD_User crudUser;
    CRUD_Review crudReview;
    int busId;

    public responseAdapter(Context context, List<Response> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    class MyHolder extends  RecyclerView.ViewHolder{

        ImageView avaTarIv;
        TextView nameTv, commentTv, timeTv, positionTag;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            avaTarIv = itemView.findViewById(R.id.AvatarIv);
            nameTv = itemView.findViewById(R.id.nameTv);
            commentTv = itemView.findViewById(R.id.commentTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            positionTag = itemView.findViewById(R.id.responsePositionTag);
        }

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_comments, parent, false);
        try{
            dbHelper = new DatabaseHelper(parent.getContext());
            crudReview = new CRUD_Review(dbHelper);
            crudUser = new CRUD_User(dbHelper);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        int uid = commentList.get(position).getUserID();
        String text = commentList.get(position).getResponseText();
        User user = crudUser.getUser(uid);
        Position userPosition = crudUser.getUserPoistion(uid);
        String userName = user.getUsername();
        String timeStamp = commentList.get(position).getResponseDate();
        Bitmap profilePicture = user.getProfilePicture();

        //        convert timestamp to dd/mm/yyyy hh:mm am/pm
        try {
            SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date currentDate = new Date();
            long differenceInMillis;
            if(timeStamp == null){
                long tempTime = new Date().getTime();
                differenceInMillis = currentDate.getTime() - tempTime;
            }else{
                Date postDate = dateTime.parse(timeStamp);
                differenceInMillis = currentDate.getTime() - postDate.getTime();
            }

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

            holder.timeTv.setText(timeAgo);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        holder.nameTv.setText(userName);
        holder.commentTv.setText(text);

        if(profilePicture != null){
            holder.avaTarIv.setImageBitmap(Bitmap.createScaledBitmap(profilePicture, 40, 40, false));
        }

        try{
            if(userPosition != null && userPosition.getBusinessID() == busId){
                holder.positionTag.setText(userPosition.getPositionName());
            }else{
                holder.positionTag.setVisibility(View.GONE);
            }
        }catch (Exception e){
        }
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

}
