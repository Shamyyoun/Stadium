package com.stadium.app.activities;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;

import com.stadium.app.R;

public abstract class MainActivity extends ParentActivity {

    private static int DRAWER_GRAVITY = Gravity.RIGHT;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        // customize the toolbar
        setToolbarIcon(R.drawable.menu_icon);

        // customize the drawer layout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    protected abstract int getContentView();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onMenuIcon();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onMenuIcon() {
        if (drawerLayout.isDrawerOpen(DRAWER_GRAVITY)) {
            closeMenuDrawer();
        } else {
            drawerLayout.openDrawer(DRAWER_GRAVITY);
        }
    }

    public void closeMenuDrawer() {
        drawerLayout.closeDrawer(DRAWER_GRAVITY);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(DRAWER_GRAVITY)) {
            closeMenuDrawer();
        } else {
            super.onBackPressed();
        }
    }
}