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
import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.dialogs.ForgetPasswordDialog;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.AppUtils;
import com.stadium.app.utils.Utils;

public class LoginActivity extends ParentActivity {
    private EditText etPhone;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvForgetPassword;
    private TextView tvSignUp;
    private ForgetPasswordDialog forgetPasswordDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // init views
        etPhone = (EditText) findViewById(R.id.et_phone);
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

            case R.id.tv_forget_password:
                forgetPassword();
                break;

            case R.id.tv_sign_up:
                signUp();
                break;
        }
    }

    private void login() {
        // prepare params
        String phone = Utils.getText(etPhone);
        String password = Utils.getText(etPassword);

        // validate params
        if (Utils.isEmpty(phone)) {
            etPhone.setError(getString(R.string.required));
            return;
        }
        if (Utils.isEmpty(password)) {
            etPassword.setError(getString(R.string.required));
            return;
        }
        if (password.length() < Const.USER_PASSWORD_MIN_CHARS) {
            etPassword.setError(getString(R.string.too_short_password));
            return;
        }

        hideKeyboard();

        // check internet connection
        if (!Utils.hasConnection(this)) {
            Utils.showShortToast(this, R.string.no_internet_connection);
            return;
        }

        showProgressDialog();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.login(this, this, phone, password);
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        hideProgressDialog();
        User user = (User) response;
        if (statusCode == Const.SER_CODE_200) {
            // save him
            ActiveUserController userController = new ActiveUserController(this);
            userController.setUser(user);
            userController.save();

            // check his role in the system, if admin or not to goto suitable activity
            Intent intent;
            if (userController.isAdmin()) {
                intent = new Intent(this, AdminMainActivity.class);
            } else {
                intent = new Intent(this, PlayerMainActivity.class);
            }

            // goto suitable activity
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Utils.showShortToast(this, AppUtils.getResponseMsg(this, response));
        }
    }

    private void forgetPassword() {
        forgetPasswordDialog = new ForgetPasswordDialog(this);
        forgetPasswordDialog.setPhoneNumber(Utils.getText(etPhone));
        forgetPasswordDialog.show();
    }

    private void signUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}