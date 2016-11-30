package com.stadium.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.stadium.app.R;
import com.stadium.app.dialogs.StadiumBioDialog;
import com.stadium.app.dialogs.StadiumContactsDialog;
import com.stadium.app.utils.Utils;

/*
 * Created by karam on 8/10/16.
 */
public class StadiumInfoFragment extends StadiumInfoParentFragment {
    private ImageButton ibContacts;
    private ImageButton ibLocation;
    private StadiumContactsDialog contactsDialog;
    private StadiumBioDialog bioDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = super.onCreateView(inflater, container, savedInstanceState);

        // init views
        ibContacts = (ImageButton) findViewById(R.id.ib_contacts);
        ibLocation = (ImageButton) findViewById(R.id.ib_location);

        // add listeners
        tvName.setOnClickListener(this);
        ibContacts.setOnClickListener(this);
        ibLocation.setOnClickListener(this);

        return rootView;
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.fragment_stadium_info;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_name:
                onName();
                break;

            case R.id.ib_contacts:
                onContacts();
                break;

            case R.id.ib_location:
                onLocation();
                break;

            default:
                super.onClick(v);
        }
    }

    private void onName() {
        // check if this stadium has bio
        if (!Utils.isNullOrEmpty(stadium.getBio())) {
            // open bio dialog
            if (bioDialog == null) {
                bioDialog = new StadiumBioDialog(activity, stadium);
            }
            bioDialog.show();
        } else {
            Utils.showShortToast(activity, R.string.no_bio_available);
        }
    }

    private void onContacts() {
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

    private void onLocation() {
        // check if this stadium has location
        if (stadiumController.hasLocation(stadium)) {
            Utils.openMapIntent(activity, stadium.getName(), stadium.getLatitude(), stadium.getLongitude());
        } else {
            Utils.showShortToast(activity, R.string.no_location_info_available);
        }
    }

    @Override
    protected void enableControls(boolean enable) {
        super.enableControls(enable);
        ibContacts.setEnabled(enable);
        ibLocation.setEnabled(enable);
    }
}

