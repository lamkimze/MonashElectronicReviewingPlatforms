package com.example.myapplication;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.CRUD_Business;
import com.example.myapplication.Database.CRUD_Image;
import com.example.myapplication.Database.CRUD_Review;
import com.example.myapplication.Database.CRUD_User;
import com.example.myapplication.Database.DatabaseHelper;
import com.example.myapplication.Entities.User;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class samplePostDetailActivity extends AppCompatActivity {

    String myUid, myEmail, myName, myDp, pLikes, hisDp, hisName;
    DatabaseHelper dbHelper;
    CRUD_Review crudReview;
    CRUD_Business crudBusiness;
    CRUD_Image crudImage;
    CRUD_User crudUser;
    ReviewModel currentReview;
    User currentUser, postUser;
    RatingBar ratingReview;
    RecyclerView imageView;

    List<Response> responseList;
    responseAdapter responseAdapter;
    showImages recyclerAdapter;

    int postId, myId, busId;

    ImageView uPictureIv;
    TextView uNameTv, pTimeTv, pTitleTv, pDescriptionTv, pTags, pPosition, pCommentTv;
    ImageButton moreBtn;
    Button likeBtn, disLikeBtn;
    LinearLayout profileLayout;
    RecyclerView recyclerView;

    EditText commentEt;
    ImageButton sendBtn;
    ImageView cAvatarTv;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sample_post_detail);

        // Find the Toolbar in the layout and set it as the ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        actionbar and its properties
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("Post Detail");
//        toolbar.setDisplayShowHomeEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);

        postId = getIntent().getExtras().getInt("reviewId");
        myId = getIntent().getExtras().getInt("userId");
        busId = getIntent().getExtras().getInt("busId");

        uPictureIv = findViewById(R.id.uPictureIv);
        uNameTv = findViewById(R.id.uNameTv);
        pTimeTv = findViewById(R.id.pTimeTv);
        pTitleTv = findViewById(R.id.pTitleTv);
        pDescriptionTv = findViewById(R.id.pDescriptionTv);
        pTags = findViewById(R.id.itemTag);
        pPosition = findViewById(R.id.positionTag);
        ratingReview = findViewById(R.id.detailReviewRating);
        moreBtn = findViewById(R.id.moreBtn);
        likeBtn = findViewById(R.id.agreeBtn);
        disLikeBtn = findViewById(R.id.disagreeBtn);
        profileLayout = findViewById(R.id.detailedProfileLayout);
        pCommentTv = findViewById(R.id.commentNo);
        recyclerView = findViewById(R.id.recyclerView);
        imageView = findViewById(R.id.pImageTv);

        commentEt = findViewById(R.id.commentEt);
        sendBtn = findViewById(R.id.sendBtn);
        cAvatarTv = findViewById(R.id.cAvatarIv);

        loadPostInfo();
        loadUserInfo();
        loadComments();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postComment();
            }
        });

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Integer> likeList = currentReview.getLikes();
                ArrayList<Integer> disLikeList = currentReview.getDislike();
//                Log.e("likes inside", String.valueOf(likeList.contains(userId)));

//                user liked the review and tap again
                if(likeList.contains(myId)){
                    likeList.remove(Integer.valueOf(myId));
                    likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unclicked_thumbs_up, 0, 0, 0); // Default like icon
                }else{
                    likeList.add(myId);
                    likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.thumb_up, 0, 0, 0); // Active like icon
                    if(disLikeList.contains(myId)){
                        disLikeList.remove(Integer.valueOf(myId));
                        disLikeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.uncliked_thumbs_down, 0, 0, 0); // Reset dislike button
                    }
                }
                crudReview.updateLikes(postId, likeList);
                crudReview.updateDisLikes(postId, disLikeList);
                refreshPostData();
            }
        });

        disLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Integer> likeList = currentReview.getLikes();
                ArrayList<Integer> disLikeList = currentReview.getDislike();
//                Log.e("likes inside", String.valueOf(likeList.contains(userId)));

//                user liked the review and tap again
                if(disLikeList.contains(myId)){
                    disLikeList.remove(Integer.valueOf(myId));
                    disLikeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.uncliked_thumbs_down, 0, 0, 0); // Default like icon
                }else{
                    disLikeList.add(myId);
                    disLikeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.thumb_down, 0, 0, 0); // Active like icon
                    if(likeList.contains(myId)){
                        likeList.remove(Integer.valueOf(myId));
                        likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unclicked_thumbs_up, 0, 0, 0); // Reset dislike button
                    }
                }
                crudReview.updateLikes(postId, likeList);
                crudReview.updateDisLikes(postId, disLikeList);
                refreshPostData();
            }
        });
    }

    private void loadComments() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        List<Response> ownerResponse = new ArrayList<>();
        List<Response> notOwnerResponse = new ArrayList<>();
        responseList = crudReview.getResponses(postId);
        for (Response response: responseList) {
            int userId = response.getUserID();
            Position userPosition = crudUser.getUserPoistion(userId);
            if(userPosition != null){
                if(userPosition.getPositionName().equalsIgnoreCase("Owner") && userPosition.getBusinessID() == busId){
                    Log.e("Position", userPosition.getPositionName());
                    Log.e("Bus id", String.valueOf(userPosition.getBusinessID()));
                    ownerResponse.add(response);
                }else{
                    Log.e("Position", userPosition.getPositionName());
                    Log.e("Bus id", String.valueOf(userPosition.getBusinessID()));
                    notOwnerResponse.add(response);
                }
            }else{
                Log.e("Position", "position is null");
                Log.e("Bus id", "bus id is null");
                notOwnerResponse.add(response);
            }

        }
        responseList = ownerResponse;
        responseList.addAll(notOwnerResponse);
        responseAdapter = new responseAdapter(getApplicationContext(), responseList);
        responseAdapter.busId = busId;
        recyclerView.setAdapter(responseAdapter);
        refreshPostData();

    }

    private void refreshPostData() {
        // Reload the current review data from the database
        currentReview = crudReview.getReview(postId);

        // Update the like and dislike button text
        likeBtn.setText(String.valueOf(currentReview.getLikes().size()) + " Likes");
        disLikeBtn.setText(String.valueOf(currentReview.getDislike().size()) + " Dislikes");

        // Update the icons based on whether the user has liked or disliked the post
        if (currentReview.getLikes().contains(myId)) {
            likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.thumb_up, 0, 0, 0); // Active like icon
        } else {
            likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unclicked_thumbs_up, 0, 0, 0); // Default like icon
        }

        if (currentReview.getDislike().contains(myId)) {
            disLikeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.thumb_down, 0, 0, 0); // Active dislike icon
        } else {
            disLikeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.uncliked_thumbs_down, 0, 0, 0); // Default dislike icon
        }
    }


    private void postComment() {
        String comment = commentEt.getText().toString().trim();
        if (TextUtils.isEmpty(comment)) {
            Toast.makeText(this, "Comment is empty...", Toast.LENGTH_SHORT).show();
            return;
        }
        // Add the comment to the database
        Response response = new Response(postId, currentUser.getId(), comment);
        crudReview.createResponse(response);

        // Fetch the updated list of comments (responses)
        responseList.add(response); // Add the new comment to the local list

        // Notify the adapter that a new item has been inserted
        responseAdapter.notifyItemInserted(responseList.size() - 1);

        // Scroll to the bottom of the RecyclerView
        recyclerView.scrollToPosition(responseList.size() - 1);

        // Clear the comment input field and update the comment count
        commentEt.setText("");
        pCommentTv.setText(responseList.size() + " Comments");

        Toast.makeText(this, "Comment Added...", Toast.LENGTH_SHORT).show();
    }



    public void loadUserInfo() {
        // Assume userBitmap is the bitmap you are working with
        if (currentUser.getProfilePicture() != null) {
            // Safely scale the bitmap
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(currentUser.getProfilePicture(), 50, 50, true);
            cAvatarTv.setImageBitmap(scaledBitmap);
        } else {
            // Fallback to a default image if userBitmap is null
            cAvatarTv.setImageResource(R.drawable.person_icon);
        }
    }

    private void loadPostInfo() {
        try{
            dbHelper = new DatabaseHelper(this);
            crudReview = new CRUD_Review(dbHelper);
            crudBusiness = new CRUD_Business(dbHelper);
            crudImage = new CRUD_Image(dbHelper);
            crudUser = new CRUD_User(dbHelper);
            currentReview = crudReview.getReview(postId);
            currentUser = crudUser.getUser(myId);

            int postUserId = currentReview.getReviewerId();
            postUser = crudUser.getUser(postUserId);

        }catch (Exception e) {
            e.printStackTrace();
        }

        // Load Images for the review
        ArrayList<Bitmap> reviewImages = crudImage.getReviewImages(currentReview.getReviewId());
        if (!reviewImages.isEmpty()) {
            recyclerAdapter = new showImages(this, reviewImages);
            imageView.setLayoutManager(new GridLayoutManager(this, 4));  // Ensure correct view
            imageView.setAdapter(recyclerAdapter);  // Set the adapter for the imageView RecyclerView
        } else {
            imageView.setVisibility(View.GONE);  // Hide if no images
        }



//        setting profile picture
        if(postUser.getProfilePicture()  != null){
            uPictureIv.setImageBitmap(Bitmap.createScaledBitmap(postUser.getProfilePicture(), 50, 50, false));
        }

        likeBtn.setText(String.valueOf(currentReview.getLikes().size()) + " Likes");
        if(currentReview.getLikes().contains(myId)){
            likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.thumb_up, 0, 0, 0); // Default like icon
        }
        disLikeBtn.setText(String.valueOf(currentReview.getDislike().size()) + " Dislikes");
        if(currentReview.getDislike().contains(myId)){
            disLikeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.thumb_down, 0, 0, 0); // Default like icon
        }

//        setting name
        uNameTv.setText(postUser.getUsername());

        Position postUserPosition = crudUser.getUserPoistion(postUser.getId());
//        set position
        if(postUserPosition.getBusinessID() == busId){
            pPosition.setText(postUserPosition.getPositionName());
        }else{
            pPosition.setVisibility(View.GONE);
        }

        Log.e("Rating", String.valueOf(currentReview.getReviewRating()));
//        set reviewStars
        ratingReview.setRating(currentReview.getReviewRating());

//        set title
        pTitleTv.setText(currentReview.getReviewTitle());

//        set content
        pDescriptionTv.setText(currentReview.getReviewText());

        String[] tag = currentReview.getTags();
//        set tags
        if(tag != null){
            String itemTags = "";
            for (int i = 0; i < tag.length; i++) {
                itemTags += "#";
                itemTags += tag[i];
                itemTags += " ";
            }
            pTags.setText(itemTags);
        }else{
            pTags.setVisibility(View.GONE);
        }

//        set time
        //        convert timestamp to dd/mm/yyyy hh:mm am/pm
        try {
            SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date postDate = dateTime.parse(currentReview.getTimestamp());
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

            pTimeTv.setText(timeAgo);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        pCommentTv.setText(String.valueOf(crudReview.getResponses(postId).size()) + " Comments");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}