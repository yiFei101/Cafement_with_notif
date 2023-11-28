package com.example.loginauthentication;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.example.loginauthentication.MerchantPanel.MerchantNavigationBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    TextInputLayout emailLayout, passwordLayout;
    Button signInButton;
    ImageView imageView;
    FirebaseAuth firebaseAuth;
    String emailInput, passwordInput;
    RadioGroup roleRadioGroup;
    String selectedRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLayout = findViewById(R.id.Emailformerchant);
        passwordLayout = findViewById(R.id.Pwdformerchant);
        signInButton = findViewById(R.id.loginformerchant);
        firebaseAuth = FirebaseAuth.getInstance();
        imageView = findViewById(R.id.backButton);
        roleRadioGroup = findViewById(R.id.roleRadioGroup);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back when the back button is clicked
                onBackPressed();
            }
        });


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailInput = emailLayout.getEditText().getText().toString().trim();
                passwordInput = passwordLayout.getEditText().getText().toString().trim();

                if (isValid()) {
                    int selectedId = roleRadioGroup.getCheckedRadioButtonId();
                    if (selectedId == R.id.merchantRadioButton) {
                        selectedRole = "Merchant";
                    } else if (selectedId == R.id.studentRadioButton) {
                        selectedRole = "Student";
                    } else {
                        Toast.makeText(Login.this, "Please select a role", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    firebaseAuth.signInWithEmailAndPassword(emailInput, passwordInput)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                            checkUserRoleAndRedirect();
                                        } else {
                                            Toast.makeText(Login.this, "Email not verified. Please verify your email before logging in.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(Login.this, "Login failed. Invalid credentials.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void checkUserRoleAndRedirect() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User")
                .child(firebaseAuth.getCurrentUser().getUid());
        userRef.child("Role").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userRole = dataSnapshot.getValue(String.class);

                if (selectedRole.equals(userRole)) {
                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    if ("Merchant".equals(selectedRole)) {
                        startActivity(new Intent(Login.this, ChefFoodPanel_BottomNavigation1.class));
                    } else if ("Student".equals(selectedRole)) {
                        startActivity(new Intent(Login.this, CustomerFoodPanel_BottomNavigation.class));
                    }
                } else {
                    Toast.makeText(Login.this, "Access Denied. You do not have permission to access this role.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Login.this, "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValid() {
        emailLayout.setError(null);
        passwordLayout.setError(null);

        if (TextUtils.isEmpty(emailInput)) {
            emailLayout.setError("Email is required");
            return false;
        } else if (!isValidEmail(emailInput)) {
            emailLayout.setError("Invalid Email");
            return false;
        }

        if (TextUtils.isEmpty(passwordInput)) {
            passwordLayout.setError("Password is required");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
