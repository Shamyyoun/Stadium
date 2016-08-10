package com.stadium.app.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stadium.app.R;
import com.stadium.app.adapters.AcceptedReservationAdapter;
import com.stadium.app.adapters.NewReservationAdapter;
import com.stadium.app.models.entities.AcceptedReservation;
import com.stadium.app.models.entities.NewReservation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karam on 8/10/16.
 */
public class NewReservationFragment extends ParentFragment {

    private RecyclerView recyclerView;
    private NewReservationAdapter adapter;
    private List<NewReservation> data;

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
        adapter = new NewReservationAdapter(activity, data, R.layout.item_new_reservation);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private List<NewReservation> getDummyData() {
        List<NewReservation> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            NewReservation item = new NewReservation();
            data.add(item);
        }

        return data;
    }
}
