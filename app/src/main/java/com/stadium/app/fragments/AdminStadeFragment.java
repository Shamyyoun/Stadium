package com.stadium.app.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.stadium.app.R;
import com.stadium.app.adapters.AdminStadeAdapter;
import com.stadium.app.adapters.BigIntensityAdapter;
import com.stadium.app.dialogs.ChooseTeamDialog;
import com.stadium.app.interfaces.OnItemClickListener;
import com.stadium.app.models.entities.AdminStade;
import com.stadium.app.models.entities.BigIntensity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/*
 * Created by karam on 8/10/16.
 */
public class AdminStadeFragment extends ParentToolbarFragment {

    private RecyclerView recyclerView;
    private AdminStadeAdapter adapter;
    // define Components
    private ImageButton leftArrow;
    private ImageButton rightArrow;
    private TextView date;
    // date and calender variables
    private int mYear, mMonth, mDay;
    private long minDate;
    final Calendar c = Calendar.getInstance();

    private List<AdminStade> data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.stade);
        createOptionsMenu(R.menu.menu_stade);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_admin_stade, container, false);

        // init views

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        date = (TextView) findViewById(R.id.tv_date);
        date.setOnClickListener(this);
        leftArrow = (ImageButton) findViewById(R.id.iv_left_arrow);
        leftArrow.setOnClickListener(this);
        rightArrow = (ImageButton) findViewById(R.id.iv_right_arrow);
        rightArrow.setOnClickListener(this);

        // customize the recycler view

        GridLayoutManager layoutManager = new GridLayoutManager(activity, 3);
        recyclerView.setLayoutManager(layoutManager);
        data = getDummyData();
        adapter = new AdminStadeAdapter(activity, data, R.layout.item_admin_stade);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // open dialog

            }
        });

        // Get Current Date
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);

        date.setText(mDay + "/" + mMonth + "/" + mYear + "");

        return rootView;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {

            case R.id.tv_date:
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mDay, mYear, mMonth);

                minDate = c.getTime().getTime(); // Twice!
                datePickerDialog.getDatePicker().setMinDate(minDate);
                datePickerDialog.show();
                break;

            case R.id.iv_left_arrow:

                break;

            case R.id.iv_right_arrow:
                break;
        }
    }

    private List<AdminStade> getDummyData() {
        List<AdminStade> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            AdminStade item = new AdminStade();
            data.add(item);
        }

        return data;
    }
}