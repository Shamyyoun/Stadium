package com.stadium.app.dialogs;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.stadium.app.R;
import com.stadium.app.adapters.PlayersAdapter;
import com.stadium.app.interfaces.OnItemClickListener;
import com.stadium.app.interfaces.OnUserSelectedListener;
import com.stadium.app.models.entities.User;
import com.stadium.app.views.DividerItemDecoration;
import com.stadium.app.views.EmptyView;

import java.util.List;

/**
 * Created by Shamyyoun on 6/28/16.
 */
public class ChoosePlayerDialog extends ParentDialog implements OnItemClickListener {
    private EmptyView emptyView;
    private RecyclerView recyclerView;
    private PlayersAdapter adapter;
    private List<User> players;

    private OnUserSelectedListener userSelectedListener;

    public ChoosePlayerDialog(final Context context, List<User> players) {
        super(context);
        this.players = players;

        setContentView(R.layout.dialog_choose_player);
        setTitle(R.string.the_players);

        // init views
        emptyView = (EmptyView) findViewById(R.id.empty_view);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // customize the empty view
        emptyView.setLayoutResId(R.layout.view_dialog_empty);
        emptyView.setEmpty(R.string.no_players_in_this_team);

        // customize the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        if (players.size() > 1) {
            recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        }

        // check data size
        if (players.size() == 0) {
            // show empty view
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            // create and set the players adapter
            adapter = new PlayersAdapter(context, players, R.layout.item_player_simple, PlayersAdapter.TYPE_SIMPLE);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(this);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        User player = players.get(position);
        if (userSelectedListener != null) {
            userSelectedListener.onUserSelected(player);
            dismiss();
        }
    }

    public void setOnUserSelectedListener(OnUserSelectedListener userSelectedListener) {
        this.userSelectedListener = userSelectedListener;
    }

    public void setCustomEmptyMsg(String customEmptyMsg) {
        emptyView.setEmpty(customEmptyMsg);
    }
}