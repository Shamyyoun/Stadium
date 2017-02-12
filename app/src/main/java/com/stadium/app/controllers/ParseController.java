package com.stadium.app.controllers;

import android.content.Context;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.stadium.app.Const;
import com.stadium.app.utils.Utils;

/**
 * Created by Shamyyoun on 1/26/17.
 */

public class ParseController implements LogInCallback, SaveCallback {
    private Context context;
    private int userId;

    public ParseController(Context context) {
        this.context = context;
    }

    public void install(int userId) {
        this.userId = userId;

        // logout current user if exists
        logoutCurrentUser();

        // login parse
        ParseUser.logInInBackground(Const.PARSE_USERNAME, Const.PARSE_PASSWORD, this);
    }

    public void installInRequired(int userId) {
        // check cached flag
        boolean installed = Utils.getCachedBoolean(context, Const.SP_PARSE_INSTALLED, false);
        if (!installed) {
            install(userId);
        }
    }

    // login callback
    @Override
    public void done(ParseUser user, ParseException e) {
        if (user != null) {
            // log
            Utils.logE("Parse user login successful");

            // install parse
            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            installation.put(Const.PARSE_KEY_USER_ID, "" + userId);
            installation.saveInBackground(this);
        } else {
            String msg = "Parse user login failed";
            msg += "\n" + e.getMessage();
            Utils.logE(msg);
        }
    }

    // install callback
    @Override
    public void done(ParseException e) {
        if (e == null) {
            // save in sp
            Utils.cacheBoolean(context, Const.SP_PARSE_INSTALLED, true);

            // log
            Utils.logE("Parse installation successful");
        } else {
            String msg = "Parse installation failed";
            msg += "\n" + e.getMessage();
            Utils.logE(msg);
        }
    }

    private void logoutCurrentUser() {
        ParseUser parseUser = ParseUser.getCurrentUser();
        if (parseUser != null) {
            parseUser.logOut();
        }
    }
}
