package com.stadium.player.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stadium.player.R;
import com.stadium.player.models.entities.Notification;

import java.util.List;

/**
 * Created by Shamyyoun on 7/2/16.
 */
public class NotificationsAdapter extends ParentListAdapter<Notification> {

    public NotificationsAdapter(Context context, int layoutId, List<Notification> data) {
        super(context, layoutId, data);
    }

    @Override
    public View getView(int position, View itemView, ViewGroup parent) {
        ViewHolder holder = null;
        if (itemView == null) {
            // create the view holder
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            itemView = inflater.inflate(layoutId, parent, false);
            holder = new ViewHolder();

            // init views
            holder.tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            itemView.setTag(holder);
        } else {
            holder = (ViewHolder) itemView.getTag();
        }

        final Notification item = data.get(position);

        // set data
//        holder.tvTitle.setText(mobileNumber);

        return itemView;
    }

    static class ViewHolder extends ParentListViewHolder {
        TextView tvTitle;
    }
}