package com.stormnology.stadium.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stormnology.stadium.ApiRequests;
import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.adapters.ReservationsAdapter;
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.interfaces.OnItemRemovedListener;
import com.stormnology.stadium.interfaces.OnRefreshListener;
import com.stormnology.stadium.models.SerializableListWrapper;
import com.stormnology.stadium.models.entities.Reservation;
import com.stormnology.stadium.models.entities.User;
import com.stormnology.stadium.models.enums.ReservationsType;
import com.stormnology.stadium.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shamyyoun on 7/2/16.
 */
public class AdminReservationsFragment extends ProgressFragment implements OnItemRemovedListener {
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
        return R.id.recycler_view;
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
        // prepare item layout id
        int itemLayoutId;
        if (reservationsType == ReservationsType.ADMIN_NEW_RESERVATIONS
                || reservationsType == ReservationsType.ADMIN_PREVIOUS_RESERVATIONS
                || reservationsType == ReservationsType.ADMIN_MY_RESERVATIONS) {
            itemLayoutId = R.layout.item_reservation_detailed;
        } else {
            itemLayoutId = R.layout.item_reservation_simple;
        }

        // create and set the adapter
        adapter = new ReservationsAdapter(activity, data, itemLayoutId);
        adapter.setReservationsType(reservationsType);
        adapter.setOnItemRemovedListener(this);
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

    @Override
    public void onItemRemoved(int position) {
        // check data size
        if (data.size() == 0) {
            showEmpty(R.string.no_reservations_found);
        }
    }
}
