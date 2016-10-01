package com.stadium.app.dialogs;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.stadium.app.R;
import com.stadium.app.adapters.RadioButtonsAdapter;
import com.stadium.app.models.entities.Stadium;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 6/28/16.
 */
public class StadiumsDialog extends ParentDialog {
    private RecyclerView recyclerView;
    private RadioButtonsAdapter adapter;
    private List<Stadium> data;

    public StadiumsDialog(final Context context) {
        super(context);
        setContentView(R.layout.dialog_stadiums);
        setTitle(R.string.favorite_stadium);

        // init views
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // customize the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        data = getDummyData();
        adapter = new RadioButtonsAdapter(context, data, R.layout.item_radio_button);
        recyclerView.setAdapter(adapter);
    }

    private List<Stadium> getDummyData() {
        List<Stadium> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Stadium item = new Stadium();
            item.setName("الملعب " + i);
            data.add(item);
        }

        return data;
    }
}