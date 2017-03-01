package com.stormnology.stadium.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.activities.UpdateStadiumActivity;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.models.entities.Stadium;

/*
 * Created by karam on 8/10/16.
 */
public class AdminStadiumFragment extends StadiumInfoParentFragment {
    private TextView tvAddress;
    private TextView tvOpenMap;
    private TextView tvContactsInfo;

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
        tvOpenMap = (TextView) findViewById(R.id.tv_open_map);
        tvContactsInfo = (TextView) findViewById(R.id.tv_contacts_info);

        // add listeners
        tvOpenMap.setOnClickListener(this);
        tvContactsInfo.setOnClickListener(this);

        return rootView;
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.fragment_admin_stadium;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_open_map:
                onLocation();
                break;

            case R.id.tv_contacts_info:
                onContacts();
                break;

            default:
                super.onClick(v);
        }
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

        // check stadium location
        if (stadiumController.hasLocation(stadium)) {
            tvOpenMap.setVisibility(View.VISIBLE);
        } else {
            tvOpenMap.setVisibility(View.GONE);
        }

        // check stadium contacts info
        if (stadiumController.hasContactInfo(stadium)) {
            tvContactsInfo.setVisibility(View.VISIBLE);
        } else {
            tvContactsInfo.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            if (isControlsEnabled()) {
                openUpdateStadiumActivity();
            }

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void openUpdateStadiumActivity() {
        Intent intent = new Intent(activity, UpdateStadiumActivity.class);
        intent.putExtra(Const.KEY_STADIUM, stadium);
        startActivityForResult(intent, Const.REQ_UPDATE_STADIUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Const.REQ_UPDATE_STADIUM && resultCode == Activity.RESULT_OK) {
            // get new stadium object and update the ui
            stadium = (Stadium) data.getSerializableExtra(Const.KEY_STADIUM);
            updateStadiumUI();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void enableControls(boolean enable) {
        super.enableControls(enable);
        tvOpenMap.setEnabled(enable);
        tvContactsInfo.setEnabled(enable);
    }
}

