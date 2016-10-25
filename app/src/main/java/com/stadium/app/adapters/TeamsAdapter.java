package com.stadium.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stadium.app.R;
import com.stadium.app.models.entities.Team;
import com.stadium.app.utils.Utils;

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
        Team item = data.get(position);
        holder.tvTitle.setText(item.getName());
        Utils.loadImage(context, item.getImageLink(), R.drawable.default_image, holder.ivLogo);
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        ImageView ivLogo;
        TextView tvClass;
        TextView tvTitle;

        public ViewHolder(final View itemView) {
            super(itemView);

            ivLogo = (ImageView) findViewById(R.id.iv_logo);
            tvClass = (TextView) findViewById(R.id.tv_class);
            tvTitle = (TextView) findViewById(R.id.tv_title);
        }
    }
}
