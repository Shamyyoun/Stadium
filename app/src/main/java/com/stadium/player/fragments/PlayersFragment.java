package com.stadium.player.fragments;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stadium.player.R;
import com.stadium.player.adapters.PlayersAdapter;
import com.stadium.player.adapters.TeamsAdapter;
import com.stadium.player.models.entities.Players;

import java.util.List;

/**
 * Created by karam on 7/18/16.
 */
public class PlayersFragment extends ParentFragment {

    private RecyclerView rvPlayers;
    private List<Players> data;
    private PlayersAdapter playersAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_players, container, false);

        // customize the recycler view
        rvPlayers = (RecyclerView) findViewById(R.id.rv_players);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        rvPlayers.setLayoutManager(layoutManager);
        playersAdapter = new PlayersAdapter(activity, data, R.layout.item_players);
        rvPlayers.setAdapter(playersAdapter);

        return rootView;
    }

}
