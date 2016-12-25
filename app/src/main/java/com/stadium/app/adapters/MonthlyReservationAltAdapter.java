package com.stadium.app.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.models.entities.Reservation;
import com.stadium.app.utils.DateUtils;

import java.util.List;

/**
 * Created by Shamyyoun on 12/25/16.
 */

public class MonthlyReservationAltAdapter extends ParentListAdapter<Reservation> {

    public MonthlyReservationAltAdapter(Context context, List<Reservation> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public View getView(int position, View itemView, ViewGroup parent) {
        final ViewHolder holder;
        if (itemView == null) {
            itemView = layoutInflater.inflate(layoutId, null);
            holder = new ViewHolder();

            holder.tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            holder.tvFieldNo = (TextView) itemView.findViewById(R.id.tv_field_no);

            itemView.setTag(holder);
        } else {
            holder = (ViewHolder) itemView.getTag();
        }

        // set data
        final Reservation item = data.get(position);
        holder.tvFieldNo.setText(item.getField().getFieldNumber());
        String dateStr = DateUtils.formatDate(item.getDate(), Const.SER_DATE_FORMAT, "d-M-yyyy");
        holder.tvDate.setText(dateStr);

        return itemView;
    }

    static class ViewHolder extends ParentListViewHolder {
        private TextView tvDate;
        private TextView tvFieldNo;
    }
}
