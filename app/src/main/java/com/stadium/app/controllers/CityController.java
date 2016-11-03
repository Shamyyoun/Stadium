package com.stadium.app.controllers;

import com.stadium.app.models.entities.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 9/3/16.
 */
public class CityController {

    public List<City> addDefaultItem(List<City> cities, String defaultItemName) {
        if (cities == null) {
            cities = new ArrayList<>();
        }

        // add the default item
        City city = new City();
        city.setName(defaultItemName);
        cities.add(0, city);

        return cities;
    }

    public int getItemPosition(List<City> cities, int cityId) {
        for (int i = 0; i < cities.size(); i++) {
            if (cityId == cities.get(i).getId()) {
                return i;
            }
        }

        return -1;
    }
}
