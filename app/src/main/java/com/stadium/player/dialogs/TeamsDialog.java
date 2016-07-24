package com.stadium.player.dialogs;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.stadium.player.R;
import com.stadium.player.adapters.RadioButtonsAdapter;
import com.stadium.player.models.entities.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 6/28/16.
 */
public class TeamsDialog extends ParentDialog {
    private RecyclerView recyclerView;
    private RadioButtonsAdapter adapter;
    private List<Team> data;

    public TeamsDialog(final Context context) {
        super(context);
        setContentView(R.layout.dialog_teams);
        setTitle(R.string.add_player_to);

        // init views
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // customize the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        data = getDummyData();
        adapter = new RadioButtonsAdapter(context, data, R.layout.item_radio_button);
        recyclerView.setAdapter(adapter);
    }

    private List<Team> getDummyData() {
        List<Team> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Team item = new Team();
            item.setTitle("الفريق " + i);
            data.add(item);
        }

        return data;
    }
}