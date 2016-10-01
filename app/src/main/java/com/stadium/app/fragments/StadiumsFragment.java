package com.stadium.app.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stadium.app.ApiRequests;
import com.stadium.app.R;
import com.stadium.app.adapters.StadiumsAdapter;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.controllers.UserController;
import com.stadium.app.dialogs.OrderStadiumsDialog;
import com.stadium.app.interfaces.OnItemClickListener;
import com.stadium.app.models.SerializableListWrapper;
import com.stadium.app.models.entities.Stadium;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shamyyoun on 7/2/16.
 */
public class StadiumsFragment extends ProgressToolbarFragment implements OnItemClickListener {
    private TextView tvOrderBy;
    private RecyclerView recyclerView;
    private List<Stadium> data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.stadiums);
        createOptionsMenu(R.menu.menu_stadiums);
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

        // add listeners
        tvOrderBy.setOnClickListener(this);

        // get data from saved bundle if exists
        if (savedInstanceState != null) {
            SerializableListWrapper<Stadium> dataWrapper = (SerializableListWrapper<Stadium>) savedInstanceState.getSerializable("dataWrapper");
            if (dataWrapper != null) {
                data = dataWrapper.getList();
            }
        }

        // check data
        if (data != null) {
            updateUI();
        } else {
            loadData();
        }

        return rootView;
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.fragment_stadiums;
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
                loadData();
            }
        };
    }

    private void updateUI() {
        StadiumsAdapter adapter = new StadiumsAdapter(activity, data, R.layout.item_stadium);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        showMain();
    }

    @Override
    public void onItemClick(View view, int position) {
        logE("Item clicked: " + position);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_order_by) {
            // show order data dialog
            OrderStadiumsDialog dialog = new OrderStadiumsDialog(activity);
            dialog.show();
        } else {
            super.onClick(v);
        }
    }

    private void loadData() {
        // check internet connection
        if (!Utils.hasConnection(activity)) {
            showError(R.string.no_internet_connection);
            return;
        }

        showProgress();

        // get current user
        UserController userController = new UserController(activity);
        User user = userController.getUser();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.listOfStadiums(activity, this, user.getId(), user.getToken());
        cancelWhenDestroyed(connectionHandler);
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
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        showError(R.string.failed_loading_stadiums);
        hideProgressDialog();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        SerializableListWrapper dataWrapper = new SerializableListWrapper<>(data);
        outState.putSerializable("dataWrapper", dataWrapper);
        super.onSaveInstanceState(outState);
    }
}
