package com.stadium.player.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.stadium.player.R;
import com.stadium.player.adapters.ContactsAdapter;
import com.stadium.player.models.entities.Contacts;
import java.util.List;
/**
 * Created by karam on 7/17/16.
 */
public class MyContactsActivity extends ParentToolbarActivity {

    private RecyclerView rvContacts;
    private ContactsAdapter contactsAdapter;
    private List<Contacts> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        enableBackButton();

        rvContacts = (RecyclerView) findViewById(R.id.rv_contact);

        // customize the toolbar
        setToolbarIcon(R.drawable.menu_icon);
        setTitle(R.string.my_contacts);

        // set the recyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvContacts.setLayoutManager(layoutManager);
        contactsAdapter = new ContactsAdapter(this, data, R.layout.item_contacts);
        rvContacts.setAdapter(contactsAdapter);


    }
}
