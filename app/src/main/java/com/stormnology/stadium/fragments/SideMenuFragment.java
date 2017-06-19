package com.stormnology.stadium.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stormnology.stadium.R;
import com.stormnology.stadium.activities.ContactUsActivity;
import com.stormnology.stadium.activities.LoginActivity;
import com.stormnology.stadium.activities.MainActivity;
import com.stormnology.stadium.adapters.MenuItemsAdapter;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.controllers.MenuItemController;
import com.stormnology.stadium.controllers.ParseController;
import com.stormnology.stadium.dialogs.ChangePasswordDialog;
import com.stormnology.stadium.interfaces.OnMenuItemClickListener;
import com.stormnology.stadium.models.entities.MenuItem;
import com.stormnology.stadium.models.enums.MenuItemType;
import com.stormnology.stadium.utils.AppUtils;
import com.stormnology.stadium.utils.DialogUtils;
import com.stormnology.stadium.utils.Utils;

import java.util.List;

/**
 * Created by Shamyyoun on 5/28/16.
 */
public class SideMenuFragment extends ParentFragment implements OnMenuItemClickListener {
    private MainActivity activity;
    private RecyclerView rvItems;
    private List<MenuItem> menuItems;
    private MenuItemsAdapter itemsAdapter;

    private LogoutTask logoutTask;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_side_menu, container, false);

        // customize the recycler view
        rvItems = (RecyclerView) findViewById(R.id.rv_menu_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rvItems.setLayoutManager(layoutManager);

        // get the menu items
        MenuItemController menuItemController = new MenuItemController(activity);
        menuItems = menuItemController.getSideMenuItems();

        // set the adapter
        itemsAdapter = new MenuItemsAdapter(activity, menuItems, R.layout.item_menu_item);
        rvItems.setAdapter(itemsAdapter);
        itemsAdapter.setOnMenuItemClickListener(this);

        return rootView;
    }

    @Override
    public void onItemClick(MenuItemType menuItemType) {
        switch (menuItemType) {
            case CONTACT_US:
                openContactUsActivity();
                break;

            case CHANGE_PASSWORD:
                showChangePasswordDialog();
                break;

            case SHARE_APP:
                onShareApp();
                break;

            case LOGOUT:
                onLogout();
                break;
        }

        closeMenuDrawer();
    }

    private void closeMenuDrawer() {
        // close the menu drawer delayed to prevent animations confusing
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.closeMenuDrawer();
            }
        }, 200);
    }

    private void openContactUsActivity() {
        Intent intent = new Intent(activity, ContactUsActivity.class);
        startActivity(intent);
    }

    private void showChangePasswordDialog() {
        ChangePasswordDialog dialog = new ChangePasswordDialog(activity);
        dialog.show();
    }

    private void onShareApp() {
        // get the text and share it
        String text = AppUtils.getShareAppText(activity);
        Utils.shareText(activity, text);
    }

    private void onLogout() {
        DialogUtils.showConfirmDialog(activity, R.string.logout_q, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        }, null);
    }

    private void logout() {
        // execute new logout task
        logoutTask = new LogoutTask();
        logoutTask.execute();
    }

    private class LogoutTask extends AsyncTask<Void, Void, Boolean> {
        ActiveUserController activeUserController;
        ParseController parseController;

        public LogoutTask() {
            activeUserController = new ActiveUserController(activity);
            parseController = new ParseController(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // check internet connection
            if (!Utils.hasConnection(activity)) {
                Utils.showShortToast(activity, R.string.no_internet_connection);
                cancel(true);
                return;
            }

            showProgressDialog();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return parseController.logOut();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            hideProgressDialog();

            if (result != null && result.booleanValue()) {
                // logout from user controller
                activeUserController.logout();

                // goto login activity
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
                activity.finish();
            } else {
                // show msg
                Utils.showShortToast(activity, R.string.failed_logging_out);
            }
        }
    }

    private void cancelLogoutTask() {
        if (logoutTask != null) {
            logoutTask.cancel(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelLogoutTask();
    }
}
