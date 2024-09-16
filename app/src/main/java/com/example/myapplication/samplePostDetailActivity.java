package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class samplePostDetailActivity extends AppCompatActivity {

    ImageView uPictureIv, pImageIv;
    TextView nameTv, pTimeTiv, pTitle, pDescriptionTv, pLikeTv, pComment;
    ImageButton moreBtn;
    Button likeBtn, shareBtn;
    LinearLayout profileLayout;

    EditText commentEt;
    ImageButton sendBtn;
    ImageView cAvatarTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sample_post_detail);

//        actionbar and its properties
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Post Detail");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        uPictureIv = findViewById(R.id.cAvatarIv);
        nameTv = findViewById(R.id.uNameTv);
        pTimeTiv = findViewById(R.id.pTimeTv);
    };

    @Override
    public boolean onSupportNavigateUp(){
        getOnBackPressedDispatcher();
        return super.onSupportNavigateUp();
    }
}