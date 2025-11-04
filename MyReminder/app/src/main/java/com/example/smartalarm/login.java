package com.example.smartalarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smartalarm.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    EditText emailEditText,passwordEditText;
            Button loginBtn;
        private FirebaseAuth auth;
            ProgressBar progressBar;
            TextView createAccountBtnTextView;
            TextView forgotPassword;


@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        forgotPassword= findViewById(R.id.forgot_password);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


        emailEditText = findViewById(R.id.email_login);
        passwordEditText = findViewById(R.id.password_login);
        loginBtn = findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.progressBar);
        createAccountBtnTextView = findViewById(R.id.createBtnTextView);

        loginBtn.setOnClickListener((v)-> loginUser() );
        createAccountBtnTextView.setOnClickListener((v)->startActivity(new Intent(login.this,register.class)) );
        forgotPassword.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View view) {
                                                          AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
                                                          View dialogView = getLayoutInflater().inflate(R.layout.dialog_forget, null);
                                                          EditText emailBox = dialogView.findViewById(R.id.emailBox);
                                                          builder.setView(dialogView);
                                                          AlertDialog dialog = builder.create();
                                                          dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                                                                  @Override
                                                                  public void onClick(View view) {
                                                                          String userEmail = emailBox.getText().toString();
                                                                          if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                                                                                  Toast.makeText(login.this, "Enter your registered email id", Toast.LENGTH_SHORT).show();
                                                                                  return;
                                                                          }
                                                                          firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                  @Override
                                                                                  public void onComplete(@NonNull Task<Void> task) {
                                                                                          if (task.isSuccessful()) {
                                                                                                  Toast.makeText(login.this, "Check your email", Toast.LENGTH_SHORT).show();
                                                                                                  dialog.dismiss();
                                                                                          } else {
                                                                                                  Toast.makeText(login.this, "Unable to send, failed", Toast.LENGTH_SHORT).show();
                                                                                          }
                                                                                  }
                                                                          });
                                                                  }
                                                          });
                                                          dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                                                                  @Override
                                                                  public void onClick(View view) {
                                                                          dialog.dismiss();
                                                                  }
                                                          });
                                                          if (dialog.getWindow() != null) {
                                                                  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                                                          }
                                                          dialog.show();
                                                  }
                                          }
        );
}








        void loginUser(){
        String email  = emailEditText.getText().toString();
        String password  = passwordEditText.getText().toString();


        boolean isValidated = validateData(email,password);
        if(!isValidated){
        return;
        }

        loginAccountInFirebase(email,password);

        }

        void loginAccountInFirebase(String email,String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
@Override
public void onComplete(@NonNull Task<AuthResult> task) {
        changeInProgress(false);
        if(task.isSuccessful()){
        //login is success
        if(firebaseAuth.getCurrentUser().isEmailVerified()){
        //go to mainactivity
        startActivity(new Intent(login.this,action_bar.class));
        finish();
        }else{
        Utility.showToast(login.this,"Email not verified, Please verify your email.");
        }

        }else{
        //login failed
        Utility.showToast(login.this,task.getException().getLocalizedMessage());
        }
        }
        });
        }

        void changeInProgress(boolean inProgress){
        if(inProgress){
        progressBar.setVisibility(View.VISIBLE);
        loginBtn.setVisibility(View.GONE);
        }else{
        progressBar.setVisibility(View.GONE);
        loginBtn.setVisibility(View.VISIBLE);
        }
        }

        boolean validateData(String email,String password){

        //validate the data that are input by user.

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        emailEditText.setError("Email is invalid");
        return false;
        }
        if(password.length()<6){
        passwordEditText.setError("Password length is invalid");
        return false;
        }
        return true;
        }



        }