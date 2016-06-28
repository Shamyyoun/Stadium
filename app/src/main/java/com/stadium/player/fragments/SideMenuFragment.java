package com.stadium.player.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stadium.player.R;
import com.stadium.player.activities.MainActivity;
import com.stadium.player.adapters.MenuItemsAdapter;
import com.stadium.player.controllers.MenuItemController;
import com.stadium.player.interfaces.OnItemClickListener;
import com.stadium.player.models.entities.MenuItem;

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
        logE("MenuItem Clicked: " + position);
        closeMenuDrawer();
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
