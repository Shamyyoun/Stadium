package com.stadium.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stadium.app.R;
import com.stadium.app.controllers.ReservationController;
import com.stadium.app.controllers.StadiumController;
import com.stadium.app.models.entities.Reservation;
import com.stadium.app.models.entities.Stadium;
import com.stadium.app.utils.Utils;

import java.util.List;

/**
 * Created by Shamyyoun on 19/2/16.
 */
public class ReservationsAdapter extends ParentRecyclerAdapter<Reservation> {
    private StadiumController stadiumController;
    private ReservationController reservationController;

    public ReservationsAdapter(Context context, List<Reservation> data, int layoutId) {
        super(context, data, layoutId);

        // create controllers
        stadiumController = new StadiumController();
        reservationController = new ReservationController();
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

        // get item
        final Reservation item = data.get(position);

        // check stadium
        final Stadium stadium = item.getReservationStadium();
        if (stadium != null) {
            // set stadium name
            if (!Utils.isNullOrEmpty(stadium.getName())) {
                holder.tvStadiumName.setText(stadium.getName());
            } else {
                holder.tvStadiumName.setText("-----------");
            }

            // set stadium address
            String address = stadiumController.getAddress(stadium);
            if (address != null) {
                holder.tvStadiumAddress.setText(getString(R.string.address_c) + " " + address);
                holder.tvStadiumAddress.setVisibility(View.VISIBLE);
            } else {
                holder.tvStadiumAddress.setVisibility(View.GONE);
            }

            // load image
            Utils.loadImage(context, stadium.getImageLink(), R.drawable.default_image, holder.ivImage);
        } else {
            // hide stadium views
            holder.tvStadiumName.setVisibility(View.GONE);
            holder.tvStadiumAddress.setVisibility(View.GONE);

            // load def image
            holder.ivImage.setImageResource(R.drawable.default_image);
        }

        // set the date
        String dateTime = getString(R.string.appointment_c) + " " + reservationController.getDateTime(item);
        holder.tvDateTime.setText(dateTime);

        // check to add address click listener
        if (stadium != null && stadiumController.hasLocation(stadium)) {
            holder.tvStadiumAddress.setClickable(true);
            holder.tvStadiumAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.openMapIntent(context, stadium.getName(), stadium.getLatitude(), stadium.getLongitude());
                }
            });
        } else {
            holder.tvStadiumAddress.setClickable(false);
        }
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        private ImageView ivImage;
        private TextView tvStadiumName;
        private TextView tvStadiumAddress;
        private TextView tvDateTime;

        public ViewHolder(final View itemView) {
            super(itemView);

            ivImage = (ImageView) findViewById(R.id.iv_image);
            tvStadiumName = (TextView) findViewById(R.id.tv_stadium_name);
            tvStadiumAddress = (TextView) findViewById(R.id.tv_stadium_address);
            tvDateTime = (TextView) findViewById(R.id.tv_date_time);
        }
    }
}
