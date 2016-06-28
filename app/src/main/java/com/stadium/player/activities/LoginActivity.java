package com.stadium.player.activities;

import android.os.Bundle;
import android.view.View;

import com.stadium.player.R;
import com.stadium.player.dialogs.ForgetPasswordDialog;

public class LoginActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void onClick(View v) {
        ForgetPasswordDialog dialog = new ForgetPasswordDialog(this);
        dialog.show();
    }
}