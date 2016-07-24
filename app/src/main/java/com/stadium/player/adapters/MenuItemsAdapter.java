package com.stadium.player.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stadium.player.R;
import com.stadium.player.models.entities.MenuItem;

import java.util.List;

/**
 * Created by Shamyyoun on 19/2/16.
 */
public class MenuItemsAdapter extends ParentRecyclerAdapter<MenuItem> {

    public MenuItemsAdapter(Context context, List<MenuItem> data, int layoutId) {
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
        final MenuItem item = data.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, item.getIconResId(), 0);
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        View itemView;
        TextView tvTitle;

        public ViewHolder(final View itemView) {
            super(itemView);

            this.itemView = itemView;
            tvTitle = (TextView) findViewById(R.id.tv_title);
        }
    }
}
