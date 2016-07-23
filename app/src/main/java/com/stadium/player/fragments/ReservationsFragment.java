package com.stadium.player.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stadium.player.R;
import com.stadium.player.adapters.ReservationsAdapter;
import com.stadium.player.dialogs.AttendanceDialog;
import com.stadium.player.interfaces.OnItemClickListener;
import com.stadium.player.models.entities.Reservation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 7/2/16.
 */
public class ReservationsFragment extends ParentFragment {
    private RecyclerView recyclerView;
    private ReservationsAdapter adapter;
    private List<Reservation> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_resrevations, container, false);

        // init views
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // customize the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        data = getDummyData();
        adapter = new ReservationsAdapter(activity, data, R.layout.item_reservation);
        recyclerView.setAdapter(adapter);

        // add the item click listener
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // show attendance dialog
                AttendanceDialog dialog = new AttendanceDialog(activity);
                dialog.show();
            }
        });

        return rootView;
    }

    private List<Reservation> getDummyData() {
        List<Reservation> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Reservation item = new Reservation();
            data.add(item);
        }

        return data;
    }
}
