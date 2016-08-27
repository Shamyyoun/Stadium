package com.stadium.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.stadium.app.ApiRequests;
import com.stadium.app.R;
import com.stadium.app.controllers.UserController;
import com.stadium.app.models.bodies.LoginBody;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.Utils;

public class LoginActivity extends ParentActivity {
    private EditText etPhoneNo;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvForgetPassword;
    private TextView tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // init views
        etPhoneNo = (EditText) findViewById(R.id.et_phone_no);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tvForgetPassword = (TextView) findViewById(R.id.tv_forget_password);
        tvSignUp = (TextView) findViewById(R.id.tv_sign_up);

        // add listeners
        btnLogin.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
        }
    }

    private void login() {
        // validate inputs
        if (Utils.isEmpty(etPhoneNo)) {
            etPhoneNo.setError(getString(R.string.required));
            return;
        }
        if (Utils.isEmpty(etPassword)) {
            etPassword.setError(getString(R.string.required));
            return;
        }

        hideKeyboard();

        // check internet connection
        if (!Utils.hasConnection(this)) {
            Utils.showShortToast(this, R.string.no_internet_connection);
            return;
        }

        showProgressDialog();

        // create the request body
        LoginBody body = new LoginBody();
        body.setPhone(Utils.getText(etPhoneNo));
        body.setPassword(Utils.getText(etPassword));

        // send request
        ApiRequests.login(this, this, body);
    }

    @Override
    public void onSuccess(Object response, String tag) {
        hideProgressDialog();
        User user = (User) response;
        if (user != null && user.getId() != 0) {
            // save it
            UserController userController = new UserController(this, user);
            userController.save();

            // goto main activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Utils.showLongToast(this, R.string.invalid_phone_no_or_password);
        }
    }
}