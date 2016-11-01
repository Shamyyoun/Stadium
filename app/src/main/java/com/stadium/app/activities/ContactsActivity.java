package com.stadium.app.activities;

import android.Manifest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.stadium.app.ApiRequests;
import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.adapters.PlayersAdapter;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.fragments.ProgressFragment;
import com.stadium.app.models.SerializableListWrapper;
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
public class ContactsActivity extends ProgressActivity {
    private RecyclerView recyclerView;
    private List<User> data;
    private PlayersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBackButton();

        // customize the recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // get data from saved bundle if exists
        if (savedInstanceState != null) {
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
    protected ProgressFragment.OnRefreshListener getOnRefreshListener() {
        return new ProgressFragment.OnRefreshListener() {
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

        // get phone numbers and send request in an async task to avoid the long time to get contacts
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                // get contacts
                String[] phoneNumbers = AppUtils.prepareContactsPhonesArr(activity);

                // send request
                ConnectionHandler connectionHandler = ApiRequests.checkListOfContact(activity, ContactsActivity.this, phoneNumbers);
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
    public void onSaveInstanceState(Bundle outState) {
        SerializableListWrapper dataWrapper = new SerializableListWrapper<>(data);
        outState.putSerializable("dataWrapper", dataWrapper);
        super.onSaveInstanceState(outState);
    }
}
