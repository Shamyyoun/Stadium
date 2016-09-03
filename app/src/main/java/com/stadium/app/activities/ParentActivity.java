package com.stadium.app.activities;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.stadium.app.R;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.connection.ConnectionListener;
import com.stadium.app.utils.DialogUtils;
import com.stadium.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ParentActivity extends AppCompatActivity implements View.OnClickListener, ConnectionListener {
    // used to hold connection handlers that should be cancelled when destroyed
    private final List<ConnectionHandler> connectionHandlers = new ArrayList();
    protected AppCompatActivity activity;
    private FrameLayout rootView;
    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activity = this;
        rootView = (FrameLayout) findViewById(android.R.id.content);
    }

    public void logE(String msg) {
        Utils.logE(msg);
    }

    public void cancelWhenDestroyed(ConnectionHandler connectionHandler) {
        connectionHandlers.add(connectionHandler);
    }

    @Override
    protected void onDestroy() {
        // cancel requests if found
        for (ConnectionHandler connectionHandler : connectionHandlers) {
            connectionHandler.cancel(true);
        }

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        logE("onClick has been invoked from ParentActivity");
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        hideProgressDialog();
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        hideProgressDialog();
        Utils.showShortToast(this, R.string.something_went_wrong_please_try_again);
    }

    public void showProgressDialog() {
        progressDialog = DialogUtils.showProgressDialog(this, R.string.please_wait_dotted);
    }

    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public int getResColor(int id) {
        return getResources().getColor(id);
    }

    public void loadFragment(int container, Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(container, fragment)
                .commitAllowingStateLoss();
    }

    public void hideKeyboard() {
        if (rootView != null) {
            Utils.hideKeyboard(rootView);
        }
    }
}