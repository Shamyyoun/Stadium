package com.stadium.app.controllers;

import com.stadium.app.models.entities.User;
import com.stadium.app.utils.Utils;

/**
 * Created by Shamyyoun on 8/27/16.
 */
public class UserController {
    private User user;

    public UserController(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNamePosition() {
        String str = user.getName();
        if (!Utils.isNullOrEmpty(user.getPosition())) {
            str += " . " + user.getPosition();
        }

        return str;
    }

    public String getCityName() {
        if (user.getCity() == null) {
            return null;
        } else {
            return user.getCity().getName().trim();
        }
    }

    public String getPhoneNumber() {
        if (user.getPhone() == null) {
            return null;
        } else {
            return user.getPhone().trim();
        }
    }
}
