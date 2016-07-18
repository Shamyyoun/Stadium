package com.stadium.player.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stadium.player.R;
import com.stadium.player.dialogs.OrderStadiumsDialog;

/**
 * Created by Shamyyoun on 7/2/16.
 */
public class StadiumsFragment extends ParentFragment {
    private TextView tvOrderBy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stadiums, container, false);

        // init views
        tvOrderBy = (TextView) findViewById(R.id.tv_order_by);

        // add click listeners
        tvOrderBy.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_order_by) {
            // show order stadiums dialog
            OrderStadiumsDialog dialog = new OrderStadiumsDialog(activity);
            dialog.show();
        } else {
            super.onClick(v);
        }
    }
}
