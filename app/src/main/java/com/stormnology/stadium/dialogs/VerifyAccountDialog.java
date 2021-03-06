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
import com.stormnology.stadium.interfaces.OnVerifyAccountListener;
import com.stormnology.stadium.utils.AppUtils;
import com.stormnology.stadium.utils.Utils;


/**
 * Created by Shamyyoun on 2/17/2016.
 */
public class VerifyAccountDialog extends ParentDialog {
    private int userId;

    private EditText etCode;
    private TextView tvResendCode;
    private Button btnSubmit;

    private OnVerifyAccountListener onVerifyAccountListener;

    public VerifyAccountDialog(Context context, int userId) {
        super(context);
        this.userId = userId;

        // customize dialog
        setContentView(R.layout.dialog_verify_account);
        setTitle(R.string.activate_account);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init views
        etCode = (EditText) findViewById(R.id.et_code);
        tvResendCode = (TextView) findViewById(R.id.tv_resend_code);
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        // add listeners
        tvResendCode.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        etCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    validationPhone();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_resend_code) {
            resendCode();
        } else if (v.getId() == R.id.btn_submit) {
            validationPhone();
        } else {
            super.onClick(v);
        }
    }

    private void validationPhone() {
        // prepare inputs
        String code = Utils.getText(etCode);

        // validate inputs
        if (Utils.isEmpty(code)) {
            etCode.setError(getString(R.string.enter_code));
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
        ConnectionHandler connectionHandler = ApiRequests.phoneValidation(context, this, userId, code);
        cancelWhenDestroyed(connectionHandler);
    }

    private void resendCode() {
        // check internet connection
        if (!Utils.hasConnection(context)) {
            Utils.showShortToast(context, R.string.no_internet_connection);
            return;
        }

        showProgressView();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.resendValidation(context, this, userId);
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        hideProgressView();

        // check tag and handle response
        switch (tag) {
            case Const.API_PHONE_VALIDATION:
                handlePhoneValidationResponse(response);
                break;

            case Const.API_RESEND_VALIDATION:
                handleResendValidationResponse(response);
                break;
        }
    }

    private void handlePhoneValidationResponse(Object response) {
        // parse & check response
        Boolean boolResponse = (Boolean) response;
        if (Utils.checkBoolean(boolResponse)) {
            fireVerifyAccountListener();
        } else {
            // show msg
            String msg = AppUtils.getResponseMsg(context, response, R.string.activation_failed_check_code_try_again);
            Utils.showLongToast(context, msg);
        }
    }

    private void fireVerifyAccountListener() {
        if (onVerifyAccountListener != null) {
            onVerifyAccountListener.onAccountVerified();
        }
    }

    private void handleResendValidationResponse(Object response) {
        // parse & check response
        Boolean boolResponse = (Boolean) response;
        if (Utils.checkBoolean(boolResponse)) {
            // clear old code and show msg
            etCode.setText("");
            Utils.showLongToast(context, R.string.code_sent_successfully);
        } else {
            // show msg
            String msg = AppUtils.getResponseMsg(context, response, R.string.failed_sending_code);
            Utils.showShortToast(context, msg);
        }
    }

    public void setOnVerifyAccountListener(OnVerifyAccountListener onVerifyAccountListener) {
        this.onVerifyAccountListener = onVerifyAccountListener;
    }
}
