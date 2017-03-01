package com.stormnology.stadium.controllers;

import android.content.Context;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.stormnology.stadium.Const;
import com.stormnology.stadium.utils.Utils;

import java.util.List;

/**
 * Created by Shamyyoun on 1/26/17.
 */

public class ParseController implements LogInCallback, SaveCallback {
    private Context context;
    private int userId;
    private List<String> channels;

    public ParseController(Context context) {
        this.context = context;
    }

    public void install(int userId, List<String> channels) {
        this.userId = userId;
        this.channels = channels;

        // logout current user if exists
        logoutCurrentUser();

        // login parse
        ParseUser.logInInBackground(Const.PARSE_USERNAME, Const.PARSE_PASSWORD, this);
    }

    public void installIfRequired(int userId, List<String> channels) {
        // check cached flag
        boolean installed = Utils.getCachedBoolean(context, Const.SP_PARSE_INSTALLED, false);
        if (!installed) {
            install(userId, channels);
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
            installation.deleteInBackground(); // remove it first then save new values
            installation.put(Const.PARSE_KEY_USER_ID, "" + userId);
            installation.put(Const.PARSE_KEY_CHANNELS, channels);
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

    public boolean logOut() {
        // check installation
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        if (installation == null) {
            // no installation found
            // logout logic is successful, so return true
            Utils.logE("Parse logout failed because installation is not found");
            return true;
        }

        try {
            // remove channels value
            installation.remove(Const.PARSE_KEY_CHANNELS);
            installation.save();

            // logout current user
            logoutCurrentUser();

            // log
            Utils.logE("Parse logout successful");

            return true;
        } catch (ParseException e) {
            // check exception
            if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                // no user found
                // logout logic is successful, so return true
                Utils.logE("Parse logout failed because object is not found");
                return true;
            } else {
                String msg = "Parse logout failed";
                msg += "\n" + e.getMessage();
                Utils.logE(msg);

                return false;
            }
        }
    }

    private void logoutCurrentUser() {
        ParseUser parseUser = ParseUser.getCurrentUser();
        if (parseUser != null) {
            parseUser.logOut();
        }
    }
}
