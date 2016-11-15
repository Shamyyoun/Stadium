package com.stadium.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.interfaces.OnCheckableCheckedListener;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karam on 7/17/16.
 */
public class ReservationPlayersAdapter extends ParentRecyclerAdapter<User> {
    private OnCheckableCheckedListener itemCheckedListener;

    public ReservationPlayersAdapter(Context context, List<User> data, int layoutId) {
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

        // load the image as it is shared among all views
        final User item = data.get(position);
        Utils.loadImage(context, item.getImageLink(), R.drawable.default_image, holder.ivImage);

        // check id
        if (item.getId() == Const.DEFAULT_ITEM_ID) {
            // check if checked
            if (isAllChecked()) {
                // all selected
                holder.tvName.setText(R.string.all_selected);
                holder.tvName.setTextColor(context.getResources().getColor(R.color.green));
                holder.ivImage.setImageResource(R.drawable.green_ok);
            } else {
                holder.tvName.setText(R.string.select_all);
                holder.tvName.setTextColor(context.getResources().getColor(R.color.dark_gray));
                holder.ivImage.setImageResource(R.drawable.gray_ok);
            }
            holder.ivConfirmStatus.setVisibility(View.GONE);
        } else {
            // set data
            holder.tvName.setText(item.getName());
            Utils.loadImage(context, item.getImageLink(), R.drawable.default_image, holder.ivImage);
            holder.ivConfirmStatus.setVisibility(item.isChecked() ? View.VISIBLE : View.GONE);
        }

        // add click listeners
        holder.layoutContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check id
                if (item.getId() == Const.DEFAULT_ITEM_ID) {
                    // check if checked
                    if (isAllChecked()) {
                        // all checked, so uncheck all
                        checkAll(false);
                    } else {
                        // not all checked, so check all
                        checkAll(true);
                    }
                } else {
                    if (item.isChecked()) {
                        // uncheck it and the default item
                        item.setChecked(false);
                    } else {
                        // check it and the default item
                        item.setChecked(true);
                    }
                }

                // notify the adapter
                notifyDataSetChanged();

                // fire the listener if possible
                if (itemCheckedListener != null) {
                    itemCheckedListener.onCheckableChecked(item, item.isChecked());
                }
            }
        });
    }

    private boolean isAllChecked() {
        if (data.size() > 1) {
            for (int i = 1; i < data.size(); i++) {
                User user = data.get(i);
                if (!user.isChecked()) {
                    return false;
                }
            }
        } else {
            return false;
        }

        return true;
    }

    private void checkAll(boolean check) {
        if (data.size() > 1) {
            for (int i = 0; i < data.size(); i++) {
                User user = data.get(i);
                user.setChecked(check);
            }
        }
    }

    public List<User> getCheckedItems() {
        List<User> checkedItems = new ArrayList<>();
        if (data.size() > 1) {
            for (int i = 1; i < data.size(); i++) {
                User user = data.get(i);
                if (user.isChecked()) {
                    checkedItems.add(user);
                }
            }
        }

        return checkedItems;
    }

    public int getCheckedItemsCount() {
        int count = 0;
        if (data.size() > 1) {
            for (int i = 1; i < data.size(); i++) {
                User user = data.get(i);
                if (user.isChecked()) {
                    count++;
                }
            }
        }

        return count;
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        private View layoutContent;
        private ImageView ivImage;
        private TextView tvName;
        private ImageView ivConfirmStatus;

        public ViewHolder(final View itemView) {
            super(itemView);

            layoutContent = findViewById(R.id.layout_content);
            ivImage = (ImageView) findViewById(R.id.iv_image);
            tvName = (TextView) findViewById(R.id.tv_name);
            ivConfirmStatus = (ImageView) findViewById(R.id.iv_confirm_status);
        }
    }

    public void setOnItemCheckedListener(OnCheckableCheckedListener itemCheckedListener) {
        this.itemCheckedListener = itemCheckedListener;
    }
}
