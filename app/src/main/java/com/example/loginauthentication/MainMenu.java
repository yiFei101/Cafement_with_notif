package com.example.loginauthentication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.example.loginauthentication.MerchantPanel.MerchantNavigationBar;

public class MainMenu extends AppCompatActivity {
    private Button Signwithstudentid, register;
    ImageView bgimage;
    private int clickCount = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openregister();
            }
        });

        Signwithstudentid = findViewById(R.id.Signwithstudentid);
        Signwithstudentid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment the click count
                clickCount++;

                // Check if the button was clicked 3 times
                if (clickCount == 3) {
                    redirectToAdminPanel();
                } else if (clickCount == 1) {
                    openMainActivity2();
                }

                // Reset the click count after a delay
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        clickCount = 0;
                    }
                }, 2000); // Reset after 2 seconds (adjust as needed)
            }
        });

        final Animation zoomin = AnimationUtils.loadAnimation(this, R.anim.zoomin);
        final Animation zoomout = AnimationUtils.loadAnimation(this, R.anim.zoomout);

        bgimage = findViewById(R.id.back2);
        bgimage.setAnimation(zoomin);
        bgimage.setAnimation(zoomout);

        zoomout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bgimage.startAnimation(zoomin);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        zoomin.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bgimage.startAnimation(zoomout);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public void openMainActivity2() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void redirectToAdminPanel() {
        Intent intent = new Intent(this, AdminPanel.class);
        startActivity(intent);
    }



    public void openregister() {
        Intent intent = new Intent(this, register.class);
        startActivity(intent);
    }
}