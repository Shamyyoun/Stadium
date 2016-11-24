package com.stadium.app.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.stadium.app.ApiRequests;
import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.activities.CreateTeamActivity;
import com.stadium.app.activities.ProfileImageActivity;
import com.stadium.app.activities.UpdateProfileActivity;
import com.stadium.app.adapters.EventsAdapter;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.interfaces.OnItemClickListener;
import com.stadium.app.models.SerializableListWrapper;
import com.stadium.app.models.entities.Event;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.BitmapUtils;
import com.stadium.app.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shamyyoun on 7/2/16.
 */
public class PlayerHomeFragment extends ProgressFragment implements OnItemClickListener {
    private ActiveUserController userController;
    private View layoutImage;
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
        createOptionsMenu(R.menu.menu_player_home);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // create the user controller
        userController = new ActiveUserController(activity);

        // init views
        layoutImage = findViewById(R.id.layout_image);
        ivImage = (ImageView) findViewById(R.id.iv_image);
        tvRating = (TextView) findViewById(R.id.tv_rating);
        tvName = (TextView) findViewById(R.id.tv_name);
        btnCreateTeam = (Button) findViewById(R.id.btn_create_team);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // customize the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // add listeners
        layoutImage.setOnClickListener(this);
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
            if (!data.isEmpty()) {
                updateEventsUI();
            } else {
                showEmpty(R.string.no_events_yet);
            }
        } else {
            loadData();
        }

        return rootView;
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.fragment_player_home;
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
        tvRating.setText(Utils.formatDouble(user.getRate()));

        // load the profile image
        if (!Utils.isNullOrEmpty(user.getImageLink())) {
            Utils.loadImage(activity, user.getImageLink(), R.drawable.default_image, ivImage);
        } else if (user.getUserImage() != null && !Utils.isNullOrEmpty(user.getUserImage().getContentBase64())) {
            Bitmap bitmap = BitmapUtils.decodeBase64(user.getUserImage().getContentBase64());
            ivImage.setImageBitmap(bitmap);
        }
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
        if (v.getId() == R.id.layout_image) {
            // open profile image activity
            Intent intent = new Intent(activity, ProfileImageActivity.class);
            startActivityForResult(intent, Const.REQ_UPDATE_PROFILE_IMAGE);
            activity.overridePendingTransition(R.anim.scale_fade_enter, R.anim.scale_fade_exit);
        } else if (v.getId() == R.id.btn_create_team) {
            // open create team activity
            Intent intent = new Intent(activity, CreateTeamActivity.class);
            startActivity(intent);
        } else {
            super.onClick(v);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            // open update profile activity
            Intent intent = new Intent(activity, UpdateProfileActivity.class);
            startActivityForResult(intent, Const.REQ_UPDATE_PROFILE);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Const.REQ_UPDATE_PROFILE || requestCode == Const.REQ_UPDATE_PROFILE_IMAGE) {
                updateUserUI();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        SerializableListWrapper dataWrapper = new SerializableListWrapper<>(data);
        outState.putSerializable("dataWrapper", dataWrapper);
        super.onSaveInstanceState(outState);
    }
}
