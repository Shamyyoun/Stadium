package com.stadium.app.activities;

import android.os.Bundle;
import android.view.View;

import com.stadium.app.R;
import com.stadium.app.dialogs.ForgetPasswordDialog;

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