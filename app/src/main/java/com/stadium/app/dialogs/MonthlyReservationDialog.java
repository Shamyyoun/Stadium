package com.stadium.app.dialogs;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.stadium.app.ApiRequests;
import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.adapters.MonthlyReservationAltAdapter;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.controllers.MonthlyReservationController;
import com.stadium.app.interfaces.OnReservationAddedListener;
import com.stadium.app.models.entities.RepeatedReservation;
import com.stadium.app.models.entities.User;
import com.stadium.app.models.enums.MonthlyReservationStatusType;
import com.stadium.app.models.responses.MonthlyReservationResponse;
import com.stadium.app.utils.AppUtils;
import com.stadium.app.utils.DateUtils;
import com.stadium.app.utils.Utils;


/**
 * Created by Shamyyoun on 2/17/2016.
 */
public class MonthlyReservationDialog extends ParentDialog {
    private static final String DISPLAYED_DATE_FORMAT = "d-M-yyyy";

    private RepeatedReservation repeatedReservation;
    private MonthlyReservationResponse monthlyReservationResponse;
    private MonthlyReservationController monthlyReservationController;

    private ScrollView scrollView;
    private TextView tvTeamName;
    private TextView tvPrice;
    private TextView tvDuration;
    private TextView tvDateFrom;
    private TextView tvDateTo;
    private TextView tvDay;
    private TextView tvStatus;
    private ListView listView;
    private Button btnReserve;
    private Button btnCancel;
    private OnReservationAddedListener onReservationAddedListener;

    public MonthlyReservationDialog(Context context, RepeatedReservation repeatedReservation,
                                    MonthlyReservationResponse monthlyReservationResponse) {
        super(context);

        // set main fields
        this.repeatedReservation = repeatedReservation;
        this.monthlyReservationResponse = monthlyReservationResponse;
        monthlyReservationController = new MonthlyReservationController();

        // customize dialog
        setContentView(R.layout.dialog_monthly_reservation);
        setTitle(R.string.repeated_reservation);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init views
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        tvTeamName = (TextView) findViewById(R.id.tv_team_name);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvDuration = (TextView) findViewById(R.id.tv_duration);
        tvDateFrom = (TextView) findViewById(R.id.tv_date_from);
        tvDateTo = (TextView) findViewById(R.id.tv_date_to);
        tvDay = (TextView) findViewById(R.id.tv_day);
        tvStatus = (TextView) findViewById(R.id.tv_status);
        listView = (ListView) findViewById(R.id.list_view);
        btnReserve = (Button) findViewById(R.id.btn_reserve);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        // add listeners
        btnReserve.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        updateUI();
    }

    private void updateUI() {
        // set basic data
        tvTeamName.setText(repeatedReservation.getTeam().getName());
        tvDuration.setText(repeatedReservation.getDuration().toString());
        tvDay.setText(repeatedReservation.getDay().getTitle());

        // set price
        String price = Utils.formatDouble(repeatedReservation.getPrice());
        price = AppUtils.getFormattedPrice(context, price);
        tvPrice.setText(price);

        // set date from
        String dateFrom = DateUtils.formatDate(repeatedReservation.getDateFrom(), Const.SER_DATE_FORMAT, DISPLAYED_DATE_FORMAT);
        tvDateFrom.setText(dateFrom);

        // set date to
        String dateTo = DateUtils.formatDate(repeatedReservation.getDateTo(), Const.SER_DATE_FORMAT, DISPLAYED_DATE_FORMAT);
        tvDateTo.setText(dateTo);

        // set status
        MonthlyReservationStatusType statusType = monthlyReservationController.getStatusType(context, monthlyReservationResponse.getStatus());
        tvStatus.setText(statusType.getTitle(context));

        // customize listview
        if (statusType != MonthlyReservationStatusType.NOT_AVAILABLE) {
            // set the list adapter
            MonthlyReservationAltAdapter adapter = new MonthlyReservationAltAdapter(context,
                    monthlyReservationResponse.getAvailableReservations(), R.layout.item_monthly_reservation_alt);
            listView.setAdapter(adapter);
            ObjectAnimator.ofInt(scrollView, "scrollY", 0).setDuration(50).start();
        }

        // customize buttons
        if (statusType == MonthlyReservationStatusType.NOT_AVAILABLE) {
            // hide reserve btn
            btnReserve.setVisibility(View.GONE);
            btnCancel.setText(R.string.close);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_reserve) {
            reserve();
        } else if (v.getId() == R.id.btn_cancel) {
            dismiss();
        } else {
            super.onClick(v);
        }
    }

    private void reserve() {
        // check internet connection
        if (!Utils.hasConnection(context)) {
            Utils.showShortToast(context, R.string.no_internet_connection);
            return;
        }

        showProgressView();

        // get current user
        ActiveUserController activeUserController = new ActiveUserController(context);
        User user = activeUserController.getUser();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.addMonthlyReservations(context, this, user.getId(),
                user.getToken(), user.getAdminStadium().getId(), monthlyReservationResponse,
                repeatedReservation.getTeam().getId(), repeatedReservation.getPrice());
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        hideProgressView();

        // check status code
        if (statusCode == Const.SER_CODE_200) {
            // fire the listener if possible
            if (onReservationAddedListener != null) {
                onReservationAddedListener.onReservationAdded(null);
            }

            // show msg and dismiss
            Utils.showShortToast(context, R.string.reservation_added_successfully);
            dismiss();
        } else {
            // show msg
            String msg = AppUtils.getResponseMsg(context, response, R.string.failed_adding_reservation);
            Utils.showLongToast(context, msg);
        }
    }

    public void setOnReservationAddedListener(OnReservationAddedListener onReservationAddedListener) {
        this.onReservationAddedListener = onReservationAddedListener;
    }
}
