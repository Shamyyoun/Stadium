package com.stadium.app.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.stadium.app.ApiRequests;
import com.stadium.app.R;
import com.stadium.app.adapters.SimplePlayersAdapter;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.controllers.UserController;
import com.stadium.app.interfaces.OnItemClickListener;
import com.stadium.app.interfaces.OnUserSelectedListener;
import com.stadium.app.models.SerializableListWrapper;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shamyyoun on 6/28/16.
 */
public class ChoosePlayerDialog extends ProgressDialog implements OnItemClickListener {
    private int teamId;
    private RecyclerView recyclerView;
    private SimplePlayersAdapter adapter;
    private List<User> data;
    private OnUserSelectedListener userSelectedListener;
    private String emptyMsg;
    private boolean removeCurrentUser; // used to display data without current active user or not

    public ChoosePlayerDialog(final Context context, List<User> players) {
        super(context);
        data = players;
        init();
    }
    public ChoosePlayerDialog(final Context context, int teamId) {
        super(context);
        this.teamId = teamId;
        init();
    }

    private void init() {
        // set the title and the initial empty msg
        setTitle(R.string.the_players);
        emptyMsg = getString(R.string.no_players_found);

        // customize the recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get data from saved bundle if exists
        if (savedInstanceState != null) {
            SerializableListWrapper<User> dataWrapper = (SerializableListWrapper<User>) savedInstanceState.getSerializable("dataWrapper");
            if (dataWrapper != null) {
                data = dataWrapper.getList();
            }
        }

        // check data
        if (data != null) {
            // check size
            if (data.size() == 0) {
                showEmpty(emptyMsg);
            } else {
                updateUI();
            }
        } else {
            loadData();
        }
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.dialog_choose_player;
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

    private void updateUI() {
        adapter = new SimplePlayersAdapter(context, data, R.layout.item_player_simple);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        showMain();
    }

    @Override
    public void onItemClick(View view, int position) {
        User player = data.get(position);
        if (userSelectedListener != null) {
            userSelectedListener.onUserSelected(player);
            dismiss();
        }
    }

    private void loadData() {
        // check internet connection
        if (!Utils.hasConnection(context)) {
            showError(R.string.no_internet_connection);
            return;
        }

        showProgress();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.teamPlayers(context, this, teamId);
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        // get data
        User[] usersArr = (User[]) response;
        data = new ArrayList<>(Arrays.asList(usersArr));

        // check to remove current active user from the list
        if (removeCurrentUser) {
            // remove it from users
            ActiveUserController activeUserController = new ActiveUserController(context);
            UserController userController = new UserController(activeUserController.getUser());
            data = userController.removeFromList(data);
        }

        // check size
        if (data.size() == 0) {
            showEmpty(emptyMsg);
        } else {
            updateUI();
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        showError(R.string.failed_loading_players);
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle outState = new Bundle();
        SerializableListWrapper dataWrapper = new SerializableListWrapper<>(data);
        outState.putSerializable("dataWrapper", dataWrapper);

        return outState;
    }

    public void setOnUserSelectedListener(OnUserSelectedListener userSelectedListener) {
        this.userSelectedListener = userSelectedListener;
    }

    public void setEmptyMsg(String emptyMsg) {
        this.emptyMsg = emptyMsg;
    }

    public void setRemoveCurrentUser(boolean removeCurrentUser) {
        this.removeCurrentUser = removeCurrentUser;
    }
}