package com.stadium.player.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.stadium.player.activities.ParentActivity;
import com.stadium.player.utils.Utils;

/**
 * Created by Shamyyoun on 5/28/16.
 */
public class ParentFragment extends Fragment implements View.OnClickListener {
    protected ParentActivity activity;
    protected View rootView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (ParentActivity) activity;
    }

    public View findViewById(int id) {
        if (rootView != null) {
            return rootView.findViewById(id);
        } else {
            return null;
        }
    }

    public int getResColor(int id) {
        return getResources().getColor(id);
    }

    public void logE(String msg) {
        Utils.logE(msg);
    }

    @Override
    public void onClick(View v) {
    }

    public void loadFragment(int container, Fragment fragment) {
        loadFragment(container, fragment, 0, 0);
    }

    public void loadFragment(int container, Fragment fragment, int enterAnim, int exitAnim) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        if (enterAnim != 0 && exitAnim != 0) {
            ft.setCustomAnimations(enterAnim, exitAnim);
        }
        ft.replace(container, fragment);
        ft.commitAllowingStateLoss();
    }
}
