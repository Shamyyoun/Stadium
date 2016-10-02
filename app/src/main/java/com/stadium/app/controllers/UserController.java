package com.stadium.app.controllers;

import android.content.Context;

import com.stadium.app.Const;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.SharedPrefs;
import com.stadium.app.utils.Utils;

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

    public void logout() {
        prefs.remove(Const.SP_USER);
        setUser(null);
    }

    public String getNamePosition() {
        User user = getUser();
        String str = user.getName();
        if (!Utils.isNullOrEmpty(user.getPosition())) {
            str += " . " + user.getPosition();
        }

        return str;
    }

    public User clone() {
        return prefs.load(Const.SP_USER);
    }
}
