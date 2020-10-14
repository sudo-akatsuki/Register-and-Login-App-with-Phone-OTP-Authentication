package com.example.bullsrent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

//    EditText etName, etrUsername, etEmailId, etPhoneNo, etrPassword;
    String verificationCodeBySystem;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    EditText otpId;
    Button VerifyButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verify_phone_no);

//        etName = findViewById(R.id.etName);
//        etrUsername = findViewById(R.id.etrUsername);
//        etrPassword = findViewById(R.id.etrPassword);
//        etEmailId = findViewById(R.id.etEmailId);
//        etPhoneNo = findViewById(R.id.etPhoneNo);

        otpId=findViewById(R.id.otpId);
        VerifyButton = findViewById(R.id.VerifyButton);

        String PhoneNo = getIntent().getStringExtra("PhoneNo");
        sendVerificationCodeToUser(PhoneNo);

        VerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otpId.getText().toString();

                if (code.isEmpty() || code.length()<6) {
                    otpId.setError("Wrong OTP...");
                    otpId.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });
    }

    private void sendVerificationCodeToUser(String phoneNo) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+ phoneNo,        // Phone number to verify
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
            if (code!=null) {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneNo.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String CodeByUser) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, CodeByUser);
        signInTheUserByCredentials(credential);
    }

    private void signInTheUserByCredentials(PhoneAuthCredential credential) {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        fAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyPhoneNo.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            rootNode = FirebaseDatabase.getInstance();
                            reference = rootNode.getReference("Users");

                            String name = getIntent().getStringExtra("Name");
                            String username = getIntent().getStringExtra("Username");
                            String email = getIntent().getStringExtra("EmailId");
                            String phone = getIntent().getStringExtra("PhoneNo");
                            String password = getIntent().getStringExtra("Password");

                            UserHelperClass helperClass = new UserHelperClass(name,username,email,phone,password);
                            reference.child(username).setValue(helperClass);

                            Intent intent = new Intent(getApplicationContext(),Dashboard.class);

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                            Toast.makeText(VerifyPhoneNo.this,"Your account has been created successfully",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(VerifyPhoneNo.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}