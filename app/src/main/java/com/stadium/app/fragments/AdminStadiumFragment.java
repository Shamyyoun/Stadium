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
import com.stadium.app.models.entities.Stadium;
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
}

