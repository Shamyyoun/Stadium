package com.stadium.app.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.google.gson.Gson;
import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.dialogs.MessageDialog;
import com.stadium.app.models.responses.ServerResponse;

/**
 * Created by Shamyyoun on 22/1/16.
 * A class, with utility methods useful only for the current project "private car"
 */
public class AppUtils {
    /**
     * method, used to show message dialog with string ids
     *
     * @param context
     * @param titleId
     * @param msgId
     * @return
     */
    public static MessageDialog showMessageDialog(Context context, int titleId, int msgId) {
        return showMessageDialog(context, context.getString(titleId), context.getString(msgId));
    }

    /**
     * method, used to show message dialog with strings
     *
     * @param context
     * @param title
     * @param msg
     * @return
     */
    public static MessageDialog showMessageDialog(Context context, String title, String msg) {
        MessageDialog dialog = new MessageDialog(context, msg);
        dialog.setTitle(title);
        dialog.show();
        return dialog;
    }

    /**
     * method, used to concatenate all parts and form a valid url
     *
     * @param pathParts
     * @return
     */
    public static String getUserApiUrl(String... pathParts) {
        String fullUrl = Const.END_POINT + "/" + Const.ROUTE_USER;
        for (String pathPart : pathParts) {
            fullUrl += "/" + pathPart;
        }
        return fullUrl;
    }

    /**
     * method, used to concatenate all parts and form a valid url
     *
     * @param pathParts
     * @return
     */
    public static String getCaptainApiUrl(String... pathParts) {
        String fullUrl = Const.END_POINT + "/" + Const.ROUTE_CAPTAIN;
        for (String pathPart : pathParts) {
            fullUrl += "/" + pathPart;
        }
        return fullUrl;
    }

    /**
     * method, used to concatenate all parts and form a valid url
     *
     * @param pathParts
     * @return
     */
    public static String getAdminApiUrl(String... pathParts) {
        String fullUrl = Const.END_POINT + "/" + Const.ROUTE_ADMIN;
        for (String pathPart : pathParts) {
            fullUrl += "/" + pathPart;
        }
        return fullUrl;
    }

    /**
     * method, used to get response errors array as one string or the default string
     *
     * @param context
     * @param response
     */
    public static String getResponseError(Context context, Object response) {
        String error = null;

        if (response instanceof ServerResponse) {
            ServerResponse serverResponse = (ServerResponse) response;
            error = serverResponse.getErrorMessage();
        }

        if (Utils.isNullOrEmpty(error)) {
            error = context.getString(R.string.error_doing_operation);
        }
        return error;
    }

    /**
     * method, used to get the error msg from the server response if possible of the def msg
     *
     * @param context
     * @param response
     * @param defMsgId
     * @return
     */
    public static String getResponseMsg(Context context, Object response, int defMsgId) {
        // try to parse the response
        ServerResponse serverResponse = null;
        try {
            serverResponse = new Gson().fromJson((String) response, ServerResponse.class);
        } catch (Exception e) {
        }

        // check the msg
        String msg = null;
        if (serverResponse != null && !Utils.isNullOrEmpty(serverResponse.getErrorMessage())) {
            msg = serverResponse.getErrorMessage();
        }

        // again, check the msg
        if (Utils.isNullOrEmpty(msg)) {
            // try to convert the objct to string
            msg = null;
            try {
                msg = (String) response;
                if (Utils.isNullOrEmpty(msg)) {
                    msg = null;
                }
            } catch (Exception e) {
            }
        }

        // again :D, check the msg
        if (Utils.isNullOrEmpty(msg)) {
            return context.getString(defMsgId);
        } else {
            return msg;
        }
    }

    /**
     * method, used to get contacts phone numbers from user's contacts
     * and prepare theme. Removes the dots, spaces and dashes and replaces the + with 00
     * @param context
     * @return
     */
    public static String[] prepareContactsPhonesArr(Context context) {
        Cursor cursor = context.getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        String[] phoneNumbers = new String[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            // get and prepare the phone number
            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneNumber = phoneNumber.replaceAll("[\\s.]", "");
            phoneNumber = phoneNumber.replaceAll(" ", "");
            phoneNumber = phoneNumber.replaceAll("-", "");
            phoneNumber = phoneNumber.replaceAll("\\+", "00");

            // add it to the array
            phoneNumbers[i] = phoneNumber;
            i++;
        }
        cursor.close();

        return phoneNumbers;
    }
}
