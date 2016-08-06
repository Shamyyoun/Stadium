package com.stadium.app.dialogs;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.stadium.app.R;

/**
 * Created by karam on 7/31/16.
 */
public class ContactDialog extends ParentDialog {

    private TextView tvEmail;
    private TextView tvPhoneNumber;
    private Button btnExit;

    public ContactDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_contacts);

        // init views
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tvPhoneNumber = (TextView) findViewById(R.id.tv_number);
        btnExit = (Button) findViewById(R.id.btn_exit);


        // customize the close button
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"stadium.123@gamil.com"});
                getContext().startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });

        tvPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String posted_by = "0201155086602";
                String uri = "tel:" + posted_by.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    Toast.makeText(getContext() , "accept permission first" , Toast.LENGTH_SHORT).show();
                    return;
                }
                getContext().startActivity(intent);
            }
        });
    }


    public void setOnCloseClickListener(View.OnClickListener closeClickListener) {
        btnExit.setOnClickListener(closeClickListener);
    }
}
