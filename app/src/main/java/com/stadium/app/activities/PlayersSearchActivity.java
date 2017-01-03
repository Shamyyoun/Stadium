package com.stadium.app.activities;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
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
    private View layoutContent;
    private Button btnCity;
    private EditText etName;
    private Button btnPosition;
    private Button btnSearch;

    private Rect contentLayoutRect; // to handle the outside click
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
        layoutContent = findViewById(R.id.layout_content);
        btnCity = (Button) findViewById(R.id.btn_city);
        etName = (EditText) findViewById(R.id.et_name);
        btnPosition = (Button) findViewById(R.id.btn_position);
        btnSearch = (Button) findViewById(R.id.btn_search);

        // add listeners
        btnCity.setOnClickListener(this);
        btnPosition.setOnClickListener(this);
        btnSearch.setOnClickListener(this);

        // create the content rect
        contentLayoutRect = new Rect();

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
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.TOP;
        getWindow().setAttributes(layoutParams);
    }

    private void updateUI() {
        updateCityUI();
        updateNameUI();
        updatePositionUI();
    }

    private void updateCityUI() {
        if (filter.getCity() != null) {
            String str = getString(R.string.the_city) + ": " + filter.getCity().toString();
            btnCity.setText(str);
        } else {
            btnCity.setText(R.string.the_city);
        }
    }

    private void updateNameUI() {
        if (filter.getName() != null) {
            etName.setText(filter.getName());
        } else {
            etName.setText("");
        }
    }

    private void updatePositionUI() {
        if (filter.getPosition() != null) {
            String str = getString(R.string.position) + ": " + filter.getPosition();
            btnPosition.setText(str);
        } else {
            btnPosition.setText(R.string.position);
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
                    updateCityUI();
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
            positionsDialog.addDefaultItem(getString(R.string.all_positions));
            positionsDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
                @Override
                public void onCheckableSelected(Checkable item) {
                    // check the position item
                    Position position = (Position) item;
                    if (position.getName().equals(getString(R.string.all_positions))) {
                        // all positions item
                        filter.setPosition(null);
                    } else {
                        filter.setPosition(position.getName());
                    }
                    updatePositionUI();
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
        // get the name
        String name = Utils.getText(etName);
        if (!name.isEmpty()) {
            filter.setName(name);
        } else {
            filter.setName(null);
        }

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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // get x, y
        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();

        // check if this point is outside the content layout
        layoutContent.getDrawingRect(contentLayoutRect);
        if (ev.getAction() == MotionEvent.ACTION_UP
                && !contentLayoutRect.contains(x, y)) {
            onBackPressed();
        }

        return super.dispatchTouchEvent(ev);
    }
}
