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
import com.stadium.app.activities.LoginActivity;
import com.stadium.app.activities.MainActivity;
import com.stadium.app.adapters.MenuItemsAdapter;
import com.stadium.app.controllers.MenuItemController;
import com.stadium.app.controllers.UserController;
import com.stadium.app.interfaces.OnItemClickListener;
import com.stadium.app.models.entities.MenuItem;
import com.stadium.app.utils.DialogUtils;

import java.util.List;

/**
 * Created by Shamyyoun on 5/28/16.
 */
public class SideMenuFragment extends ParentFragment implements OnItemClickListener {
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
        menuItems = MenuItemController.getSideMenuItems(activity);
        itemsAdapter = new MenuItemsAdapter(activity, menuItems, R.layout.item_menu_item);
        rvItems.setAdapter(itemsAdapter);
        itemsAdapter.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case 0:
                break;

            case 1:
                break;

            case 2:
                break;

            case 3:
                logout();
                break;
        }

        closeMenuDrawer();
    }

    private void logout() {
        DialogUtils.showConfirmDialog(activity, R.string.logout_q, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // remove current user
                UserController userController = new UserController(activity);
                userController.removeCurrent();

                // goto login activity
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
                activity.finish();
            }
        }, null);
    }

    private void closeMenuDrawer() {
        // close the menu drawer delayed to prevent animations confusing
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.closeMenuDrawer();
            }
        }, 100);
    }
}
