package com.stormnology.stadium.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.stormnology.stadium.ApiRequests;
import com.stormnology.stadium.R;
import com.stormnology.stadium.adapters.RadioButtonsAdapter;
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.controllers.CityController;
import com.stormnology.stadium.interfaces.OnCheckableSelectedListener;
import com.stormnology.stadium.interfaces.OnRefreshListener;
import com.stormnology.stadium.models.SerializableListWrapper;
import com.stormnology.stadium.models.entities.City;
import com.stormnology.stadium.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.stormnology.stadium.R.string.cities;
import static com.stormnology.stadium.R.string.select;

/**
 * Created by Shamyyoun on 6/28/16.
 */
public class ChooseCityDialog extends ProgressDialog {
    private CityController cityController;
    private RecyclerView recyclerView;
    private Button btnSubmit;
    private RadioButtonsAdapter adapter;
    private List<City> data;
    private OnCheckableSelectedListener itemSelectedListener;
    private int selectedItemId;

    public ChooseCityDialog(final Context context) {
        super(context);
        setTitle(cities);

        // create controllers
        cityController = new CityController();

        // init views
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        // customize the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // add click listeners
        btnSubmit.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get data from saved bundle if exists
        if (savedInstanceState != null) {
            SerializableListWrapper<City> dataWrapper = (SerializableListWrapper<City>) savedInstanceState.getSerializable("dataWrapper");
            if (dataWrapper != null) {
                data = dataWrapper.getList();
            }
        }

        // check data
        if (data != null) {
            updateUI();
        } else {
            loadData();
        }
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.dialog_choose_city;
    }

    @Override
    protected int getMainViewResId() {
        return R.id.recycler_view;
    }

    @Override
    protected OnRefreshListener getOnRefreshListener() {
        return new OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        };
    }

    private void updateUI() {
        // add the default item
        data = cityController.addDefaultItem(data, getString(R.string.all_cities));

        adapter = new RadioButtonsAdapter(context, data, R.layout.item_radio_button);
        recyclerView.setAdapter(adapter);
        showMain();

        // select an item if possible
        selectCheckedItem();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_submit) {
            onSubmit();
        } else {
            super.onClick(v);
        }
    }

    private void onSubmit() {
        if (adapter != null && data != null && itemSelectedListener != null) {
            itemSelectedListener.onCheckableSelected(data.get(adapter.getSelectedItemPosition()));
        }

        dismiss();
    }

    private void loadData() {
        // check internet connection
        if (!Utils.hasConnection(context)) {
            showError(R.string.no_internet_connection);
            return;
        }

        showProgress();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.getCities(context, this);
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        // get data
        City[] citiesArr = (City[]) response;
        data = new ArrayList<>(Arrays.asList(citiesArr));

        // check size
        if (data.size() == 0) {
            showEmpty(R.string.no_cities_found);
        } else {
            updateUI();
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        showError(R.string.failed_loading_cities);
    }


    @Override
    public Bundle onSaveInstanceState() {
        Bundle outState = new Bundle();
        SerializableListWrapper dataWrapper = new SerializableListWrapper<>(data);
        outState.putSerializable("dataWrapper", dataWrapper);

        return outState;
    }

    @Override
    protected void showMain() {
        btnSubmit.setText(select);
        super.showMain();
    }

    @Override
    protected void showError(int msgResId) {
        btnSubmit.setText(R.string.close);
        super.showError(msgResId);
    }

    @Override
    protected void showEmpty(int msgResId) {
        btnSubmit.setText(R.string.close);
        super.showEmpty(msgResId);
    }

    public void setOnItemSelectedListener(OnCheckableSelectedListener itemSelectedListener) {
        this.itemSelectedListener = itemSelectedListener;
    }

    public void setSelectedItemId(int selectedItemId) {
        this.selectedItemId = selectedItemId;
    }

    private void selectCheckedItem() {
        int itemPosition = cityController.getItemPosition(data, selectedItemId);
        if (itemPosition != -1) {
            adapter.setSelectedItem(itemPosition);
        }
    }

    @Override
    public void show() {
        super.show();

        if (data != null && adapter != null) {
            selectCheckedItem();
        }
    }
}