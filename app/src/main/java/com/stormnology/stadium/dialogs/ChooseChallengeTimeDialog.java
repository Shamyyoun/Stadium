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
import com.stormnology.stadium.controllers.ChallengeTimeController;
import com.stormnology.stadium.interfaces.OnCheckableSelectedListener;
import com.stormnology.stadium.interfaces.OnRefreshListener;
import com.stormnology.stadium.models.SerializableListWrapper;
import com.stormnology.stadium.models.entities.ChallengeTime;
import com.stormnology.stadium.utils.Utils;

import java.util.List;

import static com.stormnology.stadium.R.string.select;

/**
 * Created by Shamyyoun on 6/28/16.
 */
public class ChooseChallengeTimeDialog extends ProgressDialog {
    private ChallengeTimeController timeController;
    private RecyclerView recyclerView;
    private Button btnSubmit;
    private RadioButtonsAdapter adapter;
    private List<ChallengeTime> data;
    private OnCheckableSelectedListener itemSelectedListener;
    private String selectedItem;
    private boolean addDefaultItem;

    public ChooseChallengeTimeDialog(final Context context) {
        super(context);
        setTitle(R.string.challenges_times);

        // create the controller
        timeController = new ChallengeTimeController();

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
            SerializableListWrapper<ChallengeTime> dataWrapper = (SerializableListWrapper<ChallengeTime>) savedInstanceState.getSerializable("dataWrapper");
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
        return R.layout.dialog_choose_challenge_time;
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
        // add default item if possible
        if (addDefaultItem) {
            data = timeController.addDefaultItem(data, getString(R.string.all_times));
        }

        // create and set the adapter
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
        ConnectionHandler connectionHandler = ApiRequests.challengeTimes(context, this);
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        // get data
        String[] timesArr = (String[]) response;
        data = timeController.createList(timesArr);

        // check size
        if (data.size() == 0) {
            showEmpty(R.string.no_times_found);
        } else {
            updateUI();
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        showError(R.string.failed_loading_times);
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

    public void setSelectedItem(String selectedItem) {
        this.selectedItem = selectedItem;
    }

    private void selectCheckedItem() {
        int itemPosition = timeController.getItemPosition(data, selectedItem);
        if (itemPosition != -1) {
            adapter.setSelectedItem(itemPosition);
        }
    }

    public void setAddDefaultItem(boolean addDefaultItem) {
        this.addDefaultItem = addDefaultItem;
    }

    @Override
    public void show() {
        super.show();

        if (data != null && adapter != null) {
            selectCheckedItem();
        }
    }
}