package com.stormnology.stadium.models.entities;

import java.io.Serializable;

/**
 * Created by Shamyyoun on 11/2/16.
 */

public class TeamsFilter implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
