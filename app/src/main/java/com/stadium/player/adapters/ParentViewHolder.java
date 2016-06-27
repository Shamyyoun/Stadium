package com.stadium.player.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.stadium.player.interfaces.OnItemClickListener;

/**
 * Created by Shamyyoun on 5/11/16.
 */
public class ParentViewHolder extends RecyclerView.ViewHolder {
    public ParentViewHolder(final View itemView) {
        super(itemView);
    }

    public void setOnItemClickListener(final OnItemClickListener itemClickListener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, getPosition());
                }
            }
        });
    }
}
