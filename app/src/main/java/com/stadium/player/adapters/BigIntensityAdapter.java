package com.stadium.player.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stadium.player.models.entities.Attendant;
import com.stadium.player.models.entities.BigIntensity;

import java.util.List;

/**
 * Created by karam on 7/31/16.
 */
public class BigIntensityAdapter extends ParentRecyclerAdapter<BigIntensity> {

    public BigIntensityAdapter(Context context, List<BigIntensity> data, int layoutId) {
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
        final BigIntensity item = data.get(position);
    }

    class ViewHolder extends ParentRecyclerViewHolder {

        public ViewHolder(final View itemView) {
            super(itemView);
        }
    }
}
