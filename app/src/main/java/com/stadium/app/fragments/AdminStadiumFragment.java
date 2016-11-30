package com.stadium.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stadium.app.R;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.utils.Utils;

/*
 * Created by karam on 8/10/16.
 */
public class AdminStadiumFragment extends StadiumInfoParentFragment {
    private TextView tvAddress;
    private TextView tvDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set id with admin stadium id
        ActiveUserController userController = new ActiveUserController(activity);
        id = userController.getUser().getAdminStadium().getId();

        // set admin stadium fragment flag
        setAdminStadiumScreen(true);

        // customize toolbar
        setTitle(R.string.stade);
        createOptionsMenu(R.menu.menu_admin_stadium);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = super.onCreateView(inflater, container, savedInstanceState);

        // init views
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvDescription = (TextView) findViewById(R.id.tv_desc);

        return rootView;
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.fragment_admin_stadium;
    }

    @Override
    protected void updateStadiumUI() {
        super.updateStadiumUI();

        // set address
        String address = stadiumController.getAddress(stadium);
        if (address == null) {
            tvAddress.setVisibility(View.GONE);
        } else {
            tvAddress.setText(address);
            tvAddress.setVisibility(View.VISIBLE);
        }

        // set description
        String desc = stadium.getBio();
        if (!Utils.isNullOrEmpty(desc)) {
            tvDescription.setText(desc);
        } else {
            tvDescription.setText("-------- --------");
        }
    }
}

