package com.stormnology.stadium.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stormnology.stadium.ApiRequests;
import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.adapters.ReservationsAdapter;
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.models.SerializableListWrapper;
import com.stormnology.stadium.models.entities.Reservation;
import com.stormnology.stadium.models.entities.Team;
import com.stormnology.stadium.models.entities.User;
import com.stormnology.stadium.models.enums.ReservationsType;
import com.stormnology.stadium.utils.Utils;
import com.stormnology.stadium.utils.ViewUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by karam on 7/26/16.
 */
public class TeamReservationsFragment extends ParentFragment implements SwipeRefreshLayout.OnRefreshListener {
    private Team team;
    private SwipeRefreshLayout swipeLayout;
    private View mainView;
    private TextView tvReservationsCount;
    private TextView tvAbsentCount;
    private RecyclerView recyclerView;
    private ProgressBar pbProgress;
    private TextView tvEmpty;
    private TextView tvError;
    private ReservationsAdapter adapter;
    private List<Reservation> data;
    private List<User> teamPlayers; // this is to check if the user is a player in this team

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_team_reservations, container, false);

        // get team
        team = (Team) getArguments().getSerializable(Const.KEY_TEAM);

        // init views
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mainView = findViewById(R.id.main_view);
        tvReservationsCount = (TextView) findViewById(R.id.tv_reservations_count);
        tvAbsentCount = (TextView) findViewById(R.id.tv_absent_counts);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        pbProgress = (ProgressBar) findViewById(R.id.pb_progress);
        tvEmpty = (TextView) findViewById(R.id.tv_empty);
        tvError = (TextView) findViewById(R.id.tv_error);

        // customize swipe layout
        swipeLayout.setOnRefreshListener(this);

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
        // set header data
        tvReservationsCount.setText("" + team.getTotalRes());
        tvAbsentCount.setText("" + team.getAbsentRes());

        // set the adapter
        adapter = new ReservationsAdapter(activity, data, R.layout.item_reservation_simple);
        adapter.setReservationsType(ReservationsType.TEAM_RESERVATIONS);
        adapter.updateTeamPlayers(teamPlayers);
        recyclerView.setAdapter(adapter);
        showMain();
    }

    @Override
    public void onRefresh() {
        refresh();
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
        ViewUtil.showOneView(pbProgress, tvError, mainView, tvEmpty);
        swipeLayout.setRefreshing(false);
        swipeLayout.setEnabled(false);
    }

    private void showEmpty() {
        ViewUtil.showOneView(tvEmpty, pbProgress, tvError, mainView);
        swipeLayout.setEnabled(true);
    }

    private void showError() {
        ViewUtil.showOneView(tvError, tvEmpty, pbProgress, mainView);
        swipeLayout.setEnabled(true);
    }

    private void showMain() {
        ViewUtil.showOneView(mainView, tvError, tvEmpty, pbProgress);
        swipeLayout.setEnabled(true);
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

    /**
     * this method to update the team players in this fragment when loaded from TeamPlayersFragments
     * to check in the adapter if the current user is a player in this team and specify what he can do
     */
    public void updateTeamPlayers(List<User> players) {
        teamPlayers = players;
        if (adapter != null) {
            adapter.updateTeamPlayers(players);
        }
    }
}