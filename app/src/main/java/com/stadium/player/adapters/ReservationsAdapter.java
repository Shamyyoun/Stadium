package com.stadium.player.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stadium.player.R;
import com.stadium.player.models.entities.Reservation;

import java.util.List;

/**
 * Created by Shamyyoun on 19/2/16.
 */
public class ReservationsAdapter extends ParentRecyclerAdapter<Reservation> {

    public ReservationsAdapter(Context context, List<Reservation> data, int layoutId) {
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
        final Reservation item = data.get(position);
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
