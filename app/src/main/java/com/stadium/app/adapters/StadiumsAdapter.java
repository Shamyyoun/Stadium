package com.stadium.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stadium.app.models.entities.Stadium;

import java.util.List;

/**
 * Created by Shamyyoun on 7/2/16.
 */
public class StadiumsAdapter extends ParentRecyclerAdapter<Stadium> {

    public StadiumsAdapter(Context context, List<Stadium> data, int layoutId) {
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
        final Stadium item = data.get(position);
    }

    class ViewHolder extends ParentRecyclerViewHolder {

        public ViewHolder(final View itemView) {
            super(itemView);
        }
    }
}
