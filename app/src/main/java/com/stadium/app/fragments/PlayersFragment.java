package com.stadium.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stadium.app.ApiRequests;
import com.stadium.app.R;
import com.stadium.app.activities.ContactsActivity;
import com.stadium.app.activities.PlayersSearchActivity;
import com.stadium.app.adapters.PlayersAdapter;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.models.SerializableListWrapper;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shamyyoun on 7/2/16.
 */
public class PlayersFragment extends ProgressFragment {
    private TextView tvOrderBy;
    private RecyclerView recyclerView;
    private List<User> data;
    private PlayersAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.players);
        createOptionsMenu(R.menu.menu_players);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // init views
        tvOrderBy = (TextView) findViewById(R.id.tv_order_by);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

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
            if (!data.isEmpty()) {
                updateUI();
            } else {
                showEmpty(R.string.no_players_found);
            }
        } else {
            loadData();
        }

        // add listeners
        tvOrderBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });

        return rootView;
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.fragment_players;
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
        adapter = new PlayersAdapter(activity, data, R.layout.item_player, PlayersAdapter.TYPE_SHOW_ADDRESS);
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

        // send request
        ConnectionHandler connectionHandler = ApiRequests.allPlayers(activity, this);
        cancelWhenDestroyed(connectionHandler);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_contacts:
                openContactsActivity();
                break;

            case R.id.action_search:
                openSearchActivity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openContactsActivity() {
        Intent intent = new Intent(activity, ContactsActivity.class);
        startActivity(intent);
    }

    private void openSearchActivity() {
        Intent intent = new Intent(activity, PlayersSearchActivity.class);
        startActivity(intent);
        activity.overridePendingTransition(R.anim.top_translate_enter, R.anim.no_anim);
    }
}
