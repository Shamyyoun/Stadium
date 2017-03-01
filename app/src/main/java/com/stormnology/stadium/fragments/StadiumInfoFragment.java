package com.stormnology.stadium.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.stormnology.stadium.R;

/*
 * Created by karam on 8/10/16.
 */
public class StadiumInfoFragment extends StadiumInfoParentFragment {
    private ImageButton ibContacts;
    private ImageButton ibLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = super.onCreateView(inflater, container, savedInstanceState);

        // init views
        ibContacts = (ImageButton) findViewById(R.id.ib_contacts);
        ibLocation = (ImageButton) findViewById(R.id.ib_location);

        // add listeners
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

    @Override
    protected void enableControls(boolean enable) {
        super.enableControls(enable);
        ibContacts.setEnabled(enable);
        ibLocation.setEnabled(enable);
    }
}

