package com.stadium.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stadium.app.R;
import com.stadium.app.controllers.TeamController;
import com.stadium.app.models.entities.PlayerRole;
import com.stadium.app.models.entities.Team;
import com.stadium.app.utils.Utils;

import java.util.List;

/**
 * Created by Shamyyoun on 19/2/16.
 */
public class TeamsAdapter extends ParentRecyclerAdapter<Team> {
    private int playerId;
    private TeamController teamController;

    public TeamsAdapter(Context context, List<Team> data, int layoutId, int playerId) {
        super(context, data, layoutId);
        this.playerId = playerId;
        teamController = new TeamController();
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
        Team item = data.get(position);
        holder.tvTitle.setText(item.getName());
        Utils.loadImage(context, item.getImageLink(), R.drawable.default_image, holder.ivLogo);

        // set the role
        PlayerRole role = teamController.getPlayerRole(item, playerId);
        if (role != null) {
            holder.tvRole.setText(role.getChar());
            holder.tvRole.setBackgroundResource(role.getBackgroundResId());
            holder.tvRole.setVisibility(View.VISIBLE);
        } else {
            holder.tvRole.setVisibility(View.GONE);
        }
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        private ImageView ivLogo;
        private TextView tvRole;
        private TextView tvTitle;

        public ViewHolder(final View itemView) {
            super(itemView);

            ivLogo = (ImageView) findViewById(R.id.iv_logo);
            tvRole = (TextView) findViewById(R.id.tv_role);
            tvTitle = (TextView) findViewById(R.id.tv_title);
        }
    }
}
