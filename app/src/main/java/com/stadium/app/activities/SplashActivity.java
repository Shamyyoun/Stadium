package com.stadium.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.stadium.app.R;
import com.stadium.app.controllers.UserController;

public class SplashActivity extends ParentActivity {
    private static final int SPLASH_DURATION = 0 * 1000; // TODO set splash to 2 seconds
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // check saved user
        UserController userController = new UserController(this);
        if (userController.hasLoggedInUser()) {
            // goto main activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            // start splash
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    // goto suitable activity
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            };
            handler.postDelayed(runnable, SPLASH_DURATION);
        }
    }
}