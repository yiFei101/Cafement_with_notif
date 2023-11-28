package com.example.loginauthentication;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.loginauthentication.MerchantPanel.MerchantHomeFragment;
import com.example.loginauthentication.MerchantPanel.MerchantOrderFragment;
import com.example.loginauthentication.MerchantPanel.MerchantPendingOrdersFragment;
import com.example.loginauthentication.MerchantPanel.MerchantProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class ChefFoodPanel_BottomNavigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_navigation_bar);
        BottomNavigationView navigationView = findViewById(R.id.merchant_bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        UpdateToken();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        String name = getIntent().getStringExtra("PAGE");
        if (name != null) {
            if (name.equalsIgnoreCase("Orderpage")) {
                loadcheffragment(new MerchantHomeFragment());
            } else if (name.equalsIgnoreCase("Confirmpage")) {
                loadcheffragment(new MerchantOrderFragment());
            } else if (name.equalsIgnoreCase("AcceptOrderpage")) {
                loadcheffragment(new MerchantHomeFragment());
            } else if (name.equalsIgnoreCase("Deliveredpage")) {
                loadcheffragment(new MerchantHomeFragment());
            }
        } else {
            loadcheffragment(new MerchantHomeFragment());
        }
    }

    private void UpdateToken() {
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isComplete()){
                    String token = task.getResult();
                    FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);



                }
            }
        });
//        String refreshToken = FirebaseInstanceId.getInstance().getToken();
//        Token token = new Token(refreshToken);
//        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
    }

    private boolean loadcheffragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.Merchanthome:
                fragment = new MerchantHomeFragment();
                break;

            case R.id.MerchantPendingOrders:
                fragment = new MerchantPendingOrdersFragment();
                break;

            case R.id.Orders:
                fragment = new MerchantOrderFragment();
                break;
            case R.id.MerchantProfile:
                fragment = new MerchantProfileFragment();
                break;
        }
        return loadcheffragment(fragment);
    }
}