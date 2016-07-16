package com.stadium.player.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stadium.player.R;
import com.stadium.player.models.entities.Team;
import com.stadium.player.utils.Utils;

import java.util.List;

/**
 * Created by Shamyyoun on 19/2/16.
 */
public class TeamsAdapter extends ParentRecyclerAdapter<Team> {

    public TeamsAdapter(Context context, List<Team> data, int layoutId) {
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
        final Team item = data.get(position);
        if (item.getTeamClass() != null) {
            holder.tvClass.setText(item.getTeamClass().getTitle());
            holder.tvClass.setBackgroundResource(item.getTeamClass().getColorId());
            holder.tvClass.setVisibility(View.VISIBLE);
        } else {
            holder.tvClass.setVisibility(View.GONE);
        }
        holder.tvTitle.setText(item.getTitle());
        Utils.loadImage(context, item.getLogo(), R.drawable.default_team_image, holder.ivLogo);
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        ImageView ivLogo;
        TextView tvClass;
        TextView tvTitle;

        public ViewHolder(final View itemView) {
            super(itemView);

            ivLogo = (ImageView) itemView.findViewById(R.id.iv_logo);
            tvClass = (TextView) itemView.findViewById(R.id.tv_class);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}
