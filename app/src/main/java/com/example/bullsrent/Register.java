package com.example.bullsrent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener {

    EditText etName, etrUsername, etEmailId, etPhoneNo, etrPassword;
    Button LoginLink, RegisterButton;
//    FirebaseDatabase rootNode;
//    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        etName = findViewById(R.id.etName);
        etrUsername = findViewById(R.id.etrUsername);
        etrPassword = findViewById(R.id.etrPassword);
        etEmailId = findViewById(R.id.etEmailId);
        etPhoneNo = findViewById(R.id.etPhoneNo);

        LoginLink = (Button) findViewById(R.id.LoginLink);
        RegisterButton = (Button) findViewById(R.id.RegisterButton);
        LoginLink.setOnClickListener(this);
        RegisterButton.setOnClickListener(this);
    }

    private Boolean validateName() {
        String val = etName.getText().toString();

        if(val.isEmpty()) {
            etName.setError("Field cannot be empty");
            return false;
        }
        else {
            etName.setError(null);
            return true;
        }
    }

    private Boolean validateUsername() {
        String val = etrUsername.getText().toString();
        String noWhiteSpace = "(\\A\\w{4,20}\\z)";
        if(val.isEmpty()) {
            etrUsername.setError("Field cannot be empty");
            return false;
        }
        else if (val.length()>=15) {
            etrUsername.setError("Username is too long");
            return false;
        }
        else if (!val.matches(noWhiteSpace)) {
            etrUsername.setError("White Spaces are not allowed");
            return false;
        }
        else {
            etrUsername.setError(null);
            return true;
        }
    }

    private Boolean validatePhone() {
        String val = etPhoneNo.getText().toString();

        if(val.isEmpty()) {
            etPhoneNo.setError("Field cannot be empty");
            return false;
        }
        else {
            etPhoneNo.setError(null);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = etEmailId.getText().toString();
        String EmailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(val.isEmpty()) {
            etEmailId.setError("Field cannot be empty");
            return false;
        }
        else if (!val.matches(EmailPattern)) {
            etEmailId.setError("Invalid Email id");
            return false;
        }
        else {
            etEmailId.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = etrPassword.getText().toString();
        String passwordVal = "\\A\\w{6,20}\\z";

        if(val.isEmpty()) {
            etrPassword.setError("Field cannot be empty");
            return false;
        }
        else if(!val.matches(passwordVal)) {
            etrPassword.setError("Password should be of atleast 6 characters");
            return false;
        }
        else {
            etrPassword.setError(null);
            return true;
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.LoginLink:
                startActivity(new Intent(this,Login.class));
                break;

            case R.id.RegisterButton:
//                rootNode = FirebaseDatabase.getInstance();
//                reference = rootNode.getReference("Users");

                if (!validateName() | !validateEmail() | !validateUsername() | !validatePhone() | !validatePassword()) {
                    return;
                }

                String name = etName.getText().toString();
                String username = etrUsername.getText().toString();
                String phone = etPhoneNo.getText().toString();
                String email = etEmailId.getText().toString();
                String password = etrPassword.getText().toString();

                Intent intent = new Intent(getApplicationContext(),VerifyPhoneNo.class);

                intent.putExtra("PhoneNo",phone);
                intent.putExtra("Name",name);
                intent.putExtra("EmailId",email);
                intent.putExtra("Username",username);
                intent.putExtra("Password",password);
                startActivity(intent);
//                UserHelperClass helperClass = new UserHelperClass(name,username,email,phone,password);
//                reference.child(username).setValue(helperClass);

//                startActivity(new Intent(this,Login.class));
//                finish();
                break;
        }
    }
}