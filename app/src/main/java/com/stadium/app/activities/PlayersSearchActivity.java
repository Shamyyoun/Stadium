package com.stadium.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.dialogs.ChooseCityDialog;
import com.stadium.app.dialogs.ChoosePositionDialog;
import com.stadium.app.interfaces.OnCheckableSelectedListener;
import com.stadium.app.models.Checkable;
import com.stadium.app.models.entities.City;
import com.stadium.app.models.entities.PlayersFilter;
import com.stadium.app.models.entities.Position;
import com.stadium.app.utils.Utils;

/*
 * Created by Shamyyoun on 11/2/16.
 */
public class PlayersSearchActivity extends ParentActivity {
    private PlayersFilter filter;
    private Button btnCity;
    private EditText etName;
    private Button btnPosition;
    private Button btnSearch;
    private Button btnEmptySpace;

    private ChooseCityDialog citiesDialog;
    private ChoosePositionDialog positionsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_search);
        customizeLayoutParams();

        // customize the toolbar
        setToolbarIcon(R.drawable.white_close_icon);
        enableBackButton();

        // get the filter
        filter = (PlayersFilter) getIntent().getSerializableExtra(Const.KEY_FILTER);

        // init views
        btnCity = (Button) findViewById(R.id.btn_city);
        etName = (EditText) findViewById(R.id.et_name);
        btnPosition = (Button) findViewById(R.id.btn_position);
        btnSearch = (Button) findViewById(R.id.btn_search);
        btnEmptySpace = (Button) findViewById(R.id.btn_empty_space);

        // add listeners
        btnCity.setOnClickListener(this);
        btnPosition.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnEmptySpace.setOnClickListener(this);

        // check filter to update the ui or just create a new one
        if (filter != null) {
            updateUI();
        } else {
            filter = new PlayersFilter();
        }
    }

    private void customizeLayoutParams() {
        // customize the layout params:
        // set the activity width and height to match parent and its gravity to the top
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.TOP;
        getWindow().setAttributes(layoutParams);
    }

    private void updateUI() {
        if (filter.getCity() != null) {
            btnCity.setText(filter.getCity().toString());
        }

        if (filter.getName() != null) {
            etName.setText(filter.getName());
        }

        if (filter.getPosition() != null) {
            btnPosition.setText(filter.getPosition());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_city:
                chooseCity();
                break;

            case R.id.btn_position:
                choosePosition();
                break;

            case R.id.btn_search:
                onSearch();
                break;

            case R.id.btn_empty_space:
                onBackPressed();
                break;

            default:
                super.onClick(v);
        }
    }

    private void chooseCity() {
        if (citiesDialog == null) {
            citiesDialog = new ChooseCityDialog(this);
            citiesDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
                @Override
                public void onCheckableSelected(Checkable item) {
                    // check the city item
                    City city = (City) item;
                    if (city.getId() == 0) {
                        // all cities item
                        filter.setCity(null);
                    } else {
                        filter.setCity(city);
                    }
                    btnCity.setText(city.toString());
                }
            });
        }

        // check to select item if possible
        if (filter.getCity() != null) {
            citiesDialog.setSelectedItemId(filter.getCity().getId());
        }

        citiesDialog.show();
    }

    private void choosePosition() {
        if (positionsDialog == null) {
            positionsDialog = new ChoosePositionDialog(this);
            positionsDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
                @Override
                public void onCheckableSelected(Checkable item) {
                    // check the city item
                    Position position = (Position) item;
                    if (position.getName().equals(getString(R.string.all_positions))) {
                        // all positions item
                        filter.setPosition(null);
                    } else {
                        filter.setPosition(position.getName());
                    }
                    btnPosition.setText(position.getName());
                }
            });
        }

        // check to select item if possible
        if (filter.getPosition() != null) {
            positionsDialog.setSelectedItem(filter.getPosition());
        }

        positionsDialog.show();
    }

    private void onSearch() {
        filter.setName(Utils.getText(etName));
        Intent intent = new Intent();
        intent.putExtra(Const.KEY_FILTER, filter);
        setResult(RESULT_OK, intent);
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.no_anim, R.anim.top_translate_exit);
    }
}
