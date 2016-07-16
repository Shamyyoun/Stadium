package com.stadium.player.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ListView;
import android.widget.ScrollView;

import com.stadium.player.R;
import com.stadium.player.adapters.NotificationsAdapter;
import com.stadium.player.models.entities.Notification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 7/2/16.
 */
public class HomeFragment extends ParentFragment {
    private ScrollView scrollView;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // init views
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        listView = (ListView) findViewById(R.id.list_view);

        // set the list adapter
        List<Notification> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            data.add(new Notification());
        }
        NotificationsAdapter adapter = new NotificationsAdapter(activity, R.layout.item_notifications, data);
        listView.setAdapter(adapter);

        // add global layout listener
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // scroll the scroll view to the top
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0, 0);
                    }
                }, 50);
            }
        });

        return rootView;
    }
}
