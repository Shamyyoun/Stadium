package com.stadium.player.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.stadium.player.R;
import com.stadium.player.models.entities.StadiumsItem;
import com.stadium.player.models.entities.Team;
import com.stadium.player.utils.Utils;

import java.util.List;

/**
 * Created by karam on 7/2/16.
 */
public class StadiumsAdapter extends ParentRecyclerAdapter<StadiumsItem> {

    public static int mSelectedItem = 0;

    public StadiumsAdapter(Context context, List<StadiumsItem> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public ParentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        holder.setOnItemClickListener(itemClickListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(ParentViewHolder viewHolder, final int position) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        // set data
        final StadiumsItem item = data.get(position);

        holder.tv_stadium_name.setText(item.getStadium());

        //make radio check when choose it

        if (position == mSelectedItem) {
            holder.radio.setChecked(true);
        } else {
            holder.radio.setChecked(false);
        }
    }

    class ViewHolder extends ParentViewHolder {
        public RadioButton radio;
        public TextView tv_stadium_name;

        public ViewHolder(final View itemView) {
            super(itemView);

            radio = (RadioButton) itemView.findViewById(R.id.rb_stadiums);
            tv_stadium_name = (TextView) itemView.findViewById(R.id.tv_stadiums_name);
        }
    }
}
