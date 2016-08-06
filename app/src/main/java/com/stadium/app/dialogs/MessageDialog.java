package com.stadium.app.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stadium.app.R;


/**
 * Created by Shamyyoun on 2/17/2016.
 */
public class MessageDialog extends ParentDialog {
    private TextView tvMessage;
    private Button btnClose;

    public MessageDialog(Context context, String msg) {
        super(context);
        setContentView(R.layout.dialog_message);

        // init views
        tvMessage = (TextView) findViewById(R.id.tv_message);
        btnClose = (Button) findViewById(R.id.btn_close);

        // set the message
        tvMessage.setText(msg);

        // customize the close button
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public MessageDialog(Context context, int msgId) {
        this(context, context.getString(msgId));
    }

    public void setOnCloseClickListener(View.OnClickListener closeClickListener) {
        btnClose.setOnClickListener(closeClickListener);
    }
}
