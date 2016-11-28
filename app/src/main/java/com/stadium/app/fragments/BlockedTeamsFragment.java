package com.stadium.app.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stadium.app.ApiRequests;
import com.stadium.app.R;
import com.stadium.app.adapters.BlockedTeamsAdapter;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.interfaces.OnItemRemovedListener;
import com.stadium.app.interfaces.OnRefreshListener;
import com.stadium.app.models.SerializableListWrapper;
import com.stadium.app.models.entities.Team;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shamyyoun on 7/2/16.
 */
public class BlockedTeamsFragment extends ProgressFragment implements OnItemRemovedListener {
    private ActiveUserController userController;
    private RecyclerView recyclerView;
    private List<Team> data;
    private BlockedTeamsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.blocked_teams);
        removeOptionsMenu();

        // create the user controller
        userController = new ActiveUserController(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // customize the recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // get data from saved bundle if exists
        if (savedInstanceState != null) {
            SerializableListWrapper<Team> dataWrapper = (SerializableListWrapper<Team>) savedInstanceState.getSerializable("dataWrapper");
            if (dataWrapper != null) {
                data = dataWrapper.getList();
            }
        }

        // check data
        if (data != null) {
            if (!data.isEmpty()) {
                updateUI();
            } else {
                showEmpty(R.string.no_teams_found);
            }
        } else {
            loadData();
        }

        return rootView;
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.fragment_blocked_teams;
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
        adapter = new BlockedTeamsAdapter(activity, data, R.layout.item_blocked_team);
        adapter.setOnItemRemovedListener(this);
        recyclerView.setAdapter(adapter);
        showMain();
    }

    private void loadData() {
        // check internet connection
        if (!Utils.hasConnection(activity)) {
            showError(R.string.no_internet_connection);
            return;
        }

        showProgress();

        // prepare request params
        User user = userController.getUser();
        int stadiumId = user.getAdminStadium().getId();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.myBlockedList(activity, this, user.getId(),
                user.getToken(), stadiumId);
        cancelWhenDestroyed(connectionHandler);
    }

    private void refresh() {
        loadData();
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        // get data
        Team[] teamsArr = (Team[]) response;
        data = new ArrayList<>(Arrays.asList(teamsArr));

        // check size
        if (data.size() == 0) {
            showEmpty(R.string.no_blocked_teams_found);
        } else {
            updateUI();
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        showError(R.string.failed_loading_teams);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        SerializableListWrapper dataWrapper = new SerializableListWrapper<>(data);
        outState.putSerializable("dataWrapper", dataWrapper);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemRemoved(int position) {
        // check new size
        if (data.size() == 0) {
            showEmpty(R.string.no_blocked_teams_found);
        }
    }
}
