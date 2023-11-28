package com.example.loginauthentication.MerchantPanel;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.loginauthentication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MerchantNavigationBar extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_navigation_bar);
        BottomNavigationView navigationView = findViewById(R.id.merchant_bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.Merchanthome:
                fragment=new MerchantHomeFragment();
                break;
            case R.id.MerchantPendingOrders:
                fragment=new MerchantPendingOrdersFragment();
                break;

            case R.id.Orders:
                fragment=new MerchantOrderFragment();
                break;
            case R.id.MerchantProfile:
                fragment=new MerchantProfileFragment();
                break;
        }
        return loadcheffragment(fragment);
    }

    private boolean loadcheffragment(Fragment fragment) {

        if (fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,fragment).commit();
            return true;
        }
        return false;
    }
}