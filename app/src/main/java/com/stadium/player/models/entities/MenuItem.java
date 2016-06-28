package com.stadium.player.models.entities;

import com.stadium.player.models.enums.MenuItemType;

/**
 * Created by Shamyyoun on 6/4/16.
 */
public class MenuItem {
    private MenuItemType type;
    private String title;
    private int iconResId;

    public MenuItem(MenuItemType type, String title, int iconResId) {
        this.type = type;
        this.title = title;
        this.iconResId = iconResId;
    }

    public MenuItemType getType() {
        return type;
    }

    public void setType(MenuItemType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }
}
