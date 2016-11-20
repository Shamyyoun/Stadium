package com.stadium.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.stadium.app.R;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.models.entities.User;

public class SplashActivity extends ParentActivity {
    private static final int SPLASH_DURATION = 2 * 1000;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // check saved user
        ActiveUserController userController = new ActiveUserController(this);
        if (userController.hasLoggedInUser()) {
            // check his role in the system, if admin or not to goto suitable activity
            User user = userController.getUser();
            Intent intent;
            if (user.getAdminStadium() != null) {
                intent = new Intent(this, AdminMainActivity.class);
            } else {
                intent = new Intent(this, PlayerMainActivity.class);
            }

            // goto suitable activity
            startActivity(intent);
        } else {
            // start splash
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    // goto splash activity
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            };
            handler.postDelayed(runnable, SPLASH_DURATION);
        }
    }
}