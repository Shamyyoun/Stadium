package com.stormnology.stadium.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stormnology.stadium.ApiRequests;
import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.activities.ContactsActivity;
import com.stormnology.stadium.activities.PlayerInfoActivity;
import com.stormnology.stadium.activities.PlayersActivity;
import com.stormnology.stadium.activities.PlayersSearchActivity;
import com.stormnology.stadium.adapters.PlayersAdapter;
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.controllers.OrderController;
import com.stormnology.stadium.dialogs.OrderDialog;
import com.stormnology.stadium.interfaces.OnCheckableSelectedListener;
import com.stormnology.stadium.interfaces.OnItemClickListener;
import com.stormnology.stadium.interfaces.OnPlayerAddedListener;
import com.stormnology.stadium.interfaces.OnRefreshListener;
import com.stormnology.stadium.models.Checkable;
import com.stormnology.stadium.models.SerializableListWrapper;
import com.stormnology.stadium.models.entities.OrderCriteria;
import com.stormnology.stadium.models.entities.PlayersFilter;
import com.stormnology.stadium.models.entities.Team;
import com.stormnology.stadium.models.entities.User;
import com.stormnology.stadium.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shamyyoun on 7/2/16.
 */
public class PlayersFragment extends ProgressFragment implements OnPlayerAddedListener, OnItemClickListener {
    private Team selectedTeam; // this is the team object when the user navigates to the add players from team info screen
    private ActiveUserController activeUserController;
    private OrderController orderController;
    private TextView tvOrderBy;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private ConnectionHandler connectionHandler;
    private List<User> data;
    private PlayersAdapter adapter;
    private int page;

    private PlayersFilter filter;
    private OrderDialog orderDialog;
    private OrderCriteria orderCriteria;
    private int selectedItemPosition; // used to hold clicked player to open his info

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createOptionsMenu(R.menu.menu_players);

        // set suitable title
        String toolbarTitle = null;
        if (getArguments() != null) {
            toolbarTitle = getArguments().getString(Const.KEY_TOOLBAR_TITLE);
        }
        if (toolbarTitle == null) {
            toolbarTitle = getString(R.string.players);
        }
        setTitle(toolbarTitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // get extras
        if (getArguments() != null) {
            selectedTeam = (Team) getArguments().getSerializable(Const.KEY_TEAM);
        }

        // create controllers
        activeUserController = new ActiveUserController(activity);
        orderController = new OrderController();

        // init views
        tvOrderBy = (TextView) findViewById(R.id.tv_order_by);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // customize the recycler view
        layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // check saved instance
        if (savedInstanceState != null) {
            // get saved options
            selectedItemPosition = savedInstanceState.getInt("selectedItemPosition");
            page = savedInstanceState.getInt("page");
            orderCriteria = (OrderCriteria) savedInstanceState.getSerializable("orderCriteria");

            // get saved data if possible
            SerializableListWrapper<User> dataWrapper = (SerializableListWrapper<User>) savedInstanceState.getSerializable("dataWrapper");
            if (dataWrapper != null) {
                data = dataWrapper.getList();
            }
        }

        // create order dialog and set suitable order criteria
        orderDialog = new OrderDialog(activity, orderController.getPlayersCriterias(activity));
        if (orderCriteria == null) {
            // get default default order
            orderCriteria = orderDialog.getDefaultCriteria();
        }
        // then update order ui
        updateOrderByUI();

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
        tvOrderBy.setOnClickListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                loadMoreIfRequired();
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_order_by) {
            showOrderDialog();
        } else {
            super.onClick(v);
        }
    }

    private void updateUI() {
        adapter = new PlayersAdapter(activity, data, R.layout.item_player, PlayersAdapter.TYPE_SHOW_ADDRESS);
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
        selectedItemPosition = position;
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

    private void resetPageNo() {
        page = 0;
    }

    private void loadData() {
        // check internet connection
        if (!Utils.hasConnection(activity)) {
            // check if first load
            if (isFirstLoad()) {
                // show error and disable controls
                showError(R.string.no_internet_connection);
                disableControls();
            } else {
                // decrement the page no.
                page--;
            }

            return;
        }

        // show suitable progress
        if (isFirstLoad()) {
            showProgress();
        } else {
            showProgressFooter();
        }

        // get active user
        User user = activeUserController.getUser();

        // send suitable request
        if (filter == null) {
            // send normal request
            connectionHandler = ApiRequests.allPlayers(activity, this, user.getId(), page, orderCriteria.getType());
        } else {
            // send request with filter
            int cityId = filter.getCity() != null ? filter.getCity().getId() : 0;
            connectionHandler = ApiRequests.allPlayers(activity, this, user.getId(), page, orderCriteria.getType(),
                    cityId, filter.getName(), filter.getPosition(), filter.getPhone());
        }
        cancelWhenDestroyed(connectionHandler);
    }

    private void loadMoreIfRequired() {
        // check if it is still loading some data
        if (ConnectionHandler.isLoading(connectionHandler)) {
            return;
        }

        // check if reached end of the list
        if (Utils.isReachedEndOfRecycler(layoutManager)) {
            // load more
            loadMore();
        }
    }

    private void loadMore() {
        // increment the page no. and load data
        page++;
        loadData();
    }

    private void refresh() {
        // reset page no. and load data
        resetPageNo();
        loadData();
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        // get data
        User[] usersArr = (User[]) response;
        List<User> usersList = new ArrayList<>(Arrays.asList(usersArr));

        // check if first load
        if (isFirstLoad()) {
            // enable controls & set the data
            enableControls();
            data = usersList;

            // check size
            if (usersList.size() == 0) {
                showEmpty(R.string.no_players_found);
            } else {
                updateUI();
            }
        } else {
            // hide progress and add new items
            hideProgressFooter();
            addNewPlayers(usersList);
        }
    }

    private void addNewPlayers(List<User> usersList) {
        // add new players to the data list then notify the adapter
        data.addAll(usersList);
        adapter.notifyItemRangeInserted(data.size() - usersList.size(), data.size());
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        // check if first load
        if (isFirstLoad()) {
            // show error view and disable controls
            showError(R.string.failed_loading_players);
            disableControls();
        } else {
            // show error msg and decrement the page no.
            Utils.showShortToast(activity, R.string.failed_loading_players);
            page--;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        SerializableListWrapper dataWrapper = new SerializableListWrapper<>(data);
        outState.putSerializable("dataWrapper", dataWrapper);
        outState.putInt("selectedItemPosition", selectedItemPosition);
        outState.putInt("page", page);
        outState.putSerializable("orderCriteria", orderCriteria);
        super.onSaveInstanceState(outState);
    }

    private void enableControls() {
        tvOrderBy.setEnabled(true);
    }

    private void disableControls() {
        tvOrderBy.setEnabled(false);
    }

    private boolean isFirstLoad() {
        return (page == 0);
    }

    private void showProgressFooter() {
        adapter.addProgressFooter();
    }

    private void hideProgressFooter() {
        adapter.removeFooterItem();
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
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Const.REQ_SEARCH_PLAYERS) {
                filter = (PlayersFilter) data.getSerializableExtra(Const.KEY_FILTER);
                search();
            } else if (requestCode == Const.REQ_VIEW_CONTACTS) {
                // some players have been added
                // notify players activity
                notifyPlayersActivity();
            } else if (requestCode == Const.REQ_VIEW_PLAYER_INFO) {
                // update the player rating if possible
                double rating = data.getDoubleExtra(Const.KEY_RATING, -1);
                if (rating != -1) {
                    updatePlayerRating(selectedItemPosition, rating);
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void search() {
        // reset page no. and load data
        resetPageNo();
        loadData();
    }

    private void updatePlayerRating(int position, double rating) {
        User player = this.data.get(position);
        player.setRate(rating);
        adapter.notifyDataSetChanged();
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
            }
        });

        // show the dialog
        orderDialog.show();
    }

    private void order() {
        // update order ui
        updateOrderByUI();

        // then load data
        loadData();
    }
}
