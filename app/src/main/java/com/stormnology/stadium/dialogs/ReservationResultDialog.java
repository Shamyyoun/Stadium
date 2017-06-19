package com.stormnology.stadium.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stormnology.stadium.R;
import com.stormnology.stadium.controllers.ReservationController;
import com.stormnology.stadium.models.entities.Reservation;
import com.stormnology.stadium.utils.Utils;


/**
 * Created by Shamyyoun on 2/17/2016.
 */
public class ReservationResultDialog extends ParentDialog {
    private Reservation reservation;
    private ReservationController reservationController;
    private TextView tvShareInfo;
    private Button btnClose;

    public ReservationResultDialog(Context context, Reservation reservation) {
        super(context);
        setContentView(R.layout.dialog_reservation_result);
        setTitle(R.string.reservation_result);

        // obtain main objects
        this.reservation = reservation;
        reservationController = new ReservationController();

        // init views
        tvShareInfo = (TextView) findViewById(R.id.tv_share_info);
        btnClose = (Button) findViewById(R.id.btn_close);

        // add listeners
        tvShareInfo.setOnClickListener(this);
        btnClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_share_info:
                shareInfo();
                break;

            case R.id.btn_close:
                dismiss();
                break;
        }
    }

    private void shareInfo() {
        // get the sharable text
        String text = reservationController.getShareableText(context, reservation);

        // share it
        Utils.shareText(context, text);
    }
}
