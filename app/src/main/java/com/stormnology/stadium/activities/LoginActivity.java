package com.stormnology.stadium.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.stormnology.stadium.ApiRequests;
import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.controllers.ParseController;
import com.stormnology.stadium.dialogs.ForgetPasswordDialog;
import com.stormnology.stadium.dialogs.VerifyAccountDialog;
import com.stormnology.stadium.interfaces.OnVerifyAccountListener;
import com.stormnology.stadium.models.entities.User;
import com.stormnology.stadium.utils.AppUtils;
import com.stormnology.stadium.utils.Utils;

public class LoginActivity extends ParentActivity {
    private ActiveUserController activeUserController;
    private ParseController parseController;

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

        // obtain main objects
        activeUserController = new ActiveUserController(this);
        parseController = new ParseController(this);

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
            // check validation status
            if (user.isValidationStatus()) {
                handleSuccessfulLogin(user);
            } else {
                // show verify account dialog
                showVerifyAccountDialog(user);
            }
        } else {
            Utils.showShortToast(this, AppUtils.getResponseMsg(this, response));
        }
    }

    private void handleSuccessfulLogin(User user) {
        // set user password
        String password = Utils.getText(etPassword);
        user.setPassword(password);

        // save him
        activeUserController.setUser(user);
        activeUserController.save();

        // install parse
        parseController.install(user.getId(), user.getChannels());

        // check his role in the system, if admin or not to goto suitable activity
        Intent intent;
        if (activeUserController.isAdmin()) {
            intent = new Intent(this, AdminMainActivity.class);
        } else {
            intent = new Intent(this, PlayerMainActivity.class);
        }

        // goto suitable activity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void showVerifyAccountDialog(final User user) {
        // create the dialog and set its listener
        VerifyAccountDialog dialog = new VerifyAccountDialog(this, user.getId());
        dialog.setOnVerifyAccountListener(new OnVerifyAccountListener() {
            @Override
            public void onAccountVerified() {
                // change status and continue handling the login
                user.setValidationStatus(true);
                handleSuccessfulLogin(user);
            }
        });

        // show the dialog
        dialog.show();
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