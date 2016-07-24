package com.stadium.player.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.stadium.player.R;
import com.stadium.player.dialogs.TeamsDialog;
import com.stadium.player.models.entities.Player;

import java.util.List;

/**
 * Created by karam on 7/17/16.
 */
public class PlayersAdapter extends ParentRecyclerAdapter<Player> {

    public PlayersAdapter(Context context, List<Player> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public ParentRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        holder.setOnItemClickListener(itemClickListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(ParentRecyclerViewHolder viewHolder, final int position) {
        final ViewHolder holder = (ViewHolder) viewHolder;

        // set data
        final Player item = data.get(position);
        holder.ibAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show teams dialog
                TeamsDialog dialog = new TeamsDialog(context);
                dialog.show();
            }
        });
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        private View layoutContent;
        private ImageButton ibAdd;

        public ViewHolder(final View itemView) {
            super(itemView);

            layoutContent = findViewById(R.id.layout_content);
            ibAdd = (ImageButton) findViewById(R.id.ib_add);

            setClickableRootView(layoutContent);
        }
    }
}
