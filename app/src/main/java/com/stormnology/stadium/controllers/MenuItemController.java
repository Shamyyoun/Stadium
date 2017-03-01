package com.stormnology.stadium.controllers;

import android.content.Context;

import com.stormnology.stadium.R;
import com.stormnology.stadium.models.entities.MenuItem;
import com.stormnology.stadium.models.enums.MenuItemType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 6/4/16.
 */
public class MenuItemController {
    private Context context;

    public MenuItemController(Context context) {
        this.context = context;
    }

    public List<MenuItem> getSideMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();

        MenuItem contactUsItem = new MenuItem(MenuItemType.CONTACT_US, context.getString(R.string.contact_us), R.drawable.phone_icon);
        menuItems.add(contactUsItem);

        MenuItem settingsItem = new MenuItem(MenuItemType.CHANGE_PASSWORD, context.getString(R.string.change_password), R.drawable.settings_icon);
        menuItems.add(settingsItem);

        MenuItem logoutItem = new MenuItem(MenuItemType.LOGOUT, context.getString(R.string.logout), R.drawable.logout_icon);
        menuItems.add(logoutItem);

        return menuItems;
    }
}
