package com.stadium.app.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stadium.app.R;
import com.stadium.app.activities.ContactUsActivity;
import com.stadium.app.activities.LoginActivity;
import com.stadium.app.activities.MainActivity;
import com.stadium.app.adapters.MenuItemsAdapter;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.controllers.MenuItemController;
import com.stadium.app.interfaces.OnMenuItemClickListener;
import com.stadium.app.models.entities.MenuItem;
import com.stadium.app.models.enums.MenuItemType;
import com.stadium.app.utils.DialogUtils;

import java.util.List;

/**
 * Created by Shamyyoun on 5/28/16.
 */
public class SideMenuFragment extends ParentFragment implements OnMenuItemClickListener {
    private MainActivity activity;
    private RecyclerView rvItems;
    private List<MenuItem> menuItems;
    private MenuItemsAdapter itemsAdapter;

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

            case LOGOUT:
                onLogout();
                break;
        }

        closeMenuDrawer();
    }

    private void openContactUsActivity() {
        Intent intent = new Intent(activity, ContactUsActivity.class);
        startActivity(intent);
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
        // logout using active user controller
        ActiveUserController userController = new ActiveUserController(activity);
        userController.logout();

        // goto login activity
        Intent intent = new Intent(activity, LoginActivity.class);
        startActivity(intent);
        activity.finish();
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
}
