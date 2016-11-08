package com.stadium.app.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stadium.app.ApiRequests;
import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.adapters.ReservationsAdapter;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.models.SerializableListWrapper;
import com.stadium.app.models.entities.Reservation;
import com.stadium.app.models.entities.Team;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.Utils;
import com.stadium.app.utils.ViewUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by karam on 7/26/16.
 */
public class TeamReservationsFragment extends ParentFragment {
    private Team team;
    private RecyclerView recyclerView;
    private ProgressBar pbProgress;
    private TextView tvEmpty;
    private TextView tvError;
    private ReservationsAdapter adapter;
    private List<Reservation> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_team_reservations, container, false);

        // get team
        team = (Team) getArguments().getSerializable(Const.KEY_TEAM);

        // init views
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        pbProgress = (ProgressBar) findViewById(R.id.pb_progress);
        tvEmpty = (TextView) findViewById(R.id.tv_empty);
        tvError = (TextView) findViewById(R.id.tv_error);

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
            updateUI();
        }

        return rootView;
    }

    private void updateUI() {
        // set the adapter
        adapter = new ReservationsAdapter(activity, data, R.layout.item_reservation);
        adapter.setIsTeamReservations(true);
        recyclerView.setAdapter(adapter);
        showMain();
    }

    public void loadData() {
        // check internet connection
        if (!Utils.hasConnection(activity)) {
            Utils.showShortToast(activity, R.string.no_internet_connection);
            return;
        }

        showProgress();

        // get active user
        ActiveUserController userController = new ActiveUserController(activity);
        User user = userController.getUser();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.reservationsOfTeam(activity, this, user.getId(),
                user.getToken(), team.getId());
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

    public void updateTeam(Team team) {
        this.team = team;
    }
}
