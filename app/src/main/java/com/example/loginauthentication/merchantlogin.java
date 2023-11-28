package com.example.loginauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.loginauthentication.MerchantPanel.MerchantNavigationBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class merchantlogin extends AppCompatActivity {
    TextInputLayout email, pass;
    Button signIn;
    FirebaseAuth Fauth;
    String emailInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchantlogin);

        email = findViewById(R.id.Emailformerchant);
        pass = findViewById(R.id.Pwdformerchant);
        signIn = findViewById(R.id.loginformerchant);
        Fauth = FirebaseAuth.getInstance();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailInput = email.getEditText().getText().toString().trim();
                passwordInput = pass.getEditText().getText().toString().trim();

                if (isValid()) {
                    final ProgressDialog mDialog = new ProgressDialog(merchantlogin.this);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setCancelable(false);
                    mDialog.setMessage("Sign In please wait......");
                    mDialog.show();

                    Fauth.signInWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (Fauth.getCurrentUser().isEmailVerified()) {
                                    mDialog.dismiss();
                                    Toast.makeText(merchantlogin.this, "Email verified. Login Successful", Toast.LENGTH_SHORT).show();

                                    Intent merchantPanelIntent = new Intent(merchantlogin.this, MerchantNavigationBar.class);
                                    startActivity(merchantPanelIntent);
                                } else {
                                    mDialog.dismiss();
                                    ReusableCodeForAll.ShowAlert(merchantlogin.this, "Email not verified", "Please verify your email before logging in.");
                                }
                            } else {
                                mDialog.dismiss();
                                ReusableCodeForAll.ShowAlert(merchantlogin.this, "Login failed", "Invalid credentials. Please check your email and password.");
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

        boolean isValid = false, isValidPassword = false;

        if (TextUtils.isEmpty(emailInput)) {
            email.setErrorEnabled(true);
            email.setError("Email is required");
        } else {
            // Email format validation
            if (isValidEmail(emailInput)) {
                isValidPassword = true;
            } else {
                email.setErrorEnabled(true);
                email.setError("Invalid Email");
            }
        }

        if (TextUtils.isEmpty(passwordInput)) {
            pass.setErrorEnabled(true);
            pass.setError("Password is Required");
        } else {
            isValidPassword = true;
        }
        isValid = isValidPassword; // Only check for password validity
        return isValid;
    }

    // Function to validate email format
    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}