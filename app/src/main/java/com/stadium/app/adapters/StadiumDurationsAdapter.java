package com.stadium.app.adapters;

import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.controllers.DurationController;
import com.stadium.app.models.entities.Duration;
import com.stadium.app.utils.DateUtils;
import com.stadium.app.utils.TimePickerFragment;

import java.util.List;

/**
 * Created by Shamyyoun on 19/2/16.
 */
public class StadiumDurationsAdapter extends ParentRecyclerAdapter<Duration> {
    private static final String DISPLAYED_TIME_FORMAT = "hh:mm a";
    private TimePickerFragment timePickerFragment;
    private DurationController durationController;

    public StadiumDurationsAdapter(Context context, List<Duration> data, int layoutId) {
        super(context, data, layoutId);
        timePickerFragment = new TimePickerFragment();
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

        // prepare start time
        String startTimeStr;
        if (item.getStartTime() != null) {
            startTimeStr = DateUtils.formatDate(item.getStartTime(), Const.SER_TIME_FORMAT, DISPLAYED_TIME_FORMAT);
        } else {
            startTimeStr = getString(R.string.from);
        }

        // prepare end time
        String endTimeStr;
        if (item.getEndTime() != null) {
            endTimeStr = DateUtils.formatDate(item.getEndTime(), Const.SER_TIME_FORMAT, DISPLAYED_TIME_FORMAT);
        } else {
            endTimeStr = getString(R.string.to);
        }

        // check item type
        if (!holder.isUpdateItem()) {
            // just set textviews text
            holder.tvFrom.setText(startTimeStr);
            holder.tvTo.setText(endTimeStr);
        } else {
            // set buttons text
            holder.btnFrom.setText(startTimeStr);
            holder.btnTo.setText(endTimeStr);

            // customize remove button
            if (position != 0 && position == getItemCount() - 1) {
                holder.ibRemove.setVisibility(View.VISIBLE);
            } else {
                holder.ibRemove.setVisibility(View.GONE);
            }

            // create global click listener
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.btn_from:
                            chooseStartDate(position);
                            break;

                        case R.id.btn_to:
                            chooseEndDate(position);
                            break;

                        case R.id.ib_remove:
                            remove(position);
                            break;
                    }
                }
            };

            // add listeners
            holder.btnFrom.setOnClickListener(clickListener);
            holder.btnTo.setOnClickListener(clickListener);
            holder.ibRemove.setOnClickListener(clickListener);
        }
    }

    private void chooseStartDate(final int position) {
        // get item
        final Duration duration = data.get(position);

        // set current time if possible
        if (duration.getStartTime() != null) {
            timePickerFragment.setTime(duration.getStartTime(), Const.SER_TIME_FORMAT);
        }

        // set listener
        timePickerFragment.setTimePickerListener(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // set start time
                String startTime = hourOfDay + ":" + minute;
                startTime = DateUtils.formatDate(startTime, "H:m", Const.SER_TIME_FORMAT);
                duration.setStartTime(startTime);
                notifyItemChanged(position);
            }
        });

        // show time dialog if possible
        if (context instanceof AppCompatActivity) {
            timePickerFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), null);
        }
    }

    private void chooseEndDate(final int position) {
        // get item
        final Duration duration = data.get(position);

        // set current time if possible
        if (duration.getEndTime() != null) {
            timePickerFragment.setTime(duration.getEndTime(), Const.SER_TIME_FORMAT);
        }

        // set listener
        timePickerFragment.setTimePickerListener(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // set end time
                String endTime = hourOfDay + ":" + minute;
                endTime = DateUtils.formatDate(endTime, "H:m", Const.SER_TIME_FORMAT);
                duration.setEndTime(endTime);
                notifyItemChanged(position);
            }
        });

        // show time dialog if possible
        if (context instanceof AppCompatActivity) {
            timePickerFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), null);
        }
    }

    private void remove(int position) {
        removeItem(position);
        notifyDataSetChanged();
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        private TextView tvNumber;
        private TextView tvFrom;
        private TextView tvTo;
        private Button btnFrom;
        private Button btnTo;
        private ImageButton ibRemove;

        public ViewHolder(final View itemView) {
            super(itemView);

            tvNumber = (TextView) findViewById(R.id.tv_number);
            tvFrom = (TextView) findViewById(R.id.tv_from);
            tvTo = (TextView) findViewById(R.id.tv_to);
            btnFrom = (Button) findViewById(R.id.btn_from);
            btnTo = (Button) findViewById(R.id.btn_to);
            ibRemove = (ImageButton) findViewById(R.id.ib_remove);
        }

        public boolean isUpdateItem() {
            return (btnFrom != null && btnTo != null && ibRemove != null);
        }
    }
}
