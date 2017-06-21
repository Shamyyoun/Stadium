package com.stormnology.stadium.fragments;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stormnology.stadium.ApiRequests;
import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.activities.TeamInfoActivity;
import com.stormnology.stadium.adapters.TeamInvitationsAdapter;
import com.stormnology.stadium.adapters.TeamPlayersAdapter;
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.interfaces.OnAssistantChangedListener;
import com.stormnology.stadium.interfaces.OnCaptainChangedListener;
import com.stormnology.stadium.interfaces.OnItemRemovedListener;
import com.stormnology.stadium.models.SerializableListWrapper;
import com.stormnology.stadium.models.entities.Team;
import com.stormnology.stadium.models.entities.User;
import com.stormnology.stadium.utils.Utils;
import com.stormnology.stadium.utils.ViewUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by karam on 7/26/16.
 */
public class TeamPlayersFragment extends ParentFragment implements OnItemRemovedListener, OnCaptainChangedListener,
        OnAssistantChangedListener, SwipeRefreshLayout.OnRefreshListener {
    private Team team;
    private ActiveUserController activeUserController;
    private TeamInfoActivity activity;
    private SwipeRefreshLayout swipeLayout;
    private LinearLayout layoutMain;
    private ProgressBar pbProgress;
    private TextView tvEmpty;
    private TextView tvError;
    private RecyclerView rvPlayers;
    private View layoutInvitations;
    private RecyclerView rvInvitations;
    private TeamPlayersAdapter playersAdapter;
    private List<User> players;
    private TeamInvitationsAdapter invitationsAdapter;
    private List<User> invitations;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (TeamInfoActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_team_players, container, false);

        // obtain main objects
        team = (Team) getArguments().getSerializable(Const.KEY_TEAM);
        activeUserController = new ActiveUserController(activity);

        // init views
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        layoutMain = (LinearLayout) findViewById(R.id.layout_main);
        pbProgress = (ProgressBar) findViewById(R.id.pb_progress);
        tvEmpty = (TextView) findViewById(R.id.tv_empty);
        tvError = (TextView) findViewById(R.id.tv_error);
        rvPlayers = (RecyclerView) findViewById(R.id.rv_players);
        layoutInvitations = findViewById(R.id.layout_invitations);
        rvInvitations = (RecyclerView) findViewById(R.id.rv_invitations);

        // customize swipe layout
        swipeLayout.setOnRefreshListener(this);

        // customize the recycler views
        LinearLayoutManager playersLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rvPlayers.setLayoutManager(playersLayoutManager);
        LinearLayoutManager invitationsLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rvInvitations.setLayoutManager(invitationsLayoutManager);

        // get data from saved bundle if exists
        if (savedInstanceState != null) {
            // get players if possible
            SerializableListWrapper<User> playersWrapper = (SerializableListWrapper<User>) savedInstanceState.getSerializable("playersWrapper");
            if (playersWrapper != null) {
                players = playersWrapper.getList();
            }

            // get invitations if possible
            SerializableListWrapper<User> invitationsWrapper = (SerializableListWrapper<User>) savedInstanceState.getSerializable("invitationsWrapper");
            if (invitationsWrapper != null) {
                invitations = invitationsWrapper.getList();
            }
        }

        // check players
        if (players != null) {
            updatePlayersUI();

            // then check invitations
            if (invitations != null) {
                updateInvitationsUI();
            }
        }

        return rootView;
    }

    private void updatePlayersUI() {
        // set the adapter
        playersAdapter = new TeamPlayersAdapter(activity, players, R.layout.item_team_player, team);
        playersAdapter.setOnItemRemovedListener(this);
        playersAdapter.setOnCaptainChangedListener(this);
        playersAdapter.setOnAssistantChangedListener(this);
        rvPlayers.setAdapter(playersAdapter);
        showPlayersMain();
    }

    private void updateInvitationsUI() {
        // set the adapter
        invitationsAdapter = new TeamInvitationsAdapter(activity, invitations, R.layout.item_team_invitation, team);
        invitationsAdapter.setOnItemRemovedListener(this);
        rvInvitations.setAdapter(invitationsAdapter);
        showInvitationsMain();
    }

    @Override
    public void onItemRemoved(int position) {
        // check players size
        if (players != null && players.size() == 0) {
            hidePlayersMainView();
        }

        // check invitations size
        if (invitations != null && invitations.size() == 0) {
            hideInvitationsView();
        }

        // check both sizes
        if ((players == null || players.size() == 0)
                && (invitations == null || invitations.size() == 0)) {
            // show empty view
            showEmpty();
        }
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    public void loadPlayers() {
        // check internet connection
        if (!Utils.hasConnection(activity)) {
            swipeLayout.setRefreshing(false);
            Utils.showShortToast(activity, R.string.no_internet_connection);
            return;
        }

        showProgress();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.teamPlayers(activity, this, team.getId());
        cancelWhenDestroyed(connectionHandler);
    }

    public void loadInvitations() {
        // check players list size
        if (players.size() == 0) {
            // players list is empty
            // so, show the progress
            showProgress();
        } // otherwise, don't show anything

        // get active user
        User user = activeUserController.getUser();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.teamInvitations(activity, this, user.getId(),
                user.getToken(), team.getId());
        cancelWhenDestroyed(connectionHandler);
    }

    public void refresh() {
        // load players first then it will load invitations when players loaded successfully
        loadPlayers();
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        // check tag to handle the response
        if (Const.API_TEAM_PLAYERS.equals(tag)) {
            handlePlayersResponse(response);
        } else {
            handleInvitationsResponse(response);
        }
    }

    private void handlePlayersResponse(Object response) {
        // get data
        User[] playersArr = (User[]) response;
        players = new ArrayList<>(Arrays.asList(playersArr));

        // check size
        if (players.size() > 0) {
            // update players UI
            updatePlayersUI();
        }

        // then load invitations
        loadInvitations();

        // update the team info activity
        activity.updatePlayers(players);
    }

    private void handleInvitationsResponse(Object response) {
        // get data
        User[] invitationsArr = (User[]) response;
        invitations = new ArrayList<>(Arrays.asList(invitationsArr));

        // check invitations size
        if (invitations.size() == 0) {
            // check players size
            if (players.size() == 0) {
                // show the empty view
                showEmpty();
            } else {
                // otherwise, hide invitations view
                hideInvitationsView();
            }
        } else {
            // update invitations
            updateInvitationsUI();
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        // check tag
        if (Const.API_TEAM_PLAYERS.equals(tag)) {
            showError();
        } else {
            // check players
            if (players.size() == 0) {
                // players list is empty and failed loading team invitations
                // so, just show the empty view
                showEmpty();
            } // otherwise don't show anything
        }
    }

    private void showProgress() {
        // remove layout changes
        layoutMain.setLayoutTransition(null);
        ViewUtil.showOneView(pbProgress, tvError, rvPlayers, tvEmpty, layoutInvitations);
        swipeLayout.setRefreshing(false);
        swipeLayout.setEnabled(false);
    }

    private void showEmpty() {
        ViewUtil.showOneView(tvEmpty, pbProgress, tvError, rvPlayers, layoutInvitations);
        swipeLayout.setEnabled(true);
    }

    private void showError() {
        ViewUtil.showOneView(tvError, tvEmpty, pbProgress, rvPlayers, layoutInvitations);
        swipeLayout.setEnabled(true);
    }

    private void showPlayersMain() {
        ViewUtil.showOneView(rvPlayers, tvError, tvEmpty, pbProgress);
        swipeLayout.setEnabled(true);
    }

    private void showInvitationsMain() {
        // add layout changes animation
        layoutMain.setLayoutTransition(new LayoutTransition());
        ViewUtil.showOneView(layoutInvitations, tvError, tvEmpty, pbProgress);
        swipeLayout.setEnabled(true);
    }

    private void hidePlayersMainView() {
        rvPlayers.setVisibility(View.GONE);
        swipeLayout.setEnabled(true);
    }

    private void hideInvitationsView() {
        layoutInvitations.setVisibility(View.GONE);
        swipeLayout.setEnabled(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        SerializableListWrapper playersWrapper = new SerializableListWrapper<>(players);
        SerializableListWrapper invitationsWrapper = new SerializableListWrapper<>(invitations);
        outState.putSerializable("playersWrapper", playersWrapper);
        outState.putSerializable("invitationsWrapper", invitationsWrapper);
        super.onSaveInstanceState(outState);
    }

    public void updateTeam(Team team) {
        this.team = team;
    }

    @Override
    public void onCaptainChanged(User newCaptain) {
        // update the activity and the team
        activity.onCaptainChanged(newCaptain);
        team.setCaptain(newCaptain);

        // update the invitations adapter if possible
        if (invitationsAdapter != null) {
            invitationsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAssistantChanged(User newAssistant) {
        // update the activity and the team
        activity.onAssistantChanged(newAssistant);
        team.setAsstent(newAssistant);

        // update the invitations adapter if possible
        if (invitationsAdapter != null) {
            invitationsAdapter.notifyDataSetChanged();
        }
    }
}
