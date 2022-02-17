package com.example.firefly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.auth.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    private EditText getNameforsignup,getbirthDateForSignup,getmobileNumberForSignip,getpasswordConfirmationforsignup;
    private EditText getMailforsignup;
    private EditText getpasswordforsignup;
    private Button signUpconfirmButton;
    private ProgressBar signUpProgress;
    final Calendar myCalendar=Calendar.getInstance();
    //Firebase Connection
    private FirebaseAuth signupAuth;
    private DatabaseReference rootDatabaseRef;
    FirebaseFirestore userDataStore;
    String userId;

    //DatabaseReference databaseReference;
    int a=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getNameforsignup=findViewById(R.id.getName);
        getMailforsignup=findViewById(R.id.getMailId);
        getpasswordforsignup=findViewById(R.id.passwordField);
        signUpconfirmButton=findViewById(R.id.signUpConfirmationButton);
        signUpProgress=findViewById(R.id.signupProgressBar);
        signUpconfirmButton.setOnClickListener(this);
        getbirthDateForSignup=(EditText) findViewById(R.id.editTextBirthdate);
        getmobileNumberForSignip=findViewById(R.id.editTextMobileNumber);
        getpasswordConfirmationforsignup=findViewById(R.id.editTextTextConfirmPassword);

        //firebase realtime Data
        rootDatabaseRef=FirebaseDatabase.getInstance().getReference();

        //FirebaseApp firebaseApp = FirebaseApp.initializeApp(getApplicationContext());
        //birthDate.setOnFocusChangeListener((View.OnFocusChangeListener) this);
        //Calendar importing for birthdate
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                getBirthdateLabel();
            }
        };
        getbirthDateForSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SignUp.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Firebase Work
        signupAuth=FirebaseAuth.getInstance();
        userDataStore=FirebaseFirestore.getInstance();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signUpConfirmationButton:
                userRegister();
                break;
        }
    }


    //Register User Function
    private void userRegister() {
        String email = getMailforsignup.getText().toString().trim();
        String name = getNameforsignup.getText().toString().trim();
        String birthdate=getbirthDateForSignup.getText().toString().trim();
        String mobileNumber=getmobileNumberForSignip.getText().toString().trim();
        String password = getpasswordforsignup.getText().toString().trim();
        String confirmedPass=getpasswordConfirmationforsignup.getText().toString().trim();

        //Check Validity and Nullity of the Email
        if (email.isEmpty()) {
            getMailforsignup.setError("You must Enter an Email ID");
            getMailforsignup.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            getMailforsignup.setError("Enter a valid Email address!");
            getMailforsignup.requestFocus();
            return;
        }

        //Check Validity of the name
        if (name.isEmpty()) {
            getNameforsignup.setError("You must Enter your name!");
            getNameforsignup.requestFocus();
            return;
        }

        //check Validity of Number
        if (password.isEmpty()) {
            getpasswordforsignup.setError("You must Enter a password");
            getpasswordforsignup.requestFocus();
            return;
        }

        if (confirmedPass.isEmpty()) {
            getpasswordConfirmationforsignup.setError("You must Enter a password");
            getpasswordConfirmationforsignup.requestFocus();
            return;
        }
        if(!password.matches(confirmedPass)){
            getpasswordConfirmationforsignup.setError("Entered passwords doesn't match!");
            getpasswordConfirmationforsignup.requestFocus();
            return;
        }
        if(birthdate.isEmpty()){
            getbirthDateForSignup.setError("You must enter your birthdate");
            getbirthDateForSignup.requestFocus();
            return;
        }

        if(mobileNumber.isEmpty()){
            getmobileNumberForSignip.setError("You must enter your Mobile number");
            getmobileNumberForSignip.requestFocus();
            return;
        }
        if(mobileNumber.length()!=11){
            getmobileNumberForSignip.setError("Invalid Number");
            getmobileNumberForSignip.requestFocus();
            return;
        }

        signUpProgress.setVisibility(View.VISIBLE);

        //Signing up user in Firebase
        signupAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                signUpProgress.setVisibility(View.GONE);
                if (task.isSuccessful()) {

                    //Storing userData in cloud firestore
                    userId=signupAuth.getCurrentUser().getUid();
                    DocumentReference documentReference=userDataStore.collection("Users").document(userId);
                    Map<String,Object> user=new HashMap<>();
                    user.put("Name",name);
                    user.put("Email",email);
                    user.put("Phone number",mobileNumber);
                    user.put("Birth date",birthdate);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("Tag","onSuccess: User info is stored for "+userId);
                        }
                    });

                    //rootDatabaseRef.setValue("farhan245");
                    //Storing Data in Database
//                    databaseReference= FirebaseDatabase.getInstance().getReference();
//                    saveUserDataForSignup(name,email,birthdate,mobileNumber);

                    //Sending Verification Mail
                    sendVerificationMail();

                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(getApplicationContext(), "Registration is successful", Toast.LENGTH_SHORT).show();
                } else {
                    //If user already exist:
                    if(task.getException()instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"An user is already registered with this credential!",Toast.LENGTH_SHORT).show();
                    }



                    // If sign in fails, display a message to the user.


                    Toast.makeText(getApplicationContext(), "Registration is unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void sendVerificationMail(){
        //Send verification Email to the user
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

    private void getBirthdateLabel(){
        String myFormat="dd/MM/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        getbirthDateForSignup.setText(dateFormat.format(myCalendar.getTime()));
    }
//    private void saveUserDataForSignup(String name,String email,String birthdate,String mobileNumber){
//
//        String key=databaseReference.push().getKey();
//        //User user=new User(name,birthdate);
//        //databaseReference.child(key).setValue(user);
//        Toast.makeText(getApplicationContext(), "Added to database", Toast.LENGTH_SHORT).show();
//    }
}