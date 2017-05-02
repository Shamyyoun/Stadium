package com.stormnology.stadium.dialogs;

import android.content.Context;
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
import com.stormnology.stadium.models.entities.User;
import com.stormnology.stadium.utils.AppUtils;
import com.stormnology.stadium.utils.Utils;


/**
 * Created by Shamyyoun on 2/17/2016.
 */
public class ChangePasswordDialog extends ParentDialog {
    private ActiveUserController activeUserController;

    private EditText etCurrentPassword;
    private EditText etNewPassword;
    private EditText etReNewPassword;
    private Button btnSubmit;

    public ChangePasswordDialog(Context context) {
        super(context);

        // obtain main objects
        activeUserController = new ActiveUserController(context);

        // customize dialog
        setContentView(R.layout.dialog_change_password);
        setTitle(R.string.change_password);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init views
        etCurrentPassword = (EditText) findViewById(R.id.et_current_password);
        etNewPassword = (EditText) findViewById(R.id.et_new_password);
        etReNewPassword = (EditText) findViewById(R.id.et_re_new_password);
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        // add listeners
        btnSubmit.setOnClickListener(this);
        etReNewPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    changePassword();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_submit) {
            changePassword();
        } else {
            super.onClick(v);
        }
    }

    private void changePassword() {
        // get user
        User user = activeUserController.getUser();

        // prepare inputs
        String currentPassword = Utils.getText(etCurrentPassword);
        String newPassword = Utils.getText(etNewPassword);
        String reNewPassword = Utils.getText(etReNewPassword);

        // validate inputs
        if (Utils.isEmpty(currentPassword)) {
            etCurrentPassword.setError(getString(R.string.required));
            return;
        }
        if (!currentPassword.equals(user.getPassword())) {
            etCurrentPassword.setError(getString(R.string.password_is_incorrect));
            return;
        }
        if (Utils.isEmpty(newPassword)) {
            etNewPassword.setError(getString(R.string.required));
            return;
        }
        if (newPassword.length() < Const.USER_PASSWORD_MIN_CHARS) {
            etNewPassword.setError(getString(R.string.too_short_password));
            return;
        }
        if (Utils.isEmpty(reNewPassword)) {
            etReNewPassword.setError(getString(R.string.required));
            return;
        }
        if (!newPassword.equals(reNewPassword)) {
            etReNewPassword.setError(getString(R.string.passwords_dont_match));
            return;
        }

        hideKeyboard();

        // check internet connection
        if (!Utils.hasConnection(context)) {
            Utils.showShortToast(context, R.string.no_internet_connection);
            return;
        }

        showProgressView();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.changePassword(context, this, user.getId(),
                user.getToken(), user.getPassword(), newPassword);
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        hideProgressView();

        // check status code
        if (statusCode == Const.SER_CODE_200) {
            // update user password
            String password = Utils.getText(etNewPassword);
            activeUserController.updatePassword(password);

            // show msg and dismiss
            Utils.showShortToast(context, R.string.password_changed_successfully);
            dismiss();
        } else {
            // show msg
            String msg = AppUtils.getResponseMsg(context, response, R.string.failed_changing_password);
            Utils.showShortToast(context, msg);
        }
    }

    @Override
    public void show() {
        // reset views and show
        resetViews();
        super.show();
    }

    private void resetViews() {
        Utils.emptyEditText(etCurrentPassword);
        Utils.emptyEditText(etNewPassword);
        Utils.emptyEditText(etReNewPassword);
    }
}
