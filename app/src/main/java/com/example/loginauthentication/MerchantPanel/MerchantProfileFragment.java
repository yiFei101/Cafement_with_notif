package com.example.loginauthentication.MerchantPanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.loginauthentication.R;

public class MerchantProfileFragment extends Fragment {

    Button postDish;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_merchant_profile, container, false);

        postDish = v.findViewById(R.id.post_dish); // Initialize the postDish button

        postDish.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Merchant_post_dish.class));
            }
        });

        return v;
    }
}
