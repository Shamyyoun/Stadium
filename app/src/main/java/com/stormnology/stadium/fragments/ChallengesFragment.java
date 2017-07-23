package com.stormnology.stadium.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stormnology.stadium.ApiRequests;
import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.adapters.ChallengesAdapter;
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.interfaces.OnItemRemovedListener;
import com.stormnology.stadium.interfaces.OnRefreshListener;
import com.stormnology.stadium.models.SerializableListWrapper;
import com.stormnology.stadium.models.entities.Challenge;
import com.stormnology.stadium.models.enums.ChallengesType;
import com.stormnology.stadium.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shamyyoun on 7/2/16.
 */
public class ChallengesFragment extends ProgressFragment implements OnItemRemovedListener {
    private ChallengesType challengesType;
    private ActiveUserController activeUserController;
    private RecyclerView recyclerView;
    private List<Challenge> data;
    private ChallengesAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // obtain main objects
        challengesType = (ChallengesType) getArguments().getSerializable(Const.KEY_CHALLENGES_TYPE);
        activeUserController = new ActiveUserController(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // init views
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // customize the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // get data from saved bundle if exists
        if (savedInstanceState != null) {
            SerializableListWrapper<Challenge> dataWrapper = (SerializableListWrapper<Challenge>) savedInstanceState.getSerializable("dataWrapper");
            if (dataWrapper != null) {
                data = dataWrapper.getList();
            }
        }

        // check data
        if (data != null) {
            if (!data.isEmpty()) {
                updateUI();
            } else {
                showEmpty(R.string.no_challenges_found);
            }
        }

        return rootView;
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.fragment_challenges;
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
        // prepare item layout id
        int itemLayoutId;
        switch (challengesType) {
            case NEW_CHALLENGES:
                itemLayoutId = R.layout.item_new_challenge;
                break;

            case ACCEPTED_CHALLENGES:
                itemLayoutId = R.layout.item_accepted_challenge;
                break;

            default:
                itemLayoutId = R.layout.item_main_challenge;
        }

        // create and set the adapter
        adapter = new ChallengesAdapter(activity, data, itemLayoutId);
        adapter.setChallengesType(challengesType);
        adapter.setOnItemRemovedListener(this);
        recyclerView.setAdapter(adapter);
        showMain();
    }

    public void loadDataIfRequired() {
        if (data == null) {
            loadData();
        }
    }

    private void loadData() {
        // check internet connection
        if (!Utils.hasConnection(activity)) {
            showError(R.string.no_internet_connection);
            return;
        }

        // get active user id
        int userId = activeUserController.getUser().getId();

        // send suitable request
        ConnectionHandler connectionHandler = null;
        if (challengesType == ChallengesType.NEW_CHALLENGES) {
            connectionHandler = ApiRequests.newChallenges(activity, this, userId);
        } else if (challengesType == ChallengesType.ACCEPTED_CHALLENGES) {
            connectionHandler = ApiRequests.acceptedChallenges(activity, this, userId);
        } else if (challengesType == ChallengesType.HISTORICAL_CHALLENGES) {
            connectionHandler = ApiRequests.historicChallenges(activity, this, userId);
        } else if (challengesType == ChallengesType.MY_CHALLENGES) {
            connectionHandler = ApiRequests.myChallenges(activity, this, userId);
        }

        // show progress if suitable
        if (connectionHandler != null) {
            showProgress();
        }

        cancelWhenDestroyed(connectionHandler);
    }

    private void refresh() {
        loadData();
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        // get data
        Challenge[] challengesArr = (Challenge[]) response;
        data = new ArrayList<>(Arrays.asList(challengesArr));

        // check size
        if (data.size() == 0) {
            showEmpty(R.string.no_challenges_found);
        } else {
            updateUI();
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        showError(R.string.failed_loading_challenges);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        SerializableListWrapper dataWrapper = new SerializableListWrapper<>(data);
        outState.putSerializable("dataWrapper", dataWrapper);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemRemoved(int position) {
        // check data size
        if (data.size() == 0) {
            showEmpty(R.string.no_challenges_found);
        }
    }
}
