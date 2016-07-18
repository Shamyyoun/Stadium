package com.stadium.player.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.stadium.player.R;
import com.stadium.player.models.entities.Stadium;

import java.util.List;

/**
 * Created by karam on 7/2/16.
 */
public class FavoriteStadiumsAdapter extends ParentRecyclerAdapter<Stadium> {

    public static int mSelectedItem = 0;

    public FavoriteStadiumsAdapter(Context context, List<Stadium> data, int layoutId) {
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

        holder.tv_stadium_name.setText(item.getTitle());

        //make radio check when choose it

        if (position == mSelectedItem) {
            holder.radio.setChecked(true);
            holder.tv_stadium_name.setTextColor(context.getResources().getColor(R.color.orange));
        } else {
            holder.radio.setChecked(false);
        }
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        public RadioButton radio;
        public TextView tv_stadium_name;

        public ViewHolder(final View itemView) {
            super(itemView);
            itemView.setClickable(true);
            radio = (RadioButton) itemView.findViewById(R.id.rb_stadiums);
            tv_stadium_name = (TextView) itemView.findViewById(R.id.tv_stadiums_name);
        }
    }
}
