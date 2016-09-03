package com.stadium.app.controllers;

import android.content.Context;

import com.stadium.app.R;
import com.stadium.app.models.entities.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 9/3/16.
 */
public class CityController {
    public static List<City> addDefaultItem(Context context, List<City> cities) {
        if (cities == null) {
            cities = new ArrayList<>();
        }

        // add the default item
        City city = new City();
        city.setName(context.getString(R.string.select_city));
        cities.add(0, city);

        return cities;
    }
}
