package com.stadium.player.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.stadium.player.R;
import com.stadium.player.models.entities.Contacts;
import com.stadium.player.models.entities.Team;
import com.stadium.player.utils.Utils;

import java.util.List;

/**
 * Created by karam on 7/17/16.
 */
public class ContactsAdapter  extends ParentRecyclerAdapter<Contacts> {

    public ContactsAdapter(Context context, List<Contacts> data, int layoutId) {
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
        final Contacts item = data.get(position);

    }

    class ViewHolder extends ParentRecyclerViewHolder {
        RoundedImageView ivImage;
        RoundedImageView ivAdd;
        TextView tvName;
        TextView tvPosition;
        TextView tvPhone;

        public ViewHolder(final View itemView) {
            super(itemView);

            ivImage = (RoundedImageView) itemView.findViewById(R.id.iv_contact);
            ivAdd = (RoundedImageView) itemView.findViewById(R.id.iv_add);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvPosition = (TextView) itemView.findViewById(R.id.tv_position);
            tvPhone = (TextView) itemView.findViewById(R.id.tv_phone);


        }
    }
}
