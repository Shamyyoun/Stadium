package com.stadium.player.activities;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;

import com.stadium.player.R;

public class MainActivity extends ParentToolbarActivity {
    private static int DRAWER_GRAVITY = Gravity.RIGHT;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // customize the drawer layout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

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
}