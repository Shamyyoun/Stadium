package com.stadium.app.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stadium.app.R;
import com.stadium.app.adapters.TeamPlayersAdapter;
import com.stadium.app.models.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karam on 7/26/16.
 */
public class TeamPlayersFragment  extends ParentFragment {
    private RecyclerView recyclerView;
    private TeamPlayersAdapter adapter;
    private List<User> data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_team_players, container, false);

        // init views
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // customize the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
//        data = getDummyData();
//        adapter = new TeamPlayersAdapter(activity, data, R.layout.item_team_players);
//        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private List<User> getDummyData() {
        List<User> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            User item = new User();
            data.add(item);
        }

        return data;
    }
}
