package com.stadium.app.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stadium.app.R;
import com.stadium.app.models.entities.Stadium;
import com.stadium.app.utils.Utils;

/**
 * Created by karam on 7/31/16.
 */
public class StadiumContactsDialog extends ParentDialog {
    private Stadium stadium;
    private TextView tvEmail;
    private TextView tvPhone;
    private Button btnClose;

    public StadiumContactsDialog(Context context, Stadium stadium) {
        super(context);
        setContentView(R.layout.dialog_stadium_contacts);

        // assign stadium object
        this.stadium = stadium;

        // init views
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        btnClose = (Button) findViewById(R.id.btn_close);

        // update the ui
        updateUI();

        // add listeners
        tvEmail.setOnClickListener(this);
        tvPhone.setOnClickListener(this);
        btnClose.setOnClickListener(this);
    }

    private void updateUI() {
        // set email
        String email = stadium.getEmail();
        if (!Utils.isNullOrEmpty(email)) {
            tvEmail.setText(email);
        } else {
            tvEmail.setVisibility(View.GONE);
        }

        // set phone
        String phone = stadium.getPhoneNumber();
        if (!Utils.isNullOrEmpty(phone)) {
            tvPhone.setText(phone);
        } else {
            tvPhone.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_email:
                onEmail();
                break;

            case R.id.tv_phone:
                onPhone();
                break;

            case R.id.btn_close:
                dismiss();
                break;

            default:
                super.onClick(v);
        }
    }

    private void onEmail() {
        // open email intent if possible
        String email = stadium.getEmail();
        if (!Utils.isNullOrEmpty(email)) {
            Utils.openEmailIntent(context, email);
        }
    }

    private void onPhone() {
        // open phone intent if possible
        String phone = stadium.getPhoneNumber();
        if (!Utils.isNullOrEmpty(phone)) {
            Utils.openPhoneIntent(context, phone);
        }
    }
}
