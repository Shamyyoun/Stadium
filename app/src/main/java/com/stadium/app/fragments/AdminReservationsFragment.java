package com.stadium.app.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stadium.app.ApiRequests;
import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.adapters.ReservationsAdapter;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.models.SerializableListWrapper;
import com.stadium.app.models.entities.Reservation;
import com.stadium.app.models.entities.User;
import com.stadium.app.models.enums.ReservationsType;
import com.stadium.app.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shamyyoun on 7/2/16.
 */
public class AdminReservationsFragment extends ProgressFragment {
    private ReservationsType reservationsType;
    private ActiveUserController userController;
    private RecyclerView recyclerView;
    private List<Reservation> data;
    private ReservationsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // obtain main objects
        reservationsType = (ReservationsType) getArguments().getSerializable(Const.KEY_RESERVATIONS_TYPE);
        userController = new ActiveUserController(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // init views
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // customize the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // get data from saved bundle if exists
        if (savedInstanceState != null) {
            SerializableListWrapper<Reservation> dataWrapper = (SerializableListWrapper<Reservation>) savedInstanceState.getSerializable("dataWrapper");
            if (dataWrapper != null) {
                data = dataWrapper.getList();
            }
        }

        // check data
        if (data != null) {
            if (!data.isEmpty()) {
                updateUI();
            } else {
                showEmpty(R.string.no_reservations_found);
            }
        }

        return rootView;
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.fragment_admin_resrevations;
    }

    @Override
    protected int getMainViewResId() {
        return R.id.swipe_layout;
    }

    @Override
    protected OnRefreshListener getOnRefreshListener() {
        return new OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        };
    }

    private void updateUI() {
        adapter = new ReservationsAdapter(activity, data, R.layout.item_reservation);
        adapter.setReservationsType(reservationsType);
        recyclerView.setAdapter(adapter);
        showMain();
    }

    public void loadDataIfRequired() {
        if (data == null) {
            loadData();
        }
    }

    private void loadData() {
        // check internet connection
        if (!Utils.hasConnection(activity)) {
            showError(R.string.no_internet_connection);
            return;
        }

        // prepare params
        User user = userController.getUser();
        int userId = user.getId();
        String userToken = user.getToken();
        int stadiumId = user.getAdminStadium().getId();

        // send suitable request
        ConnectionHandler connectionHandler = null;
        if (reservationsType == ReservationsType.ADMIN_TODAY_RESERVATIONS) {
            connectionHandler = ApiRequests.todayReservations(activity, this, userId, userToken, stadiumId);
        } else if (reservationsType == ReservationsType.ADMIN_ACCEPTED_RESERVATIONS) {
            connectionHandler = ApiRequests.getReservations(activity, this, userId, userToken, stadiumId);
        } else if (reservationsType == ReservationsType.ADMIN_NEW_RESERVATIONS) {
            connectionHandler = ApiRequests.newReservations(activity, this, userId, userToken, stadiumId);
        } else if (reservationsType == ReservationsType.ADMIN_PREVIOUS_RESERVATIONS) {
            connectionHandler = ApiRequests.lastReservations(activity, this, userId, userToken, stadiumId);
        } else if (reservationsType == ReservationsType.ADMIN_MY_RESERVATIONS) {
            connectionHandler = ApiRequests.getMyReservations(activity, this, userId, userToken, stadiumId);
        }

        // show progress if suitable
        if (connectionHandler != null) {
            showProgress();
        }

        cancelWhenDestroyed(connectionHandler);
    }

    private void refresh() {
        loadData();
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        // get data
        Reservation[] reservationsArr = (Reservation[]) response;
        data = new ArrayList<>(Arrays.asList(reservationsArr));

        // check size
        if (data.size() == 0) {
            showEmpty(R.string.no_reservations_found);
        } else {
            updateUI();
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        showError(R.string.failed_loading_reservations);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        SerializableListWrapper dataWrapper = new SerializableListWrapper<>(data);
        outState.putSerializable("dataWrapper", dataWrapper);
        super.onSaveInstanceState(outState);
    }
}
