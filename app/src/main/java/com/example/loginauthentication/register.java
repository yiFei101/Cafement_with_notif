package com.example.loginauthentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class register extends AppCompatActivity {
    private Button button;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        imageView = findViewById(R.id.backButton);
        button = (Button) findViewById(R.id.merchantregister1);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back when the back button is clicked
                onBackPressed();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openmerchantregister();
            }
        });
        button = (Button) findViewById(R.id.studentregister);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStudentRegister();
            }
        });

    }


    public void openmerchantregister()
    {
        Intent intent = new Intent(this, merchantregister.class);
        startActivity(intent);
    }

    public void  openStudentRegister()
    {
        Intent intent = new Intent(this, StudentRegister.class );
        startActivity(intent);
    }
}