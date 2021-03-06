package com.stormnology.stadium.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.stormnology.stadium.R;
import com.stormnology.stadium.models.Checkable;

import java.util.List;

/**
 * Created by Shamyyoun on 19/2/16.
 */
public class RadioButtonsAdapter extends ParentRecyclerAdapter<Checkable> {
    private int selectedItemPosition;

    public RadioButtonsAdapter(Context context, List<? extends Checkable> data, int layoutId) {
        super(context, (List<Checkable>) data, layoutId);
        setSelectedItem(0); // select the first item by default
    }

    public RadioButtonsAdapter(Context context, Checkable[] data, int layoutId) {
        super(context, data, layoutId);
        setSelectedItem(0); // select the first item by default
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
        final Checkable item = data.get(position);
        holder.radioButton.setText(item.toString());
        holder.radioButton.setChecked(item.isChecked());
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        private RadioButton radioButton;

        public ViewHolder(final View itemView) {
            super(itemView);

            radioButton = (RadioButton) findViewById(R.id.radio_button);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelectedItem(getPosition());
                }
            });
        }
    }

    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }

    public void setSelectedItem(int position) {
        // deselect the selected item
        data.get(selectedItemPosition).setChecked(false);
        notifyItemChanged(selectedItemPosition);

        // select the desired item
        data.get(position).setChecked(true);
        notifyItemChanged(position);
        selectedItemPosition = position;
    }
}
