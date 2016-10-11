package com.stadium.app.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.stadium.app.R;
import com.stadium.app.adapters.PlayersAdapter;
import com.stadium.app.models.entities.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karam on 7/17/16.
 */
public class ContactsActivity extends ParentActivity {
    private RecyclerView recyclerView;
    private PlayersAdapter adapter;
    private List<Player> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        enableBackButton();

        // init views
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // customize the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        data = getDummyData();
        adapter = new PlayersAdapter(activity, data, R.layout.item_player);
        recyclerView.setAdapter(adapter);
    }

    private List<Player> getDummyData() {
        List<Player> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Player item = new Player();
            data.add(item);
        }

        return data;
    }
}
