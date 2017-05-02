package com.stormnology.stadium.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.stormnology.stadium.ApiRequests;
import com.stormnology.stadium.R;
import com.stormnology.stadium.adapters.InvitationsAdapter;
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.interfaces.OnInvitationAcceptedListener;
import com.stormnology.stadium.interfaces.OnItemRemovedListener;
import com.stormnology.stadium.interfaces.OnRefreshListener;
import com.stormnology.stadium.models.SerializableListWrapper;
import com.stormnology.stadium.models.entities.Team;
import com.stormnology.stadium.models.entities.User;
import com.stormnology.stadium.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shamyyoun on 5/1/17.
 */
public class InvitationsActivity extends ProgressActivity implements OnInvitationAcceptedListener, OnItemRemovedListener {
    private ActiveUserController activeUserController;
    private RecyclerView recyclerView;
    private List<Team> data;
    private InvitationsAdapter adapter;
    private boolean areInvitationsAccepted; // used to set the result when leaving the activity to notify parents if players added

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBackButton();

        // obtain main objects
        activeUserController = new ActiveUserController(this);

        // customize the recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // obtain saved data if possible
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
                showEmpty(R.string.no_invitations_found);
            }
        } else {
            loadData();
        }
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_invitations;
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
        adapter = new InvitationsAdapter(activity, data, R.layout.item_invitation);
        adapter.setOnItemRemovedListener(this);
        adapter.setOnInvitationAcceptedListener(this);
        recyclerView.setAdapter(adapter);
        showMain();
    }

    @Override
    public void onItemRemoved(int position) {
        // show empty view if required
        if (data.size() == 0) {
            showEmpty(R.string.no_invitations_found);
        }
    }

    @Override
    public void onInvitationAccepted(Team team) {
        areInvitationsAccepted = true;
    }

    private void loadData() {
        // check internet connection
        if (!Utils.hasConnection(activity)) {
            showError(R.string.no_internet_connection);
            return;
        }

        showProgress();

        // get active user
        User user = activeUserController.getUser();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.myInvitations(activity, this, user.getId(), user.getToken());
        cancelWhenDestroyed(connectionHandler);
    }

    private void refresh() {
        loadData();
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        // get data
        Team[] invitationsArr = (Team[]) response;
        data = new ArrayList<>(Arrays.asList(invitationsArr));

        // check size
        if (data.size() == 0) {
            showEmpty(R.string.no_invitations_found);
        } else {
            updateUI();
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        showError(R.string.failed_loading_invitations);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        SerializableListWrapper dataWrapper = new SerializableListWrapper<>(data);
        outState.putSerializable("dataWrapper", dataWrapper);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (areInvitationsAccepted) {
            setResult(RESULT_OK);
        }

        finish();
    }
}
