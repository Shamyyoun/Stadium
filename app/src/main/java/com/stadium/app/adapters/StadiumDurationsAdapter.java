package com.stadium.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.controllers.DurationController;
import com.stadium.app.models.entities.Duration;
import com.stadium.app.utils.DateUtils;

import java.util.List;

/**
 * Created by Shamyyoun on 19/2/16.
 */
public class StadiumDurationsAdapter extends ParentRecyclerAdapter<Duration> {
    private static final String DISPLAYED_TIME_FORMAT = "hh:mm a";
    private DurationController durationController;

    public StadiumDurationsAdapter(Context context, List<Duration> data, int layoutId) {
        super(context, data, layoutId);
        durationController = new DurationController();
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

        // set number
        final Duration item = data.get(position);
        String numberStr = getString(R.string.duration) + "  " + item.getDurationNumber() + ":";
        holder.tvNumber.setText(numberStr);

        // set start time
        String startTimeStr;
        if (item.getStartTime() != null) {
            startTimeStr = DateUtils.formatDate(item.getStartTime(), Const.SER_TIME_FORMAT, DISPLAYED_TIME_FORMAT);
        } else {
            startTimeStr = getString(R.string.from);
        }
        holder.tvFrom.setText(startTimeStr);

        // set end time
        String endTimeStr;
        if (item.getEndTime() != null) {
            endTimeStr = DateUtils.formatDate(item.getEndTime(), Const.SER_TIME_FORMAT, DISPLAYED_TIME_FORMAT);
        } else {
            endTimeStr = getString(R.string.to);
        }
        holder.tvTo.setText(endTimeStr);
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        private TextView tvNumber;
        private TextView tvFrom;
        private TextView tvTo;

        public ViewHolder(final View itemView) {
            super(itemView);

            tvNumber = (TextView) findViewById(R.id.tv_number);
            tvFrom = (TextView) findViewById(R.id.tv_from);
            tvTo = (TextView) findViewById(R.id.tv_to);
        }
    }
}
