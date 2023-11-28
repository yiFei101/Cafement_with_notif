package com.example.loginauthentication;

import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;


import android.os.Bundle;

public class loadinganimation extends AppCompatActivity {


    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadinganimation);

        lottieAnimationView = findViewById(R.id.lottieAnimationView);

        // Start the animation
        lottieAnimationView.playAnimation();

        // Control the animation, e.g., pause it
        // lottieAnimationView.pauseAnimation();
    }
}