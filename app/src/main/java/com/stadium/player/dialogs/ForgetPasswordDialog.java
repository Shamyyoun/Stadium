package com.stadium.player.dialogs;

import android.content.Context;

import com.stadium.player.R;

/**
 * Created by Shamyyoun on 6/28/16.
 */
public class ForgetPasswordDialog extends ParentDialog {

    public ForgetPasswordDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_forget_password);
        setTitle(R.string.forget_password_q);
    }
}
