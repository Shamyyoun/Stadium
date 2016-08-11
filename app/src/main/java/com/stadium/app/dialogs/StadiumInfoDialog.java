package com.stadium.app.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stadium.app.R;

/**
 * Created by karam on 7/31/16.
 */
public class StadiumInfoDialog  extends ParentDialog {

    private TextView tvInfo;
    private Button btnExit;

    public StadiumInfoDialog(Context context ) {
        super(context);
        setContentView(R.layout.dialog_stadium_info);

        // init views
        tvInfo = (TextView) findViewById(R.id.tv_info);
        btnExit = (Button) findViewById(R.id.btn_exit);


        // customize the close button
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }


    public void setOnCloseClickListener(View.OnClickListener closeClickListener) {
        btnExit.setOnClickListener(closeClickListener);
    }
}
