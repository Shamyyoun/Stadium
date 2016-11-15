package com.stadium.app.controllers;

import com.stadium.app.models.entities.User;
import com.stadium.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;

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

    public List<User> removeFromList(List<User> users) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getId() == this.user.getId()) {
                users.remove(i);
                return users;
            }
        }

        return users;
    }

    public List<Integer> getIds(List<User> users) {
        List<Integer> ids = new ArrayList<>(users.size());
        for (User user : users) {
            ids.add(user.getId());
        }

        return ids;
    }
}
