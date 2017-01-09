package com.stadium.app.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.stadium.app.activities.TeamInfoActivity;
import com.stadium.app.adapters.TeamPlayersAdapter;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.interfaces.OnAssistantChangedListener;
import com.stadium.app.interfaces.OnCaptainChangedListener;
import com.stadium.app.interfaces.OnItemRemovedListener;
import com.stadium.app.models.SerializableListWrapper;
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
public class TeamPlayersFragment extends ParentFragment implements OnItemRemovedListener, OnCaptainChangedListener,
        OnAssistantChangedListener, SwipeRefreshLayout.OnRefreshListener {
    private Team team;
    private TeamInfoActivity activity;
    private SwipeRefreshLayout swipeLayout;
    private RecyclerView recyclerView;
    private ProgressBar pbProgress;
    private TextView tvEmpty;
    private TextView tvError;
    private TeamPlayersAdapter adapter;
    private List<User> data;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (TeamInfoActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_team_players, container, false);

        // get team
        team = (Team) getArguments().getSerializable(Const.KEY_TEAM);

        // init views
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
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
            SerializableListWrapper<User> dataWrapper = (SerializableListWrapper<User>) savedInstanceState.getSerializable("dataWrapper");
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
        adapter = new TeamPlayersAdapter(activity, data, R.layout.item_team_player, team);
        adapter.setOnItemRemovedListener(this);
        adapter.setOnCaptainChangedListener(this);
        adapter.setOnAssistantChangedListener(this);
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

        // send request
        ConnectionHandler connectionHandler = ApiRequests.teamPlayers(activity, this, team.getId());
        cancelWhenDestroyed(connectionHandler);
    }

    public void refresh() {
        loadData();
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        // get data
        User[] playersArr = (User[]) response;
        data = new ArrayList<>(Arrays.asList(playersArr));

        // check size
        if (data.size() == 0) {
            showEmpty();
        } else {
            updateUI();
        }

        // update the team info activity
        activity.updatePlayers(data);
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        showError();
    }

    private void showProgress() {
        ViewUtil.showOneView(pbProgress, tvError, recyclerView, tvEmpty);
        swipeLayout.setRefreshing(false);
        swipeLayout.setEnabled(false);
    }

    private void showEmpty() {
        ViewUtil.showOneView(tvEmpty, pbProgress, tvError, recyclerView);
        swipeLayout.setEnabled(true);
    }

    private void showError() {
        ViewUtil.showOneView(tvError, tvEmpty, pbProgress, recyclerView);
        swipeLayout.setEnabled(true);
    }

    private void showMain() {
        ViewUtil.showOneView(recyclerView, tvError, tvEmpty, pbProgress);
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

    @Override
    public void onCaptainChanged(User newCaptain) {
        activity.onCaptainChanged(newCaptain);
    }

    @Override
    public void onAssistantChanged(User newAssistant) {
        activity.onAssistantChanged(newAssistant);
    }
}
