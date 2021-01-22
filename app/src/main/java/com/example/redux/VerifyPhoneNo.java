package com.example.redux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneNo extends AppCompatActivity {

    String verificationCodeBySystem;
    Button verify_btn;
    PinView phoneNoEnteredByTheUser;
    ProgressBar progressBar;
    TextView PhoneText;
    String name,phoneNo,email,username,password,whatToDo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verify_phone_no);

        //Hooks
        verify_btn = findViewById(R.id.verify_btn);
        phoneNoEnteredByTheUser = findViewById(R.id.pinView);
        progressBar = findViewById(R.id.progress_bar);
        PhoneText = findViewById(R.id.phoneText);
        progressBar.setVisibility(View.GONE);

         phoneNo = getIntent().getStringExtra("phoneNo");
         whatToDo = getIntent().getStringExtra("whatToDo");
         name = getIntent().getStringExtra("name");
         username = getIntent().getStringExtra("username");
         email = getIntent().getStringExtra("email");
         password = getIntent().getStringExtra("password");


         PhoneText.setText("Enter One Time Password Sent on +60"+phoneNo);

        sendVerificationCodeToUser(phoneNo);

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = phoneNoEnteredByTheUser.getText().toString();

                if(code.isEmpty() || code.length()<6){
                    phoneNoEnteredByTheUser.setError("Wrong OTP");
                    phoneNoEnteredByTheUser.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        });
    }
     private void sendVerificationCodeToUser(String phoneNo){
         PhoneAuthProvider.getInstance().verifyPhoneNumber(
                 "+60"+ phoneNo,        // Phone number to verify
                 60,                 // Timeout duration
                 TimeUnit.SECONDS,   // Unit of timeout
                 TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                 mCallbacks);        // OnVerificationStateChangedCallbacks

     }

     private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


         @Override
         public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
             super.onCodeSent(s, forceResendingToken);

             verificationCodeBySystem = s;
         }

         @Override
         public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

             String code = phoneAuthCredential.getSmsCode();
             if(code!=null){
                 phoneNoEnteredByTheUser.setText(code);
                 progressBar.setVisibility(View.VISIBLE);
                 verifyCode(code);
             }

         }

         @Override
         public void onVerificationFailed(FirebaseException e) {
             Toast.makeText(VerifyPhoneNo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
         }
     };

    private void verifyCode(String codeByUser){

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem,codeByUser);
        signInTheUserByCredintials(credential);
    }

    private void signInTheUserByCredintials(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyPhoneNo.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            if(whatToDo.equals("updateData")){
                                updateOldUserData();
                            }
                            if(whatToDo.equals("registerData")){
                                storeNewUserData();
                                Intent intent = new Intent(getApplicationContext(),Login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }
                        else {
                            Toast.makeText(VerifyPhoneNo.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateOldUserData() {

        Intent intent = new Intent(getApplicationContext(),SetNewPassword.class);
        intent.putExtra("phoneNo",phoneNo);
        finish();
        startActivity(intent);
    }

    private void storeNewUserData(){
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("users");

        UserHelperClass helperClass = new UserHelperClass(name, username, email, phoneNo, password);
        reference.child(phoneNo).setValue(helperClass);
    }


}
