package com.stadium.app.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.stadium.app.R;
import com.stadium.app.utils.AppUtils;
import com.stadium.app.utils.ViewUtil;

/**
 * Created by Shamyyoun on 6/28/16.
 */
public class ForgetPasswordDialog extends ParentDialog {
    private static final int VIEW_PASSWORD_INPUT = 1;
    private static final int VIEW_METHOD_CHOOSING = 2;

    private View layoutMobileInput;
    private View layoutMethodChoosing;
    private Button btnSubmit;

    private int currentView = VIEW_PASSWORD_INPUT; // default view

    public ForgetPasswordDialog(final Context context) {
        super(context);
        setContentView(R.layout.dialog_forget_password);
        setTitle(R.string.forget_password_q);

        // init views
        layoutMobileInput = findViewById(R.id.layout_mobile_input);
        layoutMethodChoosing = findViewById(R.id.layout_method_choosing);
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        // add listeners
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check the current view
                if (currentView == VIEW_PASSWORD_INPUT) {
                    // switch to next view
                    ViewUtil.fadeView(layoutMobileInput, false);
                    ViewUtil.fadeView(layoutMethodChoosing, true);
                    btnSubmit.setText(R.string.select);
                    currentView = VIEW_METHOD_CHOOSING;
                } else {
                    // show msg dialog and dismiss
                    AppUtils.showMessageDialog(context, R.string.empty_string, R.string.forget_password_success_msg);
                    dismiss();
                }
            }
        });
    }
}
