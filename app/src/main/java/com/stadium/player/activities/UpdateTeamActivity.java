package com.stadium.player.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.makeramen.roundedimageview.RoundedImageView;
import com.stadium.player.R;
import com.stadium.player.views.DarkenedButton;

/**
 * Created by karam on 7/10/16.
 */
public class UpdateTeamActivity extends ParentToolbarActivity {

    //Buttons
    private DarkenedButton updateTeam;
    private DarkenedButton cancel;

    //Spinners
    private Spinner sp_stadium;
    private Spinner sp_captain;
    private Spinner sp_assistant;

    //Edit text
    private EditText et_teamName;
    private EditText et_teamDetails;

    //ImageView
    private RoundedImageView iv_teamPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_team);

        //init All views
        updateTeam = (DarkenedButton) findViewById(R.id.btn_update);
        cancel = (DarkenedButton) findViewById(R.id.btn_cancel);

        sp_stadium = (Spinner) findViewById(R.id.sp_stadium);
        sp_captain = (Spinner) findViewById(R.id.sp_captine);
        sp_assistant = (Spinner) findViewById(R.id.sp_captineAssistant);

        et_teamName = (EditText) findViewById(R.id.et_name);
        et_teamDetails = (EditText) findViewById(R.id.et_team_details);

        iv_teamPhoto = (RoundedImageView) findViewById(R.id.iv_photo);

        // customize the spinners
        String[] dummyArr = new String[]{"Stadium 1", "Stadium 2", "Stadium 3", "Stadium 4", "Stadium 5", "Stadium 6"};
        ArrayAdapter adapter1 = new ArrayAdapter(this, R.layout.item_dropdown_selected, dummyArr);
        adapter1.setDropDownViewResource(R.layout.item_dropdown);
        sp_stadium.setAdapter(adapter1);

        String[] dummyArr2 = new String[]{"Captain 1", "Captain 2", "Captain 3", "Captain 4", "Captain 5", "Captain 6"};
        ArrayAdapter adapter2 = new ArrayAdapter(this, R.layout.item_dropdown_selected, dummyArr2);
        adapter2.setDropDownViewResource(R.layout.item_dropdown);
        sp_captain.setAdapter(adapter2);

        String[] dummyArr3 = new String[]{"Assistant 1", "Assistant 2", "Assistant 3", "Assistant 4", "Assistant 5", "Assistant4 6"};
        ArrayAdapter adapter3 = new ArrayAdapter(this, R.layout.item_dropdown_selected, dummyArr3);
        adapter3.setDropDownViewResource(R.layout.item_dropdown);
        sp_assistant.setAdapter(adapter3);

        String team = getResources().getString(R.string.team);
        String details = getResources().getString(R.string.details);

        et_teamName.setText(team+" . "+ "الامل");
        et_teamDetails.setText(details +" . "+" هناك حقيقة مثبتة عبر التاريخ وهي ان المحتوي المقروء سيلهي القارئ عن التركيز في الشكل الخارجي للنص");

    }
}
