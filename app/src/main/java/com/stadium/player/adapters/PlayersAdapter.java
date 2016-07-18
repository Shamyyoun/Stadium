package com.stadium.player.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.stadium.player.R;
import com.stadium.player.models.entities.Players;

import java.util.List;

/**
 * Created by karam on 7/18/16.
 */
public class PlayersAdapter  extends ParentRecyclerAdapter<Players> {

    public PlayersAdapter(Context context, List<Players> data, int layoutId) {
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
        final Players item = data.get(position);

    }

    class ViewHolder extends ParentRecyclerViewHolder {

        RoundedImageView ivImage;
        ImageView ivAdd;
        TextView tvName;
        TextView tvPosition;
        TextView tvAddress;
        RatingBar rbPlayer;

        public ViewHolder(final View itemView) {
            super(itemView);

            ivImage = (RoundedImageView) itemView.findViewById(R.id.iv_player);
            ivAdd = (ImageView) itemView.findViewById(R.id.iv_add);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvPosition = (TextView) itemView.findViewById(R.id.tv_position);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            rbPlayer = (RatingBar) itemView.findViewById(R.id.rb_player);



        }
    }
}
