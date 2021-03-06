package com.stormnology.stadium.dialogs;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.stormnology.stadium.ApiRequests;
import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.utils.AppUtils;
import com.stormnology.stadium.utils.Utils;

/**
 * Created by Shamyyoun on 6/28/16.
 */
public class ForgetPasswordDialog extends ParentDialog {
    private static final int VIEW_PASSWORD_INPUT = 1;
    private static final int VIEW_METHOD_CHOOSING = 2;

    private View layoutPhoneInput;
    private EditText etPhone;
    private View layoutMethodChoosing;
    private RadioGroup rgVerifyMethod;
    private Button btnSubmit;

    private int currentView = VIEW_PASSWORD_INPUT; // default view
    private int resetType;

    public ForgetPasswordDialog(final Context context) {
        super(context);
        setContentView(R.layout.dialog_forget_password);
        setTitle(R.string.forget_password_q);

        // init views
        layoutPhoneInput = findViewById(R.id.layout_phone_input);
        etPhone = (EditText) findViewById(R.id.et_phone);
        layoutMethodChoosing = findViewById(R.id.layout_method_choosing);
        rgVerifyMethod = (RadioGroup) findViewById(R.id.rg_verify_method);
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        // add listeners
        btnSubmit.setOnClickListener(this);
        etPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    checkPhone();
                    return true;
                }
                return false;
            }
        });
    }

    public void setPhoneNumber(String phone) {
        etPhone.setText(phone);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_submit) {
            // check the current view
            if (currentView == VIEW_PASSWORD_INPUT) {
                checkPhone();
            } else {
                sendResetEmail();
            }
        } else {
            super.onClick(v);
        }
    }

    private void checkPhone() {
        // prepare params
        String phone = Utils.getText(etPhone);

        // validate params
        if (Utils.isEmpty(phone)) {
            etPhone.setError(getString(R.string.required));
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
        ConnectionHandler connectionHandler = ApiRequests.checkEmail(context, this, phone);
        cancelWhenDestroyed(connectionHandler);
    }

    private void sendResetEmail() {
        hideKeyboard();

        // prepare params
        resetType = rgVerifyMethod.getCheckedRadioButtonId() == R.id.rb_by_mail ? 1 : 2;
        String phone = Utils.getText(etPhone);

        // check internet connection
        if (!Utils.hasConnection(context)) {
            Utils.showShortToast(context, R.string.no_internet_connection);
            return;
        }

        showProgressView();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.forgetPassword(context, this, resetType, phone);
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        hideProgressView();

        switch (tag) {
            case Const.API_CHECK_EMAIL:
                // check status code
                if (statusCode == Const.SER_CODE_200) {
                    switchMethodChoosingView();
                } else {
                    // show error
                    Utils.showLongToast(context, AppUtils.getResponseMsg(context, response));
                }
                break;

            case Const.API_FORGET_PASSWORD:
                // check status code
                if (statusCode == Const.SER_CODE_200) {
                    // show the suitable msg according to selected method
                    int msgId;
                    if (resetType == 1) {
                        msgId = R.string.forget_password_using_email_success_msg;
                    } else {
                        msgId = R.string.forget_password_using_phone_success_msg;
                    }

                    // show msg dialog
                    AppUtils.showMessageDialog(context, R.string.empty_string, msgId);
                    dismiss();
                } else {
                    // show error
                    Utils.showLongToast(context, AppUtils.getResponseMsg(context, response));
                }
                break;

            default:
                super.onSuccess(response, statusCode, tag);
        }
    }

    private void switchMethodChoosingView() {
        layoutPhoneInput.setVisibility(View.GONE);
        layoutMethodChoosing.setVisibility(View.VISIBLE);
        btnSubmit.setText(R.string.select);
        currentView = VIEW_METHOD_CHOOSING;
    }
}
