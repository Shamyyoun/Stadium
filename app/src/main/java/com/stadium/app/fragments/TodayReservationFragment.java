package com.stadium.app.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stadium.app.R;
import com.stadium.app.adapters.TeamReservationsAdapter;
import com.stadium.app.adapters.TodayReservationAdapter;
import com.stadium.app.models.entities.TeamReservations;
import com.stadium.app.models.entities.TodayReservation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karam on 8/10/16.
 */
public class TodayReservationFragment extends ParentFragment {

    private RecyclerView recyclerView;
    private TodayReservationAdapter adapter;
    private List<TodayReservation> data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_today_reservation, container, false);

        // init views
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // customize the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        data = getDummyData();
        adapter = new TodayReservationAdapter(activity, data, R.layout.item_today_reservation);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private List<TodayReservation> getDummyData() {
        List<TodayReservation> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            TodayReservation item = new TodayReservation();
            data.add(item);
        }

        return data;
    }
}
