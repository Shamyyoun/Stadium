package com.stadium.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stadium.app.R;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.Utils;

import java.util.List;

/**
 * Created by karam on 7/17/16.
 */
public class SimplePlayersAdapter extends ParentRecyclerAdapter<User> {

    public SimplePlayersAdapter(Context context, List<User> data, int layoutId) {
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
        final User item = data.get(position);
        holder.tvName.setText(item.getName());
        Utils.loadImage(context, item.getImageLink(), R.drawable.default_image, holder.ivImage);
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        private ImageView ivImage;
        private TextView tvName;

        public ViewHolder(final View itemView) {
            super(itemView);

            ivImage = (ImageView) findViewById(R.id.iv_image);
            tvName = (TextView) findViewById(R.id.tv_name);
        }
    }
}
