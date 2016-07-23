package com.stadium.player.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.stadium.player.R;

public class SplashActivity extends ParentActivity {
    private static final int SPLASH_DURATION = 0 * 1000; // TODO
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // start splash
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // goto suitable activity
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        };
        handler.postDelayed(runnable, SPLASH_DURATION);
    }
}