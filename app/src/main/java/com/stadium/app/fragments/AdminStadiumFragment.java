package com.stadium.app.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.activities.UpdateStadiumActivity;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.dialogs.StadiumContactsDialog;
import com.stadium.app.models.entities.Stadium;
import com.stadium.app.utils.Utils;

/*
 * Created by karam on 8/10/16.
 */
public class AdminStadiumFragment extends StadiumInfoParentFragment {
    private TextView tvAddress;
    private TextView tvDescription;
    private TextView tvOpenMap;
    private TextView tvContactsInfo;
    private StadiumContactsDialog contactsDialog;

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
                onOpenMap();
                break;

            case R.id.tv_contacts_info:
                onContactsInfo();
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

        // set description
        String desc = stadium.getBio();
        if (!Utils.isNullOrEmpty(desc)) {
            tvDescription.setText(desc);
        } else {
            tvDescription.setText("-------- --------");
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

    private void onOpenMap() {
        // check if this stadium has location
        if (stadiumController.hasLocation(stadium)) {
            Utils.openMapIntent(activity, stadium.getName(), stadium.getLatitude(), stadium.getLongitude());
        } else {
            Utils.showShortToast(activity, R.string.no_location_info_available);
        }
    }

    private void onContactsInfo() {
        // check if this stadium has contacts info
        if (stadiumController.hasContactInfo(stadium)) {
            // open contacts dialog
            if (contactsDialog == null) {
                contactsDialog = new StadiumContactsDialog(activity, stadium);
            }
            contactsDialog.show();
        } else {
            Utils.showShortToast(activity, R.string.no_contacts_info_available);
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

