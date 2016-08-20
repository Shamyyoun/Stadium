package com.stadium.app.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stadium.app.R;

/**
 * Created by karam on 8/11/16.
 */
public class AdminStadeDialog extends ParentDialog {

    private TextView stadiumNumber;
    private TextView date;
    private TextView time;
    private EditText name;
    private EditText phoneNumber;
    private Button btnReserve;

    public AdminStadeDialog(Context context , String tvStadium , String tvDate , String tvTime) {
        super(context);
        setContentView(R.layout.dialog_admin_stade);

        // init views
        stadiumNumber = (TextView) findViewById(R.id.tv_stadium_number);
        date = (TextView) findViewById(R.id.tv_date);
        time = (TextView) findViewById(R.id.tv_time);

        stadiumNumber.setText(tvStadium);
        date.setText(tvDate);
        time.setText(tvTime);

        btnReserve = (Button) findViewById(R.id.btn_reserve);

    }

}
