package com.stadium.app.controllers;

import android.content.Context;

import com.stadium.app.R;
import com.stadium.app.models.entities.MenuItem;
import com.stadium.app.models.enums.MenuItemType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 6/4/16.
 */
public class MenuItemController {
    public static List<MenuItem> getSideMenuItems(Context context) {
        List<MenuItem> menuItems = new ArrayList<>();

        MenuItem menuItem1 = new MenuItem(MenuItemType.CONTACT_US, context.getString(R.string.contact_us), R.drawable.phone_icon);
        menuItems.add(menuItem1);

        MenuItem menuItem2 = new MenuItem(MenuItemType.ABOUT_APP, context.getString(R.string.about_app), R.drawable.about_icon);
        menuItems.add(menuItem2);

        MenuItem menuItem3 = new MenuItem(MenuItemType.SETTINGS, context.getString(R.string.settings), R.drawable.settings_icon);
        menuItems.add(menuItem3);

        MenuItem menuItem4 = new MenuItem(MenuItemType.LOGOUT, context.getString(R.string.logout), R.drawable.logout_icon);
        menuItems.add(menuItem4);

        return menuItems;
    }
}
