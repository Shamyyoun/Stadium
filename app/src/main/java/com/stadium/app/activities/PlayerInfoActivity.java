package com.stadium.app.activities;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.stadium.app.R;
import com.stadium.app.adapters.TeamsAdapter;
import com.stadium.app.dialogs.TeamsDialog;
import com.stadium.app.models.entities.Team;
import com.stadium.app.models.enums.TeamClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karam on 7/19/16.
 */
public class PlayerInfoActivity extends ParentToolbarActivity {
    private Button btnAdd;
    private RecyclerView recyclerView;
    private List<Team> data;
    private TeamsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_info);
        enableBackButton();

        // init views
        btnAdd = (Button) findViewById(R.id.btn_add);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // customize the recycler view
        GridLayoutManager layoutManager = new GridLayoutManager(activity, 3);
        recyclerView.setLayoutManager(layoutManager);
        data = getDummyData();
        adapter = new TeamsAdapter(activity, data, R.layout.item_team);
        recyclerView.setAdapter(adapter);

        // add listeners
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add) {
            // show teams dialog
            TeamsDialog dialog = new TeamsDialog(this);
            dialog.show();
        } else {
            super.onClick(v);
        }
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
