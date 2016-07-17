package com.stadium.player.activities;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.stadium.player.R;

/**
 * Created by karam on 7/17/16.
 */
public class MyContactsActivity extends ParentToolbarActivity{

    private RecyclerView rvContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        enableBackButton();

        // customize the toolbar
        setToolbarIcon(R.drawable.menu_icon);
        setTitle(R.string.my_contacts);

    }
}
