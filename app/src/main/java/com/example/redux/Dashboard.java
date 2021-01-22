package com.example.redux;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class Dashboard extends AppCompatActivity {
    String user_phoneNo,user_email,user_username,user_name,user_password;
    ViewFlipper viewFlipper;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);

        Intent intent = getIntent();
        user_phoneNo = intent.getStringExtra("phoneNo");
        user_email = intent.getStringExtra("email");
        user_password = intent.getStringExtra("password");
        user_username = intent.getStringExtra("username");
        user_name = intent.getStringExtra("name");
        

    Button button = (Button) findViewById(R.id.button01);
    Button btn_userprofile = findViewById(R.id.profilepic);
    Button stopButton = findViewById(R.id.button02);

        button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openPlayVideo();
        }
    });
        btn_userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, UserProfile.class);
                intent.putExtra("phoneNo", user_phoneNo);
                intent.putExtra("name", user_name);
                intent.putExtra("username", user_username);
                intent.putExtra("email", user_email);
                intent.putExtra("password", user_password);
                startActivity(intent);
            }
        });

        Button btn_logout = (Button) findViewById(R.id.logoutbutton);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Logout.class);
                startActivity(intent);
            }
        });

        int images[] = {R.drawable.slider1,R.drawable.slider4,R.drawable.slider5};

        viewFlipper = findViewById(R.id.button03);

        for (int image: images){
            flipperImages(image);
        }
}

    public void flipperImages(int image){
        Context context;
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);

        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
    }

    public void openPlayVideo() {
        Intent intent = new Intent(this, PlayVideo.class);
        startActivity(intent);
    }


}
