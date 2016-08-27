package com.stadium.app.controllers;

import android.content.Context;

import com.stadium.app.Const;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.SharedPrefs;

/**
 * Created by Shamyyoun on 8/27/16.
 */
public class UserController {
    private Context context;
    private User user;

    public UserController(Context context) {
        this.context = context;
    }

    public UserController(Context context, User user) {
        this.context = context;
        this.user = user;
    }

    public void save() {
        SharedPrefs<User> prefs = new SharedPrefs(context, User.class);
        prefs.save(user, Const.SP_USER);
    }

    public User getUser() {
        if (user == null) {
            SharedPrefs<User> prefs = new SharedPrefs(context, User.class);
            user = prefs.load(Const.SP_USER);
        }

        return user;
    }

    public boolean hasLoggedInUser() {
        return getUser() != null;
    }
}
