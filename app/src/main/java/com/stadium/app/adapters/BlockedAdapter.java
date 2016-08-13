package com.stadium.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stadium.app.R;
import com.stadium.app.models.entities.Blocked;
import com.stadium.app.models.entities.Reservation;

import java.util.List;

/**
 * Created by karam on 8/13/16.
 */
public class BlockedAdapter extends ParentRecyclerAdapter<Blocked> {

    public BlockedAdapter(Context context, List<Blocked> data, int layoutId) {
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
        final Blocked item = data.get(position);
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        private View layoutContent;

        public ViewHolder(final View itemView) {
            super(itemView);

            layoutContent = findViewById(R.id.layout_content);
            setClickableRootView(layoutContent);
        }
    }
}
