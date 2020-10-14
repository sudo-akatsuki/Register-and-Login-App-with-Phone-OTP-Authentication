package com.example.bullsrent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity implements View.OnClickListener {

    ImageView LogoImage;
    TextView tvLogoName, SignInMessage, Username, Password;
    EditText etUsername, etPassword;
    Button ForgetPassword, RegisterLink, LoginButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        LogoImage = (ImageView) findViewById(R.id.LogoImage);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        ForgetPassword = (Button) findViewById(R.id.ForgetPassword);
        RegisterLink = (Button) findViewById(R.id.RegisterLink);
        LoginButton = (Button) findViewById(R.id.LoginButton);

        LoginButton.setOnClickListener(this);
        RegisterLink.setOnClickListener(this);

    }

    private Boolean validateUsername() {
        String val = etUsername.getText().toString();
        String noWhiteSpace = "(\\A\\w{4,20}\\z)";
        if(val.isEmpty()) {
            etUsername.setError("Field cannot be empty");
            return false;
        }
        else if (val.length()>=15) {
            etUsername.setError("Username is too long");
            return false;
        }
        else if (!val.matches(noWhiteSpace)) {
            etUsername.setError("White Spaces are not allowed");
            return false;
        }
        else {
            etUsername.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = etPassword.getText().toString();

        if(val.isEmpty()) {
            etPassword.setError("Field cannot be empty");
            return false;
        }
        else {
            etPassword.setError(null);
            return true;
        }
    }

//    public void loginUser(View view) {
//        if (!validateUsername() | !validatePassword()) {
//            return;
//        }
//        else {
//            isUser();
//        }
//    }

    private void isUser() {
        final String userEnteredUsername = etUsername.getText().toString().trim();
        final String userEnteredPassword = etPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    etUsername.setError(null);
                    String PasswordFromDB = dataSnapshot.child(userEnteredUsername).child("password").getValue(String.class);

                    if (PasswordFromDB.equals(userEnteredPassword)) {
                        etPassword.setError(null);
                        String nameFromDB = dataSnapshot.child(userEnteredUsername).child("name").getValue(String.class);
                        String emailFromDB = dataSnapshot.child(userEnteredUsername).child("email").getValue(String.class);
                        String phoneFromDB = dataSnapshot.child(userEnteredUsername).child("phone").getValue(String.class);
                        String usernameFromDB = dataSnapshot.child(userEnteredUsername).child("username").getValue(String.class);

                        Intent intent = new Intent(getApplicationContext(),Dashboard.class);

                        intent.putExtra("name",nameFromDB);
                        intent.putExtra("username",usernameFromDB);
                        intent.putExtra("phone",phoneFromDB);
                        intent.putExtra("email",emailFromDB);
                        intent.putExtra("password",PasswordFromDB);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        etPassword.setError("Wrong Password");
                        etPassword.requestFocus();
                    }
                }
                else {
                    etUsername.setError("No such user exist");
                    etUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.LoginButton:
//                startActivity(new Intent(this,Dashboard.class));
                if (!validateUsername() | !validatePassword()) {
                    return;
                }
                else {
                    isUser();
                }
                break;

            case R.id.RegisterLink:
                startActivity(new Intent(this,Register.class));
                break;
        }
    }
}