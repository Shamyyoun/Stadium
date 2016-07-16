package com.stadium.player.fragments;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stadium.player.R;
import com.stadium.player.adapters.TeamsAdapter;
import com.stadium.player.models.entities.Team;
import com.stadium.player.models.enums.TeamClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karam on 6/30/16.
 */
public class MyTeamsFragment extends ParentFragment {
    private RecyclerView recyclerView;
    private List<Team> data;
    private TeamsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_teams, container, false);

        // prepare the data
        data = getDummyData();

        // customize the recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(activity, 3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TeamsAdapter(activity, data, R.layout.item_team);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    public List<Team> getDummyData() {
        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Team team = new Team();
            team.setTitle("العنوان " + i);
            String logo = "https://upload.wikimedia.org/wikipedia/ar/archive/6/6a/20130520191206!Ahly_Fc_new_logo.png";
            team.setLogo(logo);

            if (i == 14) {
                team.setTeamClass(TeamClass.A);
            }

            if (i == 22) {
                team.setTeamClass(TeamClass.C);
            }

            teams.add(team);
        }

        return teams;
    }
}
