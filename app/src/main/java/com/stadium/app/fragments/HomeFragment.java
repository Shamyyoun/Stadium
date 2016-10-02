package com.stadium.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.stadium.app.ApiRequests;
import com.stadium.app.R;
import com.stadium.app.activities.CreateTeamActivity;
import com.stadium.app.adapters.EventsAdapter;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.controllers.UserController;
import com.stadium.app.interfaces.OnItemClickListener;
import com.stadium.app.models.SerializableListWrapper;
import com.stadium.app.models.entities.Event;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shamyyoun on 7/2/16.
 */
public class HomeFragment extends ProgressToolbarFragment implements OnItemClickListener {
    private UserController userController;
    private ImageView ivImage;
    private TextView tvRating;
    private TextView tvName;
    private Button btnCreateTeam;
    private RecyclerView recyclerView;
    private List<Event> data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.home);
        createOptionsMenu(R.menu.menu_home);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // create the user controller
        userController = new UserController(activity);

        // init views
        ivImage = (ImageView) findViewById(R.id.iv_image);
        tvRating = (TextView) findViewById(R.id.tv_rating);
        tvName = (TextView) findViewById(R.id.tv_name);
        btnCreateTeam = (Button) findViewById(R.id.btn_create_team);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // customize the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // add listeners
        btnCreateTeam.setOnClickListener(this);

        // update the ui
        updateUserUI();

        // get data from saved bundle if exists
        if (savedInstanceState != null) {
            SerializableListWrapper<Event> dataWrapper = (SerializableListWrapper<Event>) savedInstanceState.getSerializable("dataWrapper");
            if (dataWrapper != null) {
                data = dataWrapper.getList();
            }
        }

        // check data
        if (data != null) {
            updateEventsUI();
        } else {
            loadData();
        }

        return rootView;
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.fragment_home;
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
                loadData();
            }
        };
    }

    private void updateUserUI() {
        User user = userController.getUser();
        tvName.setText(userController.getNamePosition());
        tvRating.setText("" + user.getRate());

        Utils.loadImage(activity, user.getImageLink(), R.drawable.default_image, ivImage);
    }

    private void updateEventsUI() {
        EventsAdapter adapter = new EventsAdapter(activity, data, R.layout.item_event);
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
        if (v.getId() == R.id.btn_create_team) {
            // open create team activity
            Intent intent = new Intent(activity, CreateTeamActivity.class);
            startActivity(intent);
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
        User user = userController.getUser();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.getEvent(activity, this, user.getId());
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        // get data
        Event[] eventsArr = (Event[]) response;
        data = new ArrayList<>(Arrays.asList(eventsArr));

        // check size
        if (data.size() == 0) {
            showEmpty(R.string.no_events_yet);
        } else {
            updateEventsUI();
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        showError(R.string.failed_loading_events);
        hideProgressDialog();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        SerializableListWrapper dataWrapper = new SerializableListWrapper<>(data);
        outState.putSerializable("dataWrapper", dataWrapper);
        super.onSaveInstanceState(outState);
    }
}
