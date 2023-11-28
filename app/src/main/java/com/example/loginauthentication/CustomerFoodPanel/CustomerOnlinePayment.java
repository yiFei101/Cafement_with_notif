package com.example.loginauthentication.CustomerFoodPanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginauthentication.R;

public class CustomerOnlinePayment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_online_payment);

        // Your code for setting up the activity goes here
    }

    public void onGcashClick(View view) {
        // Handle the click event for the GCash ImageView here.
        // You can open another activity using an Intent, for example:
        Intent intent = new Intent(this, Gcash.class);
        startActivity(intent);
    }

    public void onMayaClick(View view) {
        // Handle the click event for the Paymaya ImageView here.
        // You can open another activity using an Intent, for example:
        Intent intent = new Intent(this, Paymaya.class);
        startActivity(intent);
    }
}
