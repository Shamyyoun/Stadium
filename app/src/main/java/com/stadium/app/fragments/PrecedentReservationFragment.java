package com.stadium.app.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stadium.app.R;
import com.stadium.app.adapters.NewReservationAdapter;
import com.stadium.app.adapters.PrecedentReservationAdapter;
import com.stadium.app.models.entities.NewReservation;
import com.stadium.app.models.entities.PrecedentReservation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karam on 8/10/16.
 */
public class PrecedentReservationFragment extends ParentFragment {

    private RecyclerView recyclerView;
    private PrecedentReservationAdapter adapter;
    private List<PrecedentReservation> data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_new_reservations, container, false);

        // init views
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // customize the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        data = getDummyData();
        adapter = new PrecedentReservationAdapter(activity, data, R.layout.item_precedent_reservation);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private List<PrecedentReservation> getDummyData() {
        List<PrecedentReservation> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            PrecedentReservation item = new PrecedentReservation();
            data.add(item);
        }

        return data;
    }
}
