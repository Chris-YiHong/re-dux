package com.example.redux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Queue;

public class ForgetPassword extends AppCompatActivity {
    ImageView backbutton;
    TextInputLayout phonenumberLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_password);

        backbutton = findViewById(R.id.backbutton);
        phonenumberLayout = findViewById(R.id.phonenumberlayout);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void verifyPhoneNumber(View view) {
        if (!isConnected(this)) {
            showCustomerDialog();
        }
        if(!validatePhoneNo()){
            return;
        }
        String _phoneNumber = phonenumberLayout.getEditText().getText().toString().trim().substring(1);
        final String _completePhoneNumber = _phoneNumber;

        // Check whether user exist or not in the database
        Query checkUser = FirebaseDatabase.getInstance().getReference("users").orderByChild("phoneNo").equalTo(_completePhoneNumber);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    phonenumberLayout.setError(null);
                    phonenumberLayout.setEnabled(false);

                    Intent intent = new Intent(getApplicationContext(), VerifyPhoneNo.class);
                    intent.putExtra("phoneNo", _completePhoneNumber);
                    intent.putExtra("whatToDo","updateData");
                    startActivity(intent);
                    finish();
                } else {
                    phonenumberLayout.setError("No such user exist");
                    phonenumberLayout.requestFocus();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private boolean isConnected(ForgetPassword forgetPassword) {

        ConnectivityManager connectivityManager =(ConnectivityManager) forgetPassword.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiConn != null && wifiConn.isConnected() || (mobileConn != null&& mobileConn.isConnected()))){
            return true;
        }
        else {
            return false;
        }

    }
    private void showCustomerDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPassword.this);
        builder.setMessage("Please connect to the internet to proceed further").setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(),Login.class));
                        finish();
                    }
                });
    }

    private Boolean validatePhoneNo() {
        String val = phonenumberLayout.getEditText().getText().toString();

        if (val.isEmpty()) {
            phonenumberLayout.setError("Field cannot be empty");
            return false;
        } else {
            phonenumberLayout.setError(null);
            phonenumberLayout.setErrorEnabled(false);
            return true;
        }
    }

}