package com.stadium.app.dialogs;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.stadium.app.R;
import com.stadium.app.adapters.AttendanceAdapter;
import com.stadium.app.models.entities.Attendant;
import com.stadium.app.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 6/28/16.
 */
public class AttendanceDialog extends ParentDialog {
    private RecyclerView recyclerView;
    private AttendanceAdapter adapter;
    private List<Attendant> data;

    public AttendanceDialog(final Context context) {
        super(context);
        setContentView(R.layout.dialog_attendance);
        setTitle(R.string.confirm_attendance);

        // init views
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // customize the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        data = getDummyData();
        adapter = new AttendanceAdapter(context, data, R.layout.item_attendance);
        recyclerView.setAdapter(adapter);
    }

    private List<Attendant> getDummyData() {
        List<Attendant> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Attendant item = new Attendant();
            data.add(item);
        }

        return data;
    }
}