package com.stormnology.stadium.models.entities;

import java.io.Serializable;

/**
 * Created by Shamyyoun on 11/2/16.
 */

public class PlayersFilter implements Serializable {
    private City city;
    private String name;
    private String position;
    private String phone;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
