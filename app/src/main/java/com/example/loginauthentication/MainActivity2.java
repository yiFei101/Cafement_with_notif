package com.example.loginauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity2 extends AppCompatActivity {

    TextInputLayout email, pass;
    Button Signin;
    FirebaseAuth Fauth;
    String Email1, Pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(MainActivity2.this,
                    android.Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity2.this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        email = findViewById(R.id.Email1);
        pass = findViewById(R.id.Pwd);
        Signin = findViewById(R.id.StudentSignin);
        Fauth = FirebaseAuth.getInstance();

        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email1 = email.getEditText().getText().toString().trim();
                Pwd = pass.getEditText().getText().toString().trim();

                if (isValid()) {
                    final ProgressDialog mDialog = new ProgressDialog(MainActivity2.this);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setCancelable(false);
                    mDialog.setMessage("Sign In please wait......");
                    mDialog.show();

                    // Use signInWithEmailAndPassword for email and password login
                    Fauth.signInWithEmailAndPassword(Email1, Pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (Fauth.getCurrentUser().isEmailVerified()) {
                                    mDialog.dismiss();
                                    Toast.makeText(MainActivity2.this, "Email verified. Login Successful", Toast.LENGTH_SHORT).show();

                                    Intent studentPanelIntent = new Intent(MainActivity2.this, CustomerFoodPanel_BottomNavigation.class);
                                    startActivity(studentPanelIntent);
                                } else {
                                    mDialog.dismiss();
                                    ReusableCodeForAll.ShowAlert(MainActivity2.this, "Email not verified", "Please verify your email before logging in.");
                                }
                            } else {
                                mDialog.dismiss();
                                // Show an alert for login failure
                                ReusableCodeForAll.ShowAlert(MainActivity2.this, "Login failed", "Invalid credentials. Please check your email and password.");
                            }
                        }
                    });
                }
            }
        });
    }

    public boolean isValid() {
        email.setErrorEnabled(false);
        email.setError("");
        pass.setErrorEnabled(false);
        pass.setError("");

        boolean isvalid = false, isvalidpassword = false;

        if (TextUtils.isEmpty(Email1)) {
            email.setErrorEnabled(true);
            email.setError("Email is required");
        } else {
            // Email format validation
            if (isValidEmail(Email1)) {
                isvalidpassword = true;
            } else {
                email.setErrorEnabled(true);
                email.setError("Invalid Email");
            }
        }

        if (TextUtils.isEmpty(Pwd)) {
            pass.setErrorEnabled(true);
            pass.setError("Password is Required");
        } else {
            isvalidpassword = true;
        }
        isvalid = isvalidpassword; // Only check for password validity
        return isvalid;
    }

    // Function to validate email format
    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}