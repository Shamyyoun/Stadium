package com.stormnology.stadium.dialogs;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stormnology.stadium.ApiRequests;
import com.stormnology.stadium.R;
import com.stormnology.stadium.adapters.AttendanceAdapter;
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.controllers.AttendanceController;
import com.stormnology.stadium.controllers.ReservationController;
import com.stormnology.stadium.interfaces.OnRefreshListener;
import com.stormnology.stadium.models.entities.Attendant;
import com.stormnology.stadium.models.entities.Reservation;
import com.stormnology.stadium.models.entities.User;
import com.stormnology.stadium.utils.Utils;
import com.stormnology.stadium.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shamyyoun on 6/28/16.
 */
public class AttendanceDialog extends ProgressDialog {
    private Reservation reservation;
    private ReservationController reservationController;

    private TextView tvShareInfo;
    private RecyclerView recyclerView;
    private Button btnClose;
    private AttendanceAdapter adapter;
    private List<Attendant> data;

    public AttendanceDialog(final Context context, Reservation reservation) {
        super(context);
        setTitle(R.string.confirm_attendance);

        // obtain main objects
        this.reservation = reservation;
        reservationController = new ReservationController();

        // init views
        tvShareInfo = (TextView) findViewById(R.id.tv_share_info);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        btnClose = (Button) findViewById(R.id.btn_close);

        // customize the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));

        // add click listeners
        tvShareInfo.setOnClickListener(this);
        btnClose.setOnClickListener(this);

        // load the data
        loadData();
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.dialog_attendance;
    }

    @Override
    protected int getMainViewResId() {
        return R.id.layout_main;
    }

    @Override
    protected OnRefreshListener getOnRefreshListener() {
        return new OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        };
    }

    private void updateUI() {
        adapter = new AttendanceAdapter(context, data, R.layout.item_checkable_player, reservation);
        adapter.setWrapperDialog(this);
        recyclerView.setAdapter(adapter);
        showMain();
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

            default:
                super.onClick(v);
        }
    }

    private void shareInfo() {
        // get the sharable text
        String text = reservationController.getShareableText(context, reservation);

        // share it
        Utils.shareText(context, text);
    }

    private void loadData() {
        // check internet connection
        if (!Utils.hasConnection(context)) {
            showError(R.string.no_internet_connection);
            return;
        }

        showProgress();

        // get current user
        ActiveUserController activeUserController = new ActiveUserController(context);
        User user = activeUserController.getUser();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.playerConfirmList(context, this, user.getId(),
                user.getToken(), reservation.getId());
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        // get data
        Attendant[] attendanceArr = (Attendant[]) response;
        data = new ArrayList<>(Arrays.asList(attendanceArr));

        // check size
        if (data.size() == 0) {
            showEmpty(R.string.no_attendance_found);
        } else {
            // order the list
            AttendanceController attendanceController = new AttendanceController();
            data = attendanceController.orderAttendance(context, data);
            updateUI();
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        showError(R.string.failed_loading_attendance);
    }
}