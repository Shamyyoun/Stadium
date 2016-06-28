package com.stadium.player.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.stadium.player.R;
import com.stadium.player.utils.AppUtils;

/**
 * Created by Shamyyoun on 6/28/16.
 */
public class ForgetPasswordDialog extends ParentDialog {
    private Button btnVerify;

    public ForgetPasswordDialog(final Context context) {
        super(context);
        setContentView(R.layout.dialog_forget_password);
        setTitle(R.string.forget_password_q);

        btnVerify = (Button) findViewById(R.id.btn_verify);
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.showMessageDialog(context, R.string.empty_string, R.string.forget_password_success_msg);
                dismiss();
            }
        });
    }
}
