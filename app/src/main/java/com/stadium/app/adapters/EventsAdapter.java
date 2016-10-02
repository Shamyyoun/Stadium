package com.stadium.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stadium.app.R;
import com.stadium.app.models.entities.Event;

import java.util.List;

/**
 * Created by Shamyyoun on 19/2/16.
 */
public class EventsAdapter extends ParentRecyclerAdapter<Event> {

    public EventsAdapter(Context context, List<Event> data, int layoutId) {
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
        final Event item = data.get(position);
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        private TextView tvTitle;

        public ViewHolder(final View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}
