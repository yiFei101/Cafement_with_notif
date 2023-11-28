package com.example.loginauthentication;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.example.loginauthentication.CustomerFoodPanel.SettingsFragment;
import com.example.loginauthentication.MerchantPanel.MerchantHomeFragment;
import com.example.loginauthentication.MerchantPanel.MerchantOrderFragment;
import com.example.loginauthentication.MerchantPanel.MerchantPendingOrdersFragment;
import com.example.loginauthentication.MerchantPanel.MerchantProfileFragment;
import com.example.loginauthentication.MerchantPanel.MerchantSettingsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class ChefFoodPanel_BottomNavigation1 extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_food_panel__bottom_navigation);
        BottomNavigationView navigationView = findViewById(R.id.chef_bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        UpdateToken();
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
            case R.id.chefHome:
                fragment = new MerchantHomeFragment();
                break;

            case R.id.PendingOrders:
                fragment = new MerchantPendingOrdersFragment();
                break;

            case R.id.Orders:
                fragment = new MerchantOrderFragment();
                break;
            case R.id.chefProfile:
                fragment = new MerchantProfileFragment();
                break;

            case R.id.Settings:
                fragment = new MerchantSettingsFragment();
                break;

        }
        return loadcheffragment(fragment);
    }
}
