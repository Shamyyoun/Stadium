package com.stadium.app.controllers;

import android.content.Context;

import com.stadium.app.Const;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.SharedPrefs;

/**
 * Created by Shamyyoun on 8/27/16.
 */
public class UserController {
    private static User instance;
    private Context context;
    private SharedPrefs<User> prefs;

    public UserController(Context context) {
        this.context = context;
        prefs = new SharedPrefs(context, User.class);
    }

    public void save() {
        prefs.save(instance, Const.SP_USER);
    }

    public User getUser() {
        if (instance == null) {
            instance = prefs.load(Const.SP_USER);
        }

        return instance;
    }

    public void setUser(User user) {
        instance = user;
    }

    public boolean hasLoggedInUser() {
        return getUser() != null;
    }

    public void removeCurrent() {
        prefs.remove(Const.SP_USER);
    }
}
