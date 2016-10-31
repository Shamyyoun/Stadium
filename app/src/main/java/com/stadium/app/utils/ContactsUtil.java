package com.stadium.app.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

/**
 * Created by Shamyyoun on 10/31/16.
 */

public class ContactsUtil {
    public static String[] getContactsPhoneNumbers(Context context) {
        Cursor cursor = context.getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        String[] phoneNumbers = new String[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneNumbers[i] = phoneNumber;
            i++;
        }
        cursor.close();

        return phoneNumbers;
    }
}
