package com.stadium.app.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stadium.app.R;
import com.stadium.app.adapters.StadiumsAdapter;
import com.stadium.app.dialogs.OrderStadiumsDialog;
import com.stadium.app.models.entities.Stadium;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 7/2/16.
 */
public class StadiumsFragment extends ParentToolbarFragment {
    private TextView tvOrderBy;
    private RecyclerView recyclerView;
    private StadiumsAdapter adapter;
    private List<Stadium> data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.stadiums);
        createOptionsMenu(R.menu.menu_stadiums);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stadiums, container, false);

        // init views
        tvOrderBy = (TextView) findViewById(R.id.tv_order_by);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // customize the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        data = getDummyData();
        adapter = new StadiumsAdapter(activity, data, R.layout.item_stadium);
        recyclerView.setAdapter(adapter);

        // add click listeners
        tvOrderBy.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_order_by) {
            // show order stadiums dialog
            OrderStadiumsDialog dialog = new OrderStadiumsDialog(activity);
            dialog.show();
        } else {
            super.onClick(v);
        }
    }

    private List<Stadium> getDummyData() {
        List<Stadium> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Stadium stadium = new Stadium();
            data.add(stadium);
        }

        return data;
    }
}
