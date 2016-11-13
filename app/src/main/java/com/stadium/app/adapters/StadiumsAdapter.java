package com.stadium.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.activities.StadiumInfoActivity;
import com.stadium.app.controllers.StadiumController;
import com.stadium.app.models.entities.Stadium;
import com.stadium.app.utils.Utils;

import java.util.List;

/**
 * Created by Shamyyoun on 7/2/16.
 */
public class StadiumsAdapter extends ParentRecyclerAdapter<Stadium> {

    public StadiumsAdapter(Context context, List<Stadium> data, int layoutId) {
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

        // get objects
        final Stadium item = data.get(position);
        final StadiumController controller = new StadiumController();

        // set basic data
        holder.tvTitle.setText(item.getName());
        holder.rbRating.setRating(item.getRate());
        String capacity = context.getString(R.string.has_d_stadiums, item.getFieldsCount());
        holder.tvCapacity.setText(capacity);

        // set address if possible
        String address = controller.getAddress(item);
        if (address != null) {
            holder.tvAddress.setText(getString(R.string.address_c) + " " + address);
            holder.tvAddress.setVisibility(View.VISIBLE);
        } else {
            holder.tvAddress.setVisibility(View.GONE);
        }

        // load the photo
        Utils.loadImage(context, item.getImageLink(), R.drawable.default_image, holder.ivPhoto);

        // set the contact info
        if (controller.hasContactInfo(item)) {
            holder.layoutContactInfo.setVisibility(View.VISIBLE);
            holder.viewContactInfoDivider.setVisibility(View.VISIBLE);

            // set phone if possible
            if (!Utils.isNullOrEmpty(item.getPhoneNumber())) {
                holder.btnPhone.setVisibility(View.VISIBLE);
                holder.btnPhone.setText(item.getPhoneNumber());
            } else {
                holder.btnPhone.setVisibility(View.GONE);
                holder.viewContactInfoDivider.setVisibility(View.GONE);
            }

            // set email if possible
            if (!Utils.isNullOrEmpty(item.getEmail())) {
                holder.btnEmail.setVisibility(View.VISIBLE);
                holder.btnEmail.setText(item.getEmail());
            } else {
                holder.btnEmail.setVisibility(View.GONE);
                holder.viewContactInfoDivider.setVisibility(View.GONE);
            }
        } else {
            holder.layoutContactInfo.setVisibility(View.GONE);
        }

        // add listeners
        holder.layoutContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStadiumInfoActivity(position);
            }
        });
        holder.btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openPhoneIntent(context, item.getPhoneNumber());
            }
        });
        holder.btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openEmailIntent(context, item.getEmail());
            }
        });
    }

    private void openStadiumInfoActivity(int position) {
        Stadium stadium = data.get(position);
        Intent intent = new Intent(context, StadiumInfoActivity.class);
        intent.putExtra(Const.KEY_ID, stadium.getId());
        context.startActivity(intent);
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        private View layoutContent;
        private ImageView ivPhoto;
        private TextView tvTitle;
        private TextView tvAddress;
        private TextView tvCapacity;
        private RatingBar rbRating;
        private View layoutContactInfo;
        private View viewContactInfoDivider;
        private Button btnPhone;
        private Button btnEmail;

        public ViewHolder(final View itemView) {
            super(itemView);

            // init views
            layoutContent = findViewById(R.id.layout_content);
            ivPhoto = (ImageView) findViewById(R.id.iv_photo);
            tvTitle = (TextView) findViewById(R.id.tv_title);
            tvAddress = (TextView) findViewById(R.id.tv_address);
            tvCapacity = (TextView) findViewById(R.id.tv_capacity);
            rbRating = (RatingBar) findViewById(R.id.rb_rating);
            layoutContactInfo = findViewById(R.id.layout_contact_info);
            viewContactInfoDivider = findViewById(R.id.view_contact_info_divider);
            btnPhone = (Button) findViewById(R.id.btn_phone);
            btnEmail = (Button) findViewById(R.id.btn_email);
        }
    }
}
