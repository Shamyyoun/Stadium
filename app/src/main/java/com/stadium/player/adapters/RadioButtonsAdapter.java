package com.stadium.player.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.stadium.player.R;
import com.stadium.player.models.Checkable;

import java.util.List;

/**
 * Created by Shamyyoun on 19/2/16.
 */
public class RadioButtonsAdapter<T extends Checkable> extends ParentRecyclerAdapter<Checkable> {
    private int selectedItemPosition;

    public RadioButtonsAdapter(Context context, List<Checkable> data, int layoutId) {
        super(context, data, layoutId);
    }

    public RadioButtonsAdapter(Context context, Checkable[] data, int layoutId) {
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
        final Checkable item = data.get(position);
        holder.radioButton.setText(item.toString());
        holder.radioButton.setChecked(item.isChecked());
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        private RadioButton radioButton;

        public ViewHolder(final View itemView) {
            super(itemView);

            radioButton = (RadioButton) itemView.findViewById(R.id.radio_button);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // deselect the selected item
                    data.get(selectedItemPosition).setChecked(false);
                    notifyItemChanged(selectedItemPosition);

                    // select the desired item
                    data.get(getPosition()).setChecked(true);
                    notifyItemChanged(getPosition());
                    selectedItemPosition = getPosition();
                }
            });
        }
    }

    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }
}
