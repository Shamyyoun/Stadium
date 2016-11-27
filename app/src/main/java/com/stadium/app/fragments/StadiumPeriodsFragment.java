package com.stadium.app.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stadium.app.ApiRequests;
import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.activities.StadiumInfoActivity;
import com.stadium.app.adapters.StadiumPeriodsAdapter;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.interfaces.OnItemRemovedListener;
import com.stadium.app.interfaces.OnReservationAddedListener;
import com.stadium.app.models.SerializableListWrapper;
import com.stadium.app.models.entities.Reservation;
import com.stadium.app.models.entities.Team;
import com.stadium.app.utils.Utils;
import com.stadium.app.utils.ViewUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by karam on 7/31/16.
 */
public class StadiumPeriodsFragment extends ParentFragment implements OnItemRemovedListener, OnReservationAddedListener {
    private ActiveUserController userController;
    private Team selectedTeam; // this is the team object when the user navigates to the add players from team info screen
    private Reservation reservation; // this is just to hold data like stadium, field size and date passed from activity.
    private StadiumInfoActivity activity;
    private TextView tvFieldCapacity;
    private RecyclerView recyclerView;
    private ProgressBar pbProgress;
    private TextView tvEmpty;
    private TextView tvError;
    private StadiumPeriodsAdapter adapter;
    private List<Reservation> data;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (StadiumInfoActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userController = new ActiveUserController(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stadium_periods, container, false);

        // get main objects
        reservation = (Reservation) getArguments().getSerializable(Const.KEY_RESERVATION);
        selectedTeam = (Team) getArguments().getSerializable(Const.KEY_TEAM);

        // init views
        tvFieldCapacity = (TextView) findViewById(R.id.tv_field_capacity);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        pbProgress = (ProgressBar) findViewById(R.id.pb_progress);
        tvEmpty = (TextView) findViewById(R.id.tv_empty);
        tvError = (TextView) findViewById(R.id.tv_error);

        // customize the recycler view
        GridLayoutManager layoutManager = new GridLayoutManager(activity, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

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
                showEmpty();
            }
        } else {
            loadData();
        }

        return rootView;
    }

    private void updateUI() {
        // prepare suitable item layout id
        int itemLayoutId;
        if (userController.isAdmin()) {
            itemLayoutId = R.layout.item_gray_stadium_period;
        } else {
            itemLayoutId = R.layout.item_green_stadium_period;
        }

        // create and set the adapter
        adapter = new StadiumPeriodsAdapter(activity, data, itemLayoutId, reservation);
        adapter.setSelectedTeam(selectedTeam);
        adapter.setOnItemRemovedListener(this);
        adapter.setOnReservationAddedListener(this);
        recyclerView.setAdapter(adapter);
        showMain();
    }

    @Override
    public void onItemRemoved(int position) {
        if (data.size() == 0) {
            showEmpty();
        }
    }

    @Override
    public void onReservationAdded(Reservation reservation) {
        // fire the method in the activity
        activity.onReservationAdded();
    }

    public void loadData() {
        // check internet connection
        if (!Utils.hasConnection(activity)) {
            Utils.showShortToast(activity, R.string.no_internet_connection);
            return;
        }

        showProgress();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.availableReservationSize(activity, this,
                reservation.getReservationStadium().getId(), reservation.getDate(), reservation.getField().getFieldSize());
        cancelWhenDestroyed(connectionHandler);
    }

    public void refresh() {
        loadData();
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        // get data
        Reservation[] reservationsArr = (Reservation[]) response;
        data = new ArrayList<>(Arrays.asList(reservationsArr));

        // check size
        if (data.size() == 0) {
            showEmpty();
        } else {
            updateUI();
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        showError();
    }

    private void showProgress() {
        ViewUtil.showOneView(pbProgress, tvError, recyclerView, tvEmpty);
    }

    private void showEmpty() {
        ViewUtil.showOneView(tvEmpty, pbProgress, tvError, recyclerView);
    }

    private void showError() {
        ViewUtil.showOneView(tvError, tvEmpty, pbProgress, recyclerView);
    }

    private void showMain() {
        ViewUtil.showOneView(recyclerView, tvError, tvEmpty, pbProgress);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        SerializableListWrapper dataWrapper = new SerializableListWrapper<>(data);
        outState.putSerializable("dataWrapper", dataWrapper);
        super.onSaveInstanceState(outState);
    }

    public void updateDate(String date) {
        reservation.setDate(date);
    }
}
