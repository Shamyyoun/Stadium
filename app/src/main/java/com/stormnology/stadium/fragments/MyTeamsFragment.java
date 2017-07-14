package com.stormnology.stadium.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.stormnology.stadium.ApiRequests;
import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.activities.CreateTeamActivity;
import com.stormnology.stadium.activities.InvitationsActivity;
import com.stormnology.stadium.activities.PlayersActivity;
import com.stormnology.stadium.activities.TeamInfoActivity;
import com.stormnology.stadium.adapters.TeamsAdapter;
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.interfaces.OnItemClickListener;
import com.stormnology.stadium.interfaces.OnRefreshListener;
import com.stormnology.stadium.models.SerializableListWrapper;
import com.stormnology.stadium.models.entities.Team;
import com.stormnology.stadium.models.entities.User;
import com.stormnology.stadium.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shamyyoun on 7/2/16.
 */
public class MyTeamsFragment extends ProgressFragment {
    private ActiveUserController activeUserController;
    private Button btnCreateTeam;
    private RecyclerView recyclerView;
    private List<Team> data;
    private TeamsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createOptionsMenu(R.menu.menu_my_teams);
        setTitle(R.string.my_teams);

        // create the user controller
        activeUserController = new ActiveUserController(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // init views
        btnCreateTeam = (Button) findViewById(R.id.btn_create_team);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // customize the recycler view
        GridLayoutManager layoutManager = new GridLayoutManager(activity, 3);
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

        // add listeners
        btnCreateTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open create team activity
                Intent intent = new Intent(activity, CreateTeamActivity.class);
                startActivityForResult(intent, Const.REQ_CREATE_TEAM);
            }
        });

        return rootView;
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.fragment_my_teams;
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
        adapter = new TeamsAdapter(activity, data, R.layout.item_team_simple);
        adapter.setPlayerId(activeUserController.getUser().getId());
        recyclerView.setAdapter(adapter);
        showMain();

        // add item click listener
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // open team info activity
                Team team = data.get(position);
                Intent intent = new Intent(activity, TeamInfoActivity.class);
                intent.putExtra(Const.KEY_ID, team.getId());
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        // check internet connection
        if (!Utils.hasConnection(activity)) {
            showError(R.string.no_internet_connection);
            return;
        }

        showProgress();

        // get current user
        User user = activeUserController.getUser();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.listOfMyTeams(activity, this, user.getToken(), user.getId());
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
            showEmpty(R.string.no_teams_found);
        } else {
            updateUI();
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        showError(R.string.failed_loading_teams);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // check item id
        switch (item.getItemId()) {
            case R.id.action_invitations:
                openInvitationsActivity();
                break;

            case R.id.action_players:
                openPlayersActivity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openInvitationsActivity() {
        Intent intent = new Intent(activity, InvitationsActivity.class);
        startActivityForResult(intent, Const.REQ_VIEW_INVITATIONS);
    }

    private void openPlayersActivity() {
        Intent intent = new Intent(activity, PlayersActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        SerializableListWrapper dataWrapper = new SerializableListWrapper<>(data);
        outState.putSerializable("dataWrapper", dataWrapper);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK
                && (requestCode == Const.REQ_CREATE_TEAM || requestCode == Const.REQ_VIEW_INVITATIONS)) {
            // refresh my teams
            refresh();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
