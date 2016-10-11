package com.stadium.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stadium.app.R;
import com.stadium.app.activities.TeamInfoActivity;
import com.stadium.app.adapters.TeamsAdapter;
import com.stadium.app.interfaces.OnItemClickListener;
import com.stadium.app.models.entities.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karam on 6/30/16.
 */
public class MyTeamsFragment extends ParentToolbarFragment {
    private RecyclerView recyclerView;
    private List<Team> data;
    private TeamsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.my_teams);
        removeOptionsMenu();
    }

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

        // add the item click listener
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
               activity.startActivity(new Intent(getActivity() , TeamInfoActivity.class));
            }
        });

        return rootView;
    }

    public List<Team> getDummyData() {
        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Team team = new Team();
            team.setName("العنوان " + i);
            String logo = "https://upload.wikimedia.org/wikipedia/ar/archive/6/6a/20130520191206!Ahly_Fc_new_logo.png";
            team.setImageLink(logo);

            teams.add(team);
        }

        return teams;
    }
}
