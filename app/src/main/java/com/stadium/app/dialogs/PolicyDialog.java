package com.stadium.app.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stadium.app.R;
import com.stadium.app.interfaces.ConfirmListener;
import com.stadium.app.utils.Utils;


/**
 * Created by Shamyyoun on 2/17/2016.
 */
public class PolicyDialog extends ParentDialog {
    private TextView tvPolicy;
    private Button btnAccept;
    private Button btnNotAccept;

    private String policyText;
    private ConfirmListener confirmListener;

    public PolicyDialog(Context context) {
        super(context);

        // customize dialog
        setContentView(R.layout.dialog_policy);
        setTitle(R.string.terms_and_conditions);

        // load policy text
        policyText = Utils.getRawText(context, R.raw.app_policy);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init views
        tvPolicy = (TextView) findViewById(R.id.tv_policy);
        btnAccept = (Button) findViewById(R.id.btn_accept);
        btnNotAccept = (Button) findViewById(R.id.btn_not_accept);

        // add listeners
        btnAccept.setOnClickListener(this);
        btnNotAccept.setOnClickListener(this);

        updatePolicyUI();
    }

    private void updatePolicyUI() {
        tvPolicy.setText(policyText);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_accept) {
            confirm(true);
        } else if (v.getId() == R.id.btn_not_accept) {
            confirm(false);
        } else {
            super.onClick(v);
        }
    }

    private void confirm(boolean flag) {
        if (confirmListener != null) {
            if (flag) {
                confirmListener.onAccept();
            } else {
                confirmListener.onDecline();
            }
        }

        dismiss();
    }

    public void setConfirmListener(ConfirmListener confirmListener) {
        this.confirmListener = confirmListener;
    }
}
