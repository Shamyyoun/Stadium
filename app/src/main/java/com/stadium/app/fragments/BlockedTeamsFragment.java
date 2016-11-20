package com.stadium.app.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stadium.app.R;
import com.stadium.app.adapters.BlockedAdapter;
import com.stadium.app.models.entities.Blocked;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karam on 8/10/16.
 */
public class BlockedTeamsFragment extends ParentFragment {

    private RecyclerView recyclerView;
    private BlockedAdapter blockedAdapter;
    private List<Blocked> data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.blocked);
        createOptionsMenu(R.menu.menu_blocked);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_admin_blocked, container, false);

         // init views
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // customize the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        data = getDummyData();
        blockedAdapter = new BlockedAdapter(activity, data, R.layout.item_blocked);
        recyclerView.setAdapter(blockedAdapter);

        return rootView;
    }

    private List<Blocked> getDummyData() {
        List<Blocked> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Blocked item = new Blocked();
            data.add(item);
        }

        return data;
    }
}