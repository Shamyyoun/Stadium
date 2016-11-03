package com.stadium.app.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stadium.app.ApiRequests;
import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.activities.ContactsActivity;
import com.stadium.app.activities.PlayersSearchActivity;
import com.stadium.app.adapters.PlayersAdapter;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.controllers.SearchController;
import com.stadium.app.models.SerializableListWrapper;
import com.stadium.app.models.entities.PlayersFilter;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shamyyoun on 7/2/16.
 */
public class PlayersFragment extends ProgressFragment {
    private SearchController searchController;
    private TextView tvOrderBy;
    private RecyclerView recyclerView;
    private List<User> data;
    private PlayersAdapter adapter;

    private boolean enableControls = true;
    private PlayersFilter filter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.players);
        createOptionsMenu(R.menu.menu_players);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // create controllers
        searchController = new SearchController();

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
                updateUI(data);
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

    private void updateUI(List<User> data) {
        adapter = new PlayersAdapter(activity, data, R.layout.item_player, PlayersAdapter.TYPE_SHOW_ADDRESS);
        recyclerView.setAdapter(adapter);
        showMain();
    }

    private void loadData() {
        // check internet connection
        if (!Utils.hasConnection(activity)) {
            showError(R.string.no_internet_connection);
            disableControls();
            return;
        }

        showProgress();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.allPlayers(activity, this);
        cancelWhenDestroyed(connectionHandler);
    }

    private void refresh() {
        filter = null;
        loadData();
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        enableControls();

        // get data
        User[] usersArr = (User[]) response;
        data = new ArrayList<>(Arrays.asList(usersArr));

        // check size
        if (data.size() == 0) {
            showEmpty(R.string.no_players_found);
        } else {
            updateUI(data);
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        showError(R.string.failed_loading_players);
        disableControls();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        SerializableListWrapper dataWrapper = new SerializableListWrapper<>(data);
        outState.putSerializable("dataWrapper", dataWrapper);
        super.onSaveInstanceState(outState);
    }

    private void enableControls() {
        enableControls = true;
    }

    private void disableControls() {
        enableControls = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_contacts:
                openContactsActivity();
                break;

            case R.id.action_search:
                if (enableControls) openSearchActivity();
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
        intent.putExtra(Const.KEY_FILTER, filter);
        startActivityForResult(intent, Const.REQ_SEARCH_PLAYERS);
        activity.overridePendingTransition(R.anim.top_translate_enter, R.anim.no_anim);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Const.REQ_SEARCH_PLAYERS && resultCode == Activity.RESULT_OK) {
            filter = (PlayersFilter) data.getSerializableExtra(Const.KEY_FILTER);
            search();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void search() {
        new AsyncTask() {
            List<User> playersResult;

            @Override
            protected void onPreExecute() {
                showProgressDialog();
            }

            @Override
            protected Object doInBackground(Object[] params) {
                playersResult = searchController.searchPlayers(data, filter);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                hideProgressDialog();
                updateUI(playersResult);

                // check the results size
                if (playersResult.size() == 0) {
                    showEmpty(R.string.no_search_results);
                }
            }
        }.execute();
    }
}
