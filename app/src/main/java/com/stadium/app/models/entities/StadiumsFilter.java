package com.stadium.app.models.entities;

import java.io.Serializable;

/**
 * Created by Shamyyoun on 11/2/16.
 */

public class StadiumsFilter implements Serializable {
    private City city;
    private String name;
    private String fieldCapacity;
    private String date;
    private String timeStart;
    private String timeEnd;

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

    public String getFieldCapacity() {
        return fieldCapacity;
    }

    public void setFieldCapacity(String fieldCapacity) {
        this.fieldCapacity = fieldCapacity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }
}
