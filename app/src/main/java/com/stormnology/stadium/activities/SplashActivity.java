package com.stormnology.stadium.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.stormnology.stadium.R;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.controllers.ParseController;
import com.stormnology.stadium.models.entities.User;

public class SplashActivity extends ParentActivity {
    private static final int SPLASH_DURATION = 2 * 1000;
    private ActiveUserController userController;
    private ParseController parseController;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // obtain main objects
        userController = new ActiveUserController(this);
        parseController = new ParseController(this);

        // check saved user
        if (userController.hasLoggedInUser()) {
            // install parse if required
            User user = userController.getUser();
            parseController.installIfRequired(user.getId(), user.getChannels());

            // check his role in the system, if admin or not to goto suitable activity
            Intent intent;
            if (userController.isAdmin()) {
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