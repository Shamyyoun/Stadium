package com.stormnology.stadium.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.stormnology.stadium.ApiRequests;
import com.stormnology.stadium.R;
import com.stormnology.stadium.adapters.TeamsAdapter;
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.interfaces.OnItemClickListener;
import com.stormnology.stadium.interfaces.OnRefreshListener;
import com.stormnology.stadium.interfaces.OnTeamSelectedListener;
import com.stormnology.stadium.models.SerializableListWrapper;
import com.stormnology.stadium.models.entities.Team;
import com.stormnology.stadium.utils.Utils;
import com.stormnology.stadium.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shamyyoun on 6/28/16.
 */
public class ChooseFromAllTeamsDialog extends ProgressDialog implements OnItemClickListener {
    private RecyclerView recyclerView;
    private TeamsAdapter adapter;
    private List<Team> data;
    private OnTeamSelectedListener teamSelectedListener;

    public ChooseFromAllTeamsDialog(final Context context) {
        super(context);
        setTitle(R.string.all_teams);

        // init & customize the recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get data from saved bundle if exists
        if (savedInstanceState != null) {
            SerializableListWrapper<Team> dataWrapper = (SerializableListWrapper<Team>) savedInstanceState.getSerializable("dataWrapper");
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
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.dialog_choose_from_all_teams;
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
        adapter = new TeamsAdapter(context, data, R.layout.item_team);
        recyclerView.setAdapter(adapter);
        showMain();
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        Team team = data.get(position);
        if (teamSelectedListener != null) {
            teamSelectedListener.onTeamSelected(team);
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
        ConnectionHandler connectionHandler = ApiRequests.getAllTeams(context, this);
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        // get data
        Team[] teamsArr = (Team[]) response;
        data = new ArrayList<>(Arrays.asList(teamsArr));

        // check size
        if (data.size() == 0) {
            showEmpty(R.string.no_teams_found);
        } else {
            updateUI();
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        showError(R.string.failed_loading_teams);
    }


    @Override
    public Bundle onSaveInstanceState() {
        Bundle outState = new Bundle();
        SerializableListWrapper dataWrapper = new SerializableListWrapper<>(data);
        outState.putSerializable("dataWrapper", dataWrapper);

        return outState;
    }

    public void setOnTeamSelectedListener(OnTeamSelectedListener teamSelectedListener) {
        this.teamSelectedListener = teamSelectedListener;
    }
}