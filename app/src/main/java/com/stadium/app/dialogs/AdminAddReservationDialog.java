package com.stadium.app.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.stadium.app.ApiRequests;
import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.interfaces.OnReservationAddedListener;
import com.stadium.app.models.entities.Reservation;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.AppUtils;
import com.stadium.app.utils.DateUtils;
import com.stadium.app.utils.Utils;

/**
 * Created by karam on 8/11/16.
 */
public class AdminAddReservationDialog extends ParentDialog {
    private static final String DISPLAY_TIME_FORMAT = "hh:mm a";

    private Reservation reservation;
    private TextView tvFieldNo;
    private TextView tvDate;
    private TextView tvTime;
    private EditText etName;
    private EditText etPhone;
    private Button btnSubmit;
    private OnReservationAddedListener reservationAddedListener;

    public AdminAddReservationDialog(Context context, Reservation reservation) {
        super(context);
        this.reservation = reservation;
        setContentView(R.layout.dialog_admin_add_reservation);
        setTitle(R.string.reservation_details);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inti views
        tvFieldNo = (TextView) findViewById(R.id.tv_field_no);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvTime = (TextView) findViewById(R.id.tv_time);
        etName = (EditText) findViewById(R.id.et_name);
        etPhone = (EditText) findViewById(R.id.et_phone);
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        // add listeners
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reserve();
            }
        });

        // update the ui
        updateUI();
    }

    private void updateUI() {
        // set field number
        String fieldNoStr = getString(R.string.field_c) + " ";
        if (reservation.getField() != null && !Utils.isNullOrEmpty(reservation.getField().getFieldNumber())) {
            fieldNoStr += reservation.getField().getFieldNumber();
        } else {
            fieldNoStr += "--";
        }
        tvFieldNo.setText(fieldNoStr);

        // set date
        String dateStr = getString(R.string.date_c) + " ";
        if (!Utils.isNullOrEmpty(reservation.getDate())) {
            dateStr += reservation.getDate();
        } else {
            dateStr += "----/--/--";
        }
        tvDate.setText(dateStr);

        // set time
        String timeStart = DateUtils.formatDate(reservation.getTimeStart(), Const.SER_TIME_FORMAT, DISPLAY_TIME_FORMAT);
        String timeEnd = DateUtils.formatDate(reservation.getTimeEnd(), Const.SER_TIME_FORMAT, DISPLAY_TIME_FORMAT);
        String time = timeStart + " " + getString(R.string.to) + " " + timeEnd;
        tvTime.setText(getString(R.string.time_c) + " " + time);
    }

    private void reserve() {
        // prepare inputs
        String name = Utils.getText(etName);
        String phone = Utils.getText(etPhone);

        // validate inputs
        if (Utils.isEmpty(name)) {
            etName.setError(getString(R.string.required));
            return;
        }
        if (Utils.isEmpty(phone)) {
            etPhone.setError(getString(R.string.required));
            return;
        }

        hideKeyboard();

        // check internet connection
        if (!Utils.hasConnection(context)) {
            Utils.showShortToast(context, R.string.no_internet_connection);
            return;
        }

        showProgressView();

        // get active user
        ActiveUserController userController = new ActiveUserController(context);
        User user = userController.getUser();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.adminAddReservation(context, this,
                user.getId(), user.getToken(), user.getAdminStadium().getId(), name, phone,
                reservation.getIntrvalNum(), reservation.getPrice(), reservation.getField().getId(),
                reservation.getDate(), reservation.getTimeStart(), reservation.getTimeEnd());
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        hideProgressView();
        Reservation myResponse = (Reservation) response;

        // check status code
        if (statusCode == Const.SER_CODE_200) {
            // show msg
            Utils.showShortToast(context, R.string.reservation_added_successfully);

            // fire the listener if possible
            if (reservationAddedListener != null) {
                reservationAddedListener.onReservationAdded(myResponse);
            }

            dismiss();
        } else {
            // show error msg
            String errorMsg = AppUtils.getResponseError(context, response, R.string.failed_adding_reservation);
            Utils.showShortToast(context, errorMsg);
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        hideProgressView();
        Utils.showShortToast(context, R.string.failed_adding_reservation);
    }

    public void setOnReservationAddedListener(OnReservationAddedListener reservationAddedListener) {
        this.reservationAddedListener = reservationAddedListener;
    }
}
