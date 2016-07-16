package com.stadium.player.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.stadium.player.R;

public class ParentToolbarActivity extends ParentActivity {
    private Toolbar toolbar;
    private TextView tvToolbarTitle;
    private int menuId;
    private boolean enableBack;
    private int iconResId;
    private String toolbarTitle;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // init the view_toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        // check the view_toolbar
        if (toolbar != null) {
            // view_toolbar is available >> handle it
            setSupportActionBar(toolbar);
            toolbar.setTitle("");
            tvToolbarTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_title);
            tvToolbarTitle.setText(getTitle());

            // check if enable back
            if (enableBack) {
                // set the back icon
                toolbar.setNavigationIcon(R.drawable.back_icon);
            } else if (iconResId != 0) {
                // set this icon
                toolbar.setNavigationIcon(iconResId);
            }
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (tvToolbarTitle != null) {
            tvToolbarTitle.setText(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        toolbarTitle = getString(titleId);
        if (tvToolbarTitle != null) {
            tvToolbarTitle.setText(titleId);
        }
    }

    public void hideToolbarTitle() {
        if (tvToolbarTitle != null) {
            tvToolbarTitle.setVisibility(View.GONE);
        }
    }

    public void showToolbarTitle() {
        if (tvToolbarTitle != null) {
            tvToolbarTitle.setVisibility(View.VISIBLE);
        }
    }

    public void createOptionsMenu(int menuId) {
        this.menuId = menuId;
        invalidateOptionsMenu();
    }

    public void removeOptionsMenu() {
        menuId = 0;
        invalidateOptionsMenu();
    }

    public void enableBackButton() {
        enableBack = true;
    }

    public void setToolbarIcon(int iconResId) {
        this.iconResId = iconResId;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menuId != 0) {
            getMenuInflater().inflate(menuId, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && enableBack) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}