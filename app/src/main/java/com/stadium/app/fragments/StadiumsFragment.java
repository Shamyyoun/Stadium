package com.stadium.app.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
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
import com.stadium.app.activities.StadiumInfoActivity;
import com.stadium.app.activities.StadiumsActivity;
import com.stadium.app.activities.StadiumsSearchActivity;
import com.stadium.app.adapters.StadiumsAdapter;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.controllers.OrderController;
import com.stadium.app.controllers.StadiumsFilterController;
import com.stadium.app.dialogs.OrderDialog;
import com.stadium.app.interfaces.OnCheckableSelectedListener;
import com.stadium.app.interfaces.OnItemClickListener;
import com.stadium.app.interfaces.OnRefreshListener;
import com.stadium.app.models.Checkable;
import com.stadium.app.models.SerializableListWrapper;
import com.stadium.app.models.entities.OrderCriteria;
import com.stadium.app.models.entities.Stadium;
import com.stadium.app.models.entities.StadiumsFilter;
import com.stadium.app.models.entities.Team;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.LocationUtils;
import com.stadium.app.utils.PermissionUtil;
import com.stadium.app.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shamyyoun on 7/2/16.
 */
public class StadiumsFragment extends ProgressFragment implements OnItemClickListener {
    private Team selectedTeam; // this is the team object when the user navigates to the add players from team info screen
    private OrderController orderController;
    private StadiumsFilterController filterController;
    private TextView tvOrderBy;
    private RecyclerView recyclerView;
    private List<Stadium> data;
    private StadiumsAdapter adapter;

    private StadiumsFilter filter;
    private OrderDialog orderDialog;
    private OrderCriteria orderCriteria;
    private boolean pendingOrder;
    private Location location;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createOptionsMenu(R.menu.menu_stadiums);

        // set suitable title
        String toolbarTitle = null;
        if (getArguments() != null) {
            toolbarTitle = getArguments().getString(Const.KEY_TOOLBAR_TITLE);
        }
        if (toolbarTitle == null) {
            toolbarTitle = getString(R.string.stadiums);
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
        orderController = new OrderController();
        filterController = new StadiumsFilterController();

        // init views
        tvOrderBy = (TextView) findViewById(R.id.tv_order_by);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // customize the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // set default order as the data is returned ordered from the server
        orderDialog = new OrderDialog(activity, orderController.getStadiumsCriterias(activity));
        orderCriteria = orderDialog.getDefaultCriteria();
        updateOrderByUI();

        // get data from saved bundle if exists
        if (savedInstanceState != null) {
            SerializableListWrapper<Stadium> dataWrapper = (SerializableListWrapper<Stadium>) savedInstanceState.getSerializable("dataWrapper");
            if (dataWrapper != null) {
                data = dataWrapper.getList();
            }
        }

        // check data
        if (data != null) {
            if (!data.isEmpty()) {
                updateUI();
            } else {
                showEmpty(R.string.no_stadiums_found);
            }
        } else {
            loadData(true);
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
        return R.layout.fragment_stadiums;
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
        adapter = new StadiumsAdapter(activity, data, R.layout.item_stadium);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        showMain();
    }

    @Override
    public void onItemClick(View view, int position) {
        // open stadium info activity
        Stadium stadium = data.get(position);
        Intent intent = new Intent(activity, StadiumInfoActivity.class);
        intent.putExtra(Const.KEY_ID, stadium.getId());
        intent.putExtra(Const.KEY_TEAM, selectedTeam);
        startActivityForResult(intent, Const.REQ_VIEW_STADIUM_INFO);
    }

    private void updateOrderByUI() {
        String orderStr = getString(R.string.the_order_by) + " " + orderCriteria.getName();
        tvOrderBy.setText(orderStr);
    }

    private void resetFilters() {
        orderCriteria = orderDialog.getDefaultCriteria();
        location = null;
        pendingOrder = false;
        filter = null;
    }

    private void loadData(boolean resetFilters) {
        // check internet connection
        if (!Utils.hasConnection(activity)) {
            showError(R.string.no_internet_connection);
            return;
        }

        // show progress and reset filters if required
        showProgress();
        if (resetFilters) resetFilters();

        // get current user
        ActiveUserController userController = new ActiveUserController(activity);
        User user = userController.getUser();

        // send suitable request
        ConnectionHandler connectionHandler;
        if (filter != null && filterController.hasFilters(filter)) {
            // prepare city id
            int cityId;
            if (filter.getCity() != null) {
                cityId = filter.getCity().getId();
            } else {
                cityId = 0;
            }

            // send the request
            connectionHandler = ApiRequests.stadiumsFilters(activity, this, cityId, filter.getName(),
                    filter.getFieldCapacity(), filter.getDate(), filter.getTimeStart(), filter.getTimeEnd());
        } else if (pendingOrder && location != null) {
            connectionHandler = ApiRequests.listStadiumsAround(activity, this, location.getLatitude(),
                    location.getLongitude());
        } else {
            connectionHandler = ApiRequests.listOfStadiums(activity, this, user.getId(), user.getToken());
        }

        // cancel when destroyed
        cancelWhenDestroyed(connectionHandler);
    }

    private void refresh() {
        loadData(true);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        // get data
        Stadium[] stadiumsArr = (Stadium[]) response;
        data = new ArrayList<>(Arrays.asList(stadiumsArr));

        // check size
        if (data.size() == 0) {
            showEmpty(R.string.no_stadiums_found);
        } else {
            updateUI();
        }

        // update order by UI
        updateOrderByUI();

        // check pending order
        if (pendingOrder) {
            Utils.showShortToast(activity, R.string.ordered);
            pendingOrder = false;
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        showError(R.string.failed_loading_stadiums);
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
            case R.id.action_search:
                openSearchActivity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openSearchActivity() {
        Intent intent = new Intent(activity, StadiumsSearchActivity.class);
        intent.putExtra(Const.KEY_FILTER, filter);
        startActivityForResult(intent, Const.REQ_SEARCH_STADIUMS);
        activity.overridePendingTransition(R.anim.top_translate_enter, R.anim.no_anim);
    }

    private void showOrderDialog() {
        // set the order type
        orderDialog.setSelectedItemType(orderCriteria.getType());

        // set listener
        orderDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
            @Override
            public void onCheckableSelected(Checkable item) {
                orderCriteria = (OrderCriteria) item;
                preOrder();
            }
        });

        // show the dialog
        orderDialog.show();
    }

    private void preOrder() {
        // check criteria type to order data by the appropriate method
        if (orderCriteria.getType() == OrderCriteria.TYPE_NAME) {
            // check data
            if (data != null) {
                orderByName();
            } else {
                Utils.showShortToast(activity, R.string.data_not_loaded_yet);
            }
        } else if (orderCriteria.getType() == OrderCriteria.TYPE_DEFAULT) {
            orderByDefault();
        } else if (orderCriteria.getType() == OrderCriteria.TYPE_LOCATION) {
            orderByLocation();
        }
    }

    private void orderByName() {
        orderController.orderStadiums(data, orderCriteria.getType());

        // update the ui and show msg
        adapter.notifyDataSetChanged();
        updateOrderByUI();
        Utils.showShortToast(activity, R.string.ordered);
    }

    private void orderByDefault() {
        resetFilters();
        location = null;
        pendingOrder = true;
        loadData(false);
    }

    private void orderByLocation() {
        // check location permission
        boolean permGranted = PermissionUtil.isGranted(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        if (!permGranted) {
            // request location permission
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Const.PERM_REQ_LOCATION);
        } else {
            // check GPS
            if (!LocationUtils.isGpsEnabled(activity)) {
                // open location settings snd show msg
                LocationUtils.openLocationSettingsActivity(activity);
                Utils.showShortToast(activity, R.string.please_enable_gps_first);
            } else {
                // get location
                Location location = LocationUtils.getLastKnownLocation(activity);

                // check it
                if (location == null) {
                    Utils.showShortToast(activity, R.string.failed_getting_your_location);
                } else {
                    // set filters
                    OrderCriteria orderCriteria = this.orderCriteria;
                    resetFilters();
                    this.orderCriteria = orderCriteria;
                    this.location = location;
                    pendingOrder = true;

                    // load data ordered from server
                    loadData(false);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Const.PERM_REQ_LOCATION) {
            // check result
            if (PermissionUtil.isAllGranted(grantResults)) {
                orderByLocation();
            } else {
                Utils.showShortToast(activity, R.string.permission_declined);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Const.REQ_SEARCH_STADIUMS && resultCode == Activity.RESULT_OK) {
            resetFilters();
            filter = (StadiumsFilter) data.getSerializableExtra(Const.KEY_FILTER);
            // load data with this filter
            loadData(false);
        } else if (requestCode == Const.REQ_VIEW_STADIUM_INFO && resultCode == Activity.RESULT_OK) {
            // user added some reservation
            if (activity instanceof StadiumsActivity) {
                // fire the reservation added method
                ((StadiumsActivity) activity).onReservationAdded();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
