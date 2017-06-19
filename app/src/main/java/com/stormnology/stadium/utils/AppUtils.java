package com.stormnology.stadium.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.google.gson.Gson;
import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.dialogs.MessageDialog;
import com.stormnology.stadium.models.responses.ServerResponse;

import java.util.ArrayList;
import java.util.List;

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
    public static String getUserApiUrl(Object... pathParts) {
        String fullUrl = Const.END_POINT + "/" + Const.ROUTE_USER;
        for (Object pathPart : pathParts) {
            fullUrl += "/" + pathPart.toString();
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
     * method, used to get the error msg from the server response if possible or the def msg
     *
     * @param context
     * @param response
     * @return
     */
    public static String getResponseMsg(Context context, Object response) {
        return getResponseMsg(context, response, R.string.something_went_wrong_please_try_again);
    }

    /**
     * method, used to get the error msg from the server response if possible or the def msg
     *
     * @param context
     * @param response
     * @param defMsgId
     * @return
     */
    public static String getResponseMsg(Context context, Object response, int defMsgId) {
        String msg = null;

        // check the class type and get the msg
        if (response instanceof ServerResponse) {
            msg = ((ServerResponse) response).getErrorMessage();
        } else if (response instanceof String) {
            try {
                ServerResponse serverResponse = new Gson().fromJson((String) response, ServerResponse.class);
                msg = serverResponse.getErrorMessage();
            } catch (Exception e) {
            }
        }

        // check the msg
        if (Utils.isNullOrEmpty(msg)) {
            return context.getString(defMsgId);
        } else {
            return msg;
        }
    }

    /**
     * method, used to get contacts phone numbers from user's contacts
     * and prepare theme. Removes the dots, spaces, (, ) and dashes and replaces the + with 00
     *
     * @param context
     * @return
     */
    public static List<String> prepareContactsPhonesList(Context context) {
        Cursor cursor = context.getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        List<String> phoneNumbers = new ArrayList<>(cursor.getCount());
        int i = 0;
        while (cursor.moveToNext()) {
            // get and prepare the phone number
            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            phoneNumber = phoneNumber.replaceAll("[\\s.]", "");
            phoneNumber = phoneNumber.replaceAll(" ", "");
            phoneNumber = phoneNumber.replaceAll("-", "");
            phoneNumber = phoneNumber.replaceAll("\\(", "");
            phoneNumber = phoneNumber.replaceAll("\\)", "");
            phoneNumber = phoneNumber.replaceAll("\\+", "00");

            // add it to the array
            phoneNumbers.add(phoneNumber);
            i++;
        }
        cursor.close();

        return phoneNumbers;
    }

    /**
     * method, used to format the price with the app currency and return it
     *
     * @param context
     * @param price
     * @return
     */
    public static String getFormattedPrice(Context context, String price) {
        price += " " + context.getString(R.string.currency);
        return price;
    }

    /**
     * method, used to prepare the share text and return it
     *
     * @param context
     * @return
     */
    public static String getShareAppText(Context context) {
        String text = context.getString(R.string.share_app_text, Utils.getPlayStoreAppUrl(context));
        return text;
    }
}
