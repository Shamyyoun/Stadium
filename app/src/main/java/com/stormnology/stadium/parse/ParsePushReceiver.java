package com.stormnology.stadium.parse;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;
import com.parse.ParsePushBroadcastReceiver;
import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.activities.AdminMainActivity;
import com.stormnology.stadium.activities.PlayerMainActivity;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.models.payloads.ParsePayload;
import com.stormnology.stadium.utils.Utils;

import java.util.Random;

/**
 * Created by Shamyyoun on 26/01/17.
 */
public class ParsePushReceiver extends ParsePushBroadcastReceiver {
    private Context context;
    private ActiveUserController activeUserController;
    private NotificationManager notificationManager;

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        this.context = context;

        // check logged in user
        activeUserController = new ActiveUserController(context);
        if (!activeUserController.hasLoggedInUser()) {
            return;
        }

        // check payload
        ParsePayload payload = getPayload(intent);
        if (payload == null) {
            return;
        }

        // show notification
        int id = new Random().nextInt();
        showNotification(id, payload.getAlert());
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        super.onPushDismiss(context, intent);
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
    }

    private ParsePayload getPayload(Intent intent) {
        try {
            // get and log payload str
            String payloadStr = intent.getExtras().getString("com.parse.Data");
            Utils.logE("Parse payload: \n" + payloadStr);

            // parse to object
            Gson gson = new Gson();
            ParsePayload payload = gson.fromJson(payloadStr, ParsePayload.class);

            // return it
            return payload;
        } catch (Exception e) {
            // log error
            String msg = "Can't get payload";
            msg += "\n" + e.getMessage();
            Utils.logE(msg);

            return null;
        }
    }

    private void showNotification(int id, String message) {
        String title = context.getString(R.string.app_name);

        // create pending intent
        Intent intent = new Intent(context, activeUserController.isAdmin() ?
                AdminMainActivity.class : PlayerMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(Const.KEY_REFRESH_HOME, true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND
                        | Notification.DEFAULT_VIBRATE);

        // set big text style
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.bigText(message);
        builder.setStyle(style);

        // get notification manager if required
        if (notificationManager == null) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        // show the notification
        notificationManager.notify(id, builder.build());
    }
}