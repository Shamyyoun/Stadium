package com.stadium.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stadium.app.R;
import com.stadium.app.activities.ContactsActivity;
import com.stadium.app.activities.PlayerInfoActivity;
import com.stadium.app.adapters.PlayersAdapter;
import com.stadium.app.dialogs.OrderPlayersDialog;
import com.stadium.app.interfaces.OnItemClickListener;
import com.stadium.app.models.entities.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 7/2/16.
 */
public class PlayersFragment extends ParentToolbarFragment {
    private TextView tvOrderBy;
    private RecyclerView recyclerView;
    private PlayersAdapter adapter;
    private List<Player> data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.players);
        createOptionsMenu(R.menu.menu_players);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_players, container, false);

        // init views
        tvOrderBy = (TextView) findViewById(R.id.tv_order_by);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // customize the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        data = getDummyData();
        adapter = new PlayersAdapter(activity, data, R.layout.item_player);
        recyclerView.setAdapter(adapter);

        // add listeners
        tvOrderBy.setOnClickListener(this);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // open player info activity
                startActivity(new Intent(activity, PlayerInfoActivity.class));
            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_order_by) {
            // show order players dialog
            OrderPlayersDialog dialog = new OrderPlayersDialog(activity);
            dialog.show();
        } else {
            super.onClick(v);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_contacts) {
            // open contacts activity
            startActivity(new Intent(activity, ContactsActivity.class));
        }

        return super.onOptionsItemSelected(item);
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
