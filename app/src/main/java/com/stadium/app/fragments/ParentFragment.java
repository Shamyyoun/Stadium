package com.stadium.app.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.stadium.app.R;
import com.stadium.app.activities.ParentActivity;
import com.stadium.app.connection.ConnectionListener;
import com.stadium.app.utils.DialogUtils;
import com.stadium.app.utils.Utils;

/**
 * Created by Shamyyoun on 5/28/16.
 */
public class ParentFragment extends Fragment implements View.OnClickListener, ConnectionListener {
    protected ParentActivity activity;
    protected View rootView;
    protected ProgressDialog progressDialog;

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

    public void showProgressDialog() {
        progressDialog = DialogUtils.showProgressDialog(activity, R.string.please_wait_dotted);
    }

    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        hideProgressDialog();
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        hideProgressDialog();
        Utils.showShortToast(activity, R.string.something_went_wrong_please_try_again);
    }
}
