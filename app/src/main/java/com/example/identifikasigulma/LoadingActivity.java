package com.example.identifikasigulma;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    private static final long LOADING_DELAY = 2000; // Delay dalam milidetik

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        final ImageView logo = findViewById(R.id.logo);

        // Animasi pudar masuk
        Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        logo.startAnimation(fadeIn);

        // Simulasi proses loading dengan menggunakan handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Animasi pudar keluar
                Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // Setelah proses loading selesai, beralih ke menu utama
                        Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Menutup aktivitas loading
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
                logo.startAnimation(fadeOut);
                logo.setVisibility(View.INVISIBLE);
            }
        }, LOADING_DELAY);
    }
}
