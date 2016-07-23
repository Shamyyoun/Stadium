package com.stadium.player.fragments;

import android.app.Activity;
import android.os.Bundle;

import com.stadium.player.activities.ParentToolbarActivity;

/**
 * Created by Shamyyoun on 7/23/16.
 */
public class ParentToolbarFragment extends ParentFragment {
    protected ParentToolbarActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (ParentToolbarActivity) activity;
    }

    public void setTitle(CharSequence title) {
        if (activity != null) {
            activity.setTitle(title);
        }
    }

    public void setTitle(int titleId) {
        if (activity != null) {
            activity.setTitle(titleId);
        }
    }

    public void createOptionsMenu(int menuId) {
        if (activity != null) {
            activity.createOptionsMenu(menuId);
        }
    }

    public void removeOptionsMenu() {
        if (activity != null) {
            activity.removeOptionsMenu();
        }
    }
}
