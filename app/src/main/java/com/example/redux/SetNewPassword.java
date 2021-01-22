package com.example.redux;

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
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetNewPassword extends AppCompatActivity {

    TextInputLayout password,comfirmPassword ;
    Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_set_new_password);
        //hooks
        password = findViewById(R.id.newpass);
        comfirmPassword = findViewById(R.id.newpassCom);
        updateButton = findViewById(R.id.update_btn);

    }
    public void setNewPasswordBtn(View view){
        //Check Internet Connection
        CheckInternet checkInternet = new CheckInternet();
        if(!checkInternet.isConnected(this)){
            showCustomerDialog();
            return;
        }

        String _newPassword = password.getEditText().getText().toString().trim();
        String _phoneNumber = getIntent().getStringExtra("phoneNo");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(_phoneNumber).child("password").setValue(_newPassword);

        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();

    }

    private void showCustomerDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SetNewPassword.this);
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
                        startActivity(new Intent(getApplicationContext(),SetNewPassword.class));
                        finish();
                    }
                });
    }

}