package com.stormnology.stadium.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.stormnology.stadium.ApiRequests;
import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.adapters.ReservationPlayersAdapter;
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.interfaces.OnCheckableCheckedListener;
import com.stormnology.stadium.interfaces.OnRefreshListener;
import com.stormnology.stadium.interfaces.OnReservationPlayersSelectedListener;
import com.stormnology.stadium.models.Checkable;
import com.stormnology.stadium.models.SerializableListWrapper;
import com.stormnology.stadium.models.entities.User;
import com.stormnology.stadium.utils.Utils;
import com.stormnology.stadium.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.stormnology.stadium.R.string.select;

/**
 * Created by Shamyyoun on 6/28/16.
 */
public class ReservationPlayersDialog extends ProgressDialog implements OnCheckableCheckedListener {
    private int teamId;
    private EditText etPlayersCount;
    private TextView tvSelectionCount;
    private RecyclerView recyclerView;
    private Button btnSubmit;
    private ReservationPlayersAdapter adapter;
    private List<User> data;
    private OnReservationPlayersSelectedListener playersSelectedListener;

    public ReservationPlayersDialog(final Context context, int teamId) {
        super(context);
        setTitle(R.string.select_players);
        this.teamId = teamId;

        // init views
        etPlayersCount = (EditText) findViewById(R.id.et_players_count);
        tvSelectionCount = (TextView) findViewById(R.id.tv_selection_count);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        // customize the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));

        // add click listeners
        btnSubmit.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get data from saved bundle if exists
        if (savedInstanceState != null) {
            SerializableListWrapper<User> dataWrapper = (SerializableListWrapper<User>) savedInstanceState.getSerializable("dataWrapper");
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
        return R.layout.dialog_reservation_players;
    }

    @Override
    protected int getMainViewResId() {
        return R.id.main_view;
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
        // set recycler adapter
        adapter = new ReservationPlayersAdapter(context, data, R.layout.item_checkable_player);
        adapter.setOnItemCheckedListener(this);
        recyclerView.setAdapter(adapter);
        showMain();

        // set players count in the count edit text as initial
        etPlayersCount.setText("" + (data.size() - 1));

        // update the selection count textview
        updateSelectionCount();
    }

    private void updateSelectionCount() {
        // get checked items count and update the textview
        int checkedItemsCount = adapter.getCheckedItemsCount();
        tvSelectionCount.setText(checkedItemsCount + " " + getString(R.string.invitation));
    }

    @Override
    public void onCheckableChecked(Checkable item, boolean checked) {
        updateSelectionCount();
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
        if (adapter != null && data != null && playersSelectedListener != null) {
            List<User> checkedUsers = adapter.getCheckedItems();

            // check size
            if (checkedUsers.size() == 0) {
                // show error msg
                Utils.showShortToast(context, R.string.you_must_choose_at_least_one_player);
            } else {
                // get players count and fire the listener
                int playersCount = Utils.convertToInt(Utils.getText(etPlayersCount));
                playersSelectedListener.onPlayersSelected(checkedUsers, playersCount);
                dismiss();
            }
        } else {
            dismiss();
        }
    }

    private void loadData() {
        // check internet connection
        if (!Utils.hasConnection(context)) {
            showError(R.string.no_internet_connection);
            return;
        }

        showProgress();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.teamPlayers(context, this, teamId);
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        // get data
        User[] usersArr = (User[]) response;
        data = new ArrayList<>(Arrays.asList(usersArr));

        // check size
        if (data.size() == 0) {
            showEmpty(R.string.no_players_in_this_team);
        } else {
            // add default item
            User defaultUser = new User();
            defaultUser.setId(Const.DEFAULT_ITEM_ID);
            data.add(0, defaultUser);

            // update the ui
            updateUI();
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        showError(R.string.failed_loading_team_players);
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

    public void setOnPlayersSelectedListener(OnReservationPlayersSelectedListener playersSelectedListener) {
        this.playersSelectedListener = playersSelectedListener;
    }
}