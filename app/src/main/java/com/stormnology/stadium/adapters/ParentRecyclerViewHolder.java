package com.stormnology.stadium.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.stormnology.stadium.interfaces.OnItemClickListener;
import com.stormnology.stadium.utils.Utils;

/**
 * Created by Shamyyoun on 5/11/16.
 */
public class ParentRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private View clickableRootView; // this is used to change the default onItemClickListener

    public ParentRecyclerViewHolder(final View itemView) {
        super(itemView);
    }

    public void setOnItemClickListener(final OnItemClickListener itemClickListener) {
        if (clickableRootView != null) {
            clickableRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(v, getPosition());
                    }
                }
            });
        } else {
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

    public void setClickableRootView(View clickableRootView) {
        this.clickableRootView = clickableRootView;
    }

    public View findViewById(int viewId) {
        if (itemView != null) {
            return itemView.findViewById(viewId);
        } else {
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        logE("onClick has been invoked from ParentViewHolder");
    }

    public void logE(String msg) {
        Utils.logE(msg);
    }
}
