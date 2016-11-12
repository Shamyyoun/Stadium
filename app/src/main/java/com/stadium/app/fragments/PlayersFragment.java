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
import com.stadium.app.activities.PlayersActivity;
import com.stadium.app.activities.PlayersSearchActivity;
import com.stadium.app.adapters.PlayersAdapter;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.controllers.OrderController;
import com.stadium.app.controllers.SearchController;
import com.stadium.app.dialogs.OrderDialog;
import com.stadium.app.interfaces.OnCheckableSelectedListener;
import com.stadium.app.interfaces.OnPlayerAddedListener;
import com.stadium.app.models.Checkable;
import com.stadium.app.models.SerializableListWrapper;
import com.stadium.app.models.entities.OrderCriteria;
import com.stadium.app.models.entities.PlayersFilter;
import com.stadium.app.models.entities.Team;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shamyyoun on 7/2/16.
 */
public class PlayersFragment extends ProgressFragment implements OnPlayerAddedListener {
    private Team selectedTeam; // this is the team object when the user navigates to the add players from team info screen
    private SearchController searchController;
    private OrderController orderController;
    private TextView tvOrderBy;
    private RecyclerView recyclerView;
    private List<User> data;
    private PlayersAdapter adapter;

    private List<User> searchResults;
    private boolean enableControls = true;
    private PlayersFilter filter;
    private OrderDialog orderDialog;
    private OrderCriteria orderCriteria;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.players);
        createOptionsMenu(R.menu.menu_players);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // get extras
        if (getArguments() != null) {
            selectedTeam = (Team) getArguments().getSerializable(Const.KEY_TEAM);
        }

        // create controllers
        searchController = new SearchController();
        orderController = new OrderController();

        // init views
        tvOrderBy = (TextView) findViewById(R.id.tv_order_by);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // customize the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // set default order as the data is returned ordered from the server
        orderDialog = new OrderDialog(activity, orderController.getPlayersCriterias(activity));
        orderCriteria = orderDialog.getDefaultCriteria();
        updateOrderByUI();

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
                showOrderDialog();
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
        adapter.setSelectedTeam(selectedTeam);
        adapter.setOnPlayerAddedListener(this);
        recyclerView.setAdapter(adapter);
        showMain();
    }

    @Override
    public void onPlayerAdded(User player) {
        notifyPlayersActivity();
    }

    private void notifyPlayersActivity() {
        if (getActivity() instanceof PlayersActivity) {
            PlayersActivity activity = (PlayersActivity) getActivity();
            activity.onPlayerAdded();
        }
    }

    private void updateOrderByUI() {
        String orderStr = getString(R.string.the_order_by) + " " + orderCriteria.getName();
        tvOrderBy.setText(orderStr);
    }

    private void resetFilters() {
        filter = null;
        searchResults = null;
        orderCriteria = orderDialog.getDefaultCriteria();
        updateOrderByUI();
    }

    private void loadData() {
        // check internet connection
        if (!Utils.hasConnection(activity)) {
            showError(R.string.no_internet_connection);
            disableControls();
            return;
        }

        // show progress and reset filters
        showProgress();
        resetFilters();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.allPlayers(activity, this);
        cancelWhenDestroyed(connectionHandler);
    }

    private void refresh() {
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
        tvOrderBy.setEnabled(true);
        enableControls = true;
    }

    private void disableControls() {
        tvOrderBy.setEnabled(false);
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
        intent.putExtra(Const.KEY_TEAM, selectedTeam);
        startActivityForResult(intent, Const.REQ_VIEW_CONTACTS);
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
        } else if (requestCode == Const.REQ_VIEW_CONTACTS && resultCode == Activity.RESULT_OK) {
            // some players have been added
            // notify players activity
            notifyPlayersActivity();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void search() {
        new AsyncTask() {
            @Override
            protected void onPreExecute() {
                showProgressDialog();
            }

            @Override
            protected Object doInBackground(Object[] params) {
                searchResults = searchController.searchPlayers(data, filter);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                hideProgressDialog();
                updateUI(searchResults);

                // check the results size
                if (searchResults.size() == 0) {
                    // results is empty, show empty view
                    showEmpty(R.string.no_search_results);
                } else if (orderCriteria != null) { // check if an order exists
                    // an order exits, order data
                    order();
                }
            }
        }.execute();
    }

    private void showOrderDialog() {
        // set the order type
        orderDialog.setSelectedItemType(orderCriteria.getType());

        // set listener
        orderDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
            @Override
            public void onCheckableSelected(Checkable item) {
                orderCriteria = (OrderCriteria) item;
                order();
                Utils.showShortToast(activity, R.string.ordered);
            }
        });

        // show the dialog
        orderDialog.show();
    }

    private void order() {
        // check if has a visible search
        if (searchResults != null) {
            // order search results
            orderController.orderPlayers(searchResults, orderCriteria.getType());
        } else {
            // order original data
            orderController.orderPlayers(data, orderCriteria.getType());
        }

        // update the ui
        adapter.notifyDataSetChanged();
        updateOrderByUI();
    }
}
