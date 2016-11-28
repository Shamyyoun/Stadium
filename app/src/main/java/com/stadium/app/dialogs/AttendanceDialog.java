package com.stadium.app.dialogs;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.stadium.app.ApiRequests;
import com.stadium.app.R;
import com.stadium.app.adapters.AttendanceAdapter;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.controllers.AttendanceController;
import com.stadium.app.interfaces.OnRefreshListener;
import com.stadium.app.models.entities.Attendant;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.Utils;
import com.stadium.app.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shamyyoun on 6/28/16.
 */
public class AttendanceDialog extends ProgressDialog {
    private int id;
    private RecyclerView recyclerView;
    private Button btnClose;
    private AttendanceAdapter adapter;
    private List<Attendant> data;

    public AttendanceDialog(final Context context, int reservationId) {
        super(context);
        setTitle(R.string.confirm_attendance);

        // set the reservation id
        id = reservationId;

        // init views
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        btnClose = (Button) findViewById(R.id.btn_close);

        // customize the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));

        // add click listeners
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // load the data
        loadData();
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.dialog_attendance;
    }

    @Override
    protected int getMainViewResId() {
        return R.id.recycler_view;
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
        adapter = new AttendanceAdapter(context, data, R.layout.item_checkable_player, id);
        adapter.setWrapperDialog(this);
        recyclerView.setAdapter(adapter);
        showMain();
    }

    private void loadData() {
        // check internet connection
        if (!Utils.hasConnection(context)) {
            showError(R.string.no_internet_connection);
            return;
        }

        showProgress();

        // get current user
        ActiveUserController userController = new ActiveUserController(context);
        User user = userController.getUser();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.playerConfirmList(context, this, user.getId(),
                user.getToken(), id);
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