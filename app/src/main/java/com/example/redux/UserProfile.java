package com.example.redux;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class UserProfile extends AppCompatActivity {

    TextInputLayout fullName,email,phoneNo,password;
    TextView fullNameLabel,usernameLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_profile);

        Button button = (Button) findViewById(R.id.save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              openactivity_dashboard();
            }
        });

        //Hooks
        fullName = findViewById(R.id.full_name_profile);
        email = findViewById(R.id.email_profile);
        phoneNo= findViewById(R.id.phone_no_profile);
        password = findViewById(R.id.password_profile);
        fullNameLabel = findViewById(R.id.full_name);
        usernameLabel = findViewById(R.id.username_field);

        //ShowAllData
        showAllUserData();

    }

    private void showAllUserData(){
        Intent intent = getIntent();
        String user_username = intent.getStringExtra("username");
        String user_name = intent.getStringExtra("name");
        String user_email = intent.getStringExtra("email");
        String user_phoneNo = intent.getStringExtra("phoneNo");
        String user_password = intent.getStringExtra("password");


        fullNameLabel.setText(user_username);
        usernameLabel.setText(user_name);
        fullName.getEditText().setText(user_name);
        email.getEditText().setText(user_email);
        phoneNo.getEditText().setText(user_phoneNo);
        password.getEditText().setText(user_password);
    }

    public void openactivity_dashboard() {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }

}
