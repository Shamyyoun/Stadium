package com.stadium.app.activities;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.stadium.app.ApiRequests;
import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.adapters.PlayersAdapter;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.interfaces.OnItemClickListener;
import com.stadium.app.interfaces.OnPlayerAddedListener;
import com.stadium.app.interfaces.OnRefreshListener;
import com.stadium.app.models.SerializableListWrapper;
import com.stadium.app.models.entities.Team;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.AppUtils;
import com.stadium.app.utils.PermissionUtil;
import com.stadium.app.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by karam on 7/17/16.
 */
public class ContactsActivity extends ProgressActivity implements OnPlayerAddedListener, OnItemClickListener {
    private Team selectedTeam; // this is the team object when the user navigates to the add players from team info screen
    private ActiveUserController userController;
    private RecyclerView recyclerView;
    private List<User> data;
    private PlayersAdapter adapter;
    private boolean isPlayersAdded; // used to set the result when leaving the activity to notify parents if players added
    private int selectedItemPosition; // used to hold clicked player to open his info

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBackButton();

        // obtain main objects
        selectedTeam = (Team) getIntent().getSerializableExtra(Const.KEY_TEAM);
        userController = new ActiveUserController(this);

        // customize the recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // obtain saved data if possible
        if (savedInstanceState != null) {
            selectedItemPosition = savedInstanceState.getInt("selectedItemPosition");
            SerializableListWrapper<User> dataWrapper = (SerializableListWrapper<User>) savedInstanceState.getSerializable("dataWrapper");
            if (dataWrapper != null) {
                data = dataWrapper.getList();
            }
        }

        // check data then contacts permission if required
        boolean permGranted = PermissionUtil.isGranted(this, Manifest.permission.READ_CONTACTS);
        if (data != null) {
            if (!data.isEmpty()) {
                updateUI();
            } else {
                showEmpty(R.string.no_players_found);
            }
        } else if (permGranted) {
            loadData();
        } else {
            // request contacts permission from user
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
                    Const.PERM_REQ_CONTACTS);
        }
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_contacts;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Const.PERM_REQ_CONTACTS) {
            // check result
            if (PermissionUtil.isAllGranted(grantResults)) {
                loadData();
            } else {
                showEmpty(R.string.permission_declined);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void updateUI() {
        adapter = new PlayersAdapter(activity, data, R.layout.item_player, PlayersAdapter.TYPE_SHOW_PHONE_NUMBER);
        adapter.setSelectedTeam(selectedTeam);
        adapter.setOnItemClickListener(this);
        adapter.setOnPlayerAddedListener(this);
        recyclerView.setAdapter(adapter);
        showMain();
    }

    @Override
    public void onItemClick(View view, int position) {
        // open player info activity
        User user = data.get(position);
        Intent intent = new Intent(activity, PlayerInfoActivity.class);
        intent.putExtra(Const.KEY_ID, user.getId());
        startActivityForResult(intent, Const.REQ_VIEW_PLAYER_INFO);
    }

    @Override
    public void onPlayerAdded(User player) {
        isPlayersAdded = true;
    }

    private void loadData() {
        // check internet connection
        if (!Utils.hasConnection(activity)) {
            showError(R.string.no_internet_connection);
            return;
        }

        showProgress();

        // get phone numbers and send request in an async task to avoid the long time to get contacts
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                // get contacts
                List<String> phoneNumbers = AppUtils.prepareContactsPhonesList(activity);

                // get active user
                User user = userController.getUser();

                // send request
                ConnectionHandler connectionHandler = ApiRequests.checkListOfContact(activity, ContactsActivity.this,
                        phoneNumbers, user.getId());
                cancelWhenDestroyed(connectionHandler);
                return null;
            }
        }.execute();
    }

    private void refresh() {
        loadData();
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        // get data
        User[] usersArr = (User[]) response;
        data = new ArrayList<>(Arrays.asList(usersArr));

        // check size
        if (data.size() == 0) {
            showEmpty(R.string.no_players_found);
        } else {
            updateUI();
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        showError(R.string.failed_loading_players);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Const.REQ_VIEW_PLAYER_INFO && resultCode == RESULT_OK) {
            // update the player rating if possible
            double rating = data.getDoubleExtra(Const.KEY_RATING, -1);
            if (rating != -1) {
                updatePlayerRating(selectedItemPosition, rating);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void updatePlayerRating(int position, double rating) {
        User player = this.data.get(position);
        player.setRate(rating);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        SerializableListWrapper dataWrapper = new SerializableListWrapper<>(data);
        outState.putSerializable("dataWrapper", dataWrapper);
        outState.putInt("selectedItemPosition", selectedItemPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (isPlayersAdded) {
            setResult(RESULT_OK);
        }

        finish();
    }
}
