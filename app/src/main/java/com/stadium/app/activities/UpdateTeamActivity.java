package com.stadium.app.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.stadium.app.R;

/**
 * Created by karam on 7/10/16.
 */
public class UpdateTeamActivity extends ParentActivity {
    private Spinner spStadium;
    private Spinner spCaptain;
    private Spinner spAssistant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBackButton();

        setContentView(R.layout.activity_update_team);

        // inti views
        spStadium = (Spinner) findViewById(R.id.sp_stadium);
        spCaptain = (Spinner) findViewById(R.id.sp_captain);
        spAssistant = (Spinner) findViewById(R.id.sp_assistant);

        // customize the spinners
        String[] dummyArr = new String[]{"Stadium 1", "Stadium 2", "Stadium 3", "Stadium 4", "Stadium 5", "Stadium 6"};
        ArrayAdapter adapter1 = new ArrayAdapter(this, R.layout.item_dropdown_selected, dummyArr);
        adapter1.setDropDownViewResource(R.layout.item_dropdown);
        spStadium.setAdapter(adapter1);

        String[] dummyArr2 = new String[]{"Captain 1", "Captain 2", "Captain 3", "Captain 4", "Captain 5", "Captain 6"};
        ArrayAdapter adapter2 = new ArrayAdapter(this, R.layout.item_dropdown_selected, dummyArr2);
        adapter2.setDropDownViewResource(R.layout.item_dropdown);
        spCaptain.setAdapter(adapter2);

        String[] dummyArr3 = new String[]{"Assistant 1", "Assistant 2", "Assistant 3", "Assistant 4", "Assistant 5", "Assistant4 6"};
        ArrayAdapter adapter3 = new ArrayAdapter(this, R.layout.item_dropdown_selected, dummyArr3);
        adapter3.setDropDownViewResource(R.layout.item_dropdown);
        spAssistant.setAdapter(adapter3);
    }
}
