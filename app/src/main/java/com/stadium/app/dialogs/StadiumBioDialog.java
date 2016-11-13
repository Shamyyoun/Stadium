package com.stadium.app.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stadium.app.R;
import com.stadium.app.models.entities.Stadium;

/**
 * Created by karam on 7/31/16.
 */
public class StadiumBioDialog extends ParentDialog {
    private Stadium stadium;
    private TextView tvText;
    private Button btnClose;

    public StadiumBioDialog(Context context, Stadium stadium) {
        super(context);
        setContentView(R.layout.dialog_stadium_bio);
        this.stadium = stadium;

        // init views
        tvText = (TextView) findViewById(R.id.tv_text);
        btnClose = (Button) findViewById(R.id.btn_close);

        // set bio
        tvText.setText(stadium.getBio());

        // customize the close button
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
