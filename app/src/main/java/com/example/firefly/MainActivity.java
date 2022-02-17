package com.example.firefly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton;
    private TextView signUpButton,forgotPassword,loginStat,resendEmailVer;
    private EditText loginEmail,loginPassword;
    private ProgressBar loginProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.orange)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_title_layout);
        signUpButton=findViewById(R.id.signupButton);
        loginButton=findViewById(R.id.loginButton);
        signUpButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        loginEmail=findViewById(R.id.getEmailForLogin);
        loginPassword=findViewById(R.id.getPassForLogin);
        loginProgressBar=findViewById(R.id.loginprogressBar);
        forgotPassword=findViewById(R.id.forgotPassword);
        loginStat=findViewById(R.id.loginStatus);
        resendEmailVer=findViewById(R.id.resendVerification);
        resendEmailVer.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signupButton:
                Intent intent=new Intent(getApplicationContext(),SignUp.class);
                startActivity(intent);
                break;
            case R.id.loginButton:
                //Calling the function for user login
                userLogin();
                break;

            case R.id.resendVerification:
                //need to pass the verification method in signUp activity
                sendVerificationMail();
                break;
        }

    }
    private void userLogin(){
        String mail=loginEmail.getText().toString().trim();
        String pass=loginPassword.getText().toString().trim();

        if(mail.isEmpty()){
            loginEmail.setError("You must enter the Email ID!");
            loginEmail.requestFocus();
            return;
        }
        if(pass.isEmpty()){
            loginPassword.setError("You must Enter the password");
            loginPassword.requestFocus();
            return;
        }

        FirebaseAuth loginAuth = FirebaseAuth.getInstance();
        Task<AuthResult> task = loginAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                FirebaseUser userForLogin = loginAuth.getCurrentUser();
                assert userForLogin != null;
                if (task.isSuccessful()) {
                    if (userForLogin.isEmailVerified()) {
                        resendEmailVer.setVisibility(View.GONE);
                        loginProgressBar.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent goToHome = new Intent(getApplicationContext(), Home.class);
                        startActivity(goToHome);
                        loginProgressBar.setVisibility(View.GONE);

                    }else if (!userForLogin.isEmailVerified()) {
                        Log.d(userForLogin.getEmail().trim(), "This is the mail");
                        loginEmail.setError("The Email is not verified!");
                        loginEmail.requestFocus();
                        resendEmailVer.setVisibility(View.VISIBLE);
                        forgotPassword.setVisibility(View.GONE);
                        loginStat.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                    forgotPassword.setVisibility(View.VISIBLE);
                    loginStat.setText("Wrong Credentials!");
                    loginStat.setVisibility(View.VISIBLE);
                    loginProgressBar.setVisibility(View.GONE);
                }
            }

        });
    }

    protected void sendVerificationMail(){
        //Send verification Email to the user
        FirebaseAuth signupAuth=FirebaseAuth.getInstance();
        FirebaseUser userForSignup=signupAuth.getCurrentUser();
        //assert user != null;
        userForSignup.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "A Verification Email has been sent to your Email Account!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(e.getMessage(),"onFailure: Email not sent");
            }
        });
    }
}