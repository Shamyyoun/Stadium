package com.stadium.app.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.stadium.app.ApiRequests;
import com.stadium.app.R;
import com.stadium.app.adapters.RadioButtonsAdapter;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.controllers.PositionController;
import com.stadium.app.interfaces.OnCheckableSelectedListener;
import com.stadium.app.interfaces.OnRefreshListener;
import com.stadium.app.models.SerializableListWrapper;
import com.stadium.app.models.entities.Position;
import com.stadium.app.utils.Utils;

import java.util.List;

import static com.stadium.app.R.string.select;

/**
 * Created by Shamyyoun on 6/28/16.
 */
public class ChoosePositionDialog extends ProgressDialog {
    private PositionController positionController;
    private RecyclerView recyclerView;
    private Button btnSubmit;
    private RadioButtonsAdapter adapter;
    private List<Position> data;
    private OnCheckableSelectedListener itemSelectedListener;
    private String selectedItem;
    private String defaultItem;

    public ChoosePositionDialog(final Context context) {
        super(context);
        setTitle(R.string.positions);

        // create the controller
        positionController = new PositionController();

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
            SerializableListWrapper<Position> dataWrapper = (SerializableListWrapper<Position>) savedInstanceState.getSerializable("dataWrapper");
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
        return R.layout.dialog_choose_position;
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
        // check to add default item
        if (defaultItem != null) {
            data = positionController.addDefaultItem(data, defaultItem);
        }

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
        ConnectionHandler connectionHandler = ApiRequests.getPositions(context, this);
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        // get data
        String[] positionsArr = (String[]) response;
        data = positionController.createList(positionsArr);

        // check size
        if (data.size() == 0) {
            showEmpty(R.string.no_positions_found);
        } else {
            updateUI();
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        showError(R.string.failed_loading_positions);
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
        int itemPosition = positionController.getItemPosition(data, selectedItem);
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

    public void addDefaultItem(String defaultItem) {
        this.defaultItem = defaultItem;
    }
}