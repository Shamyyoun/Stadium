package com.stadium.app.controllers;

import com.stadium.app.models.entities.Stadium;
import com.stadium.app.utils.Utils;

/**
 * Created by Shamyyoun on 9/4/16.
 */
public class StadiumController {
    private Stadium stadium;

    public StadiumController(Stadium stadium) {
        this.stadium = stadium;
    }

    public boolean hasContactInfo() {
        if (Utils.isNullOrEmpty(stadium.getPhoneNumber()) && Utils.isNullOrEmpty(stadium.getEmail())) {
            return false;
        } else {
            return true;
        }
    }

    public boolean hasLocation() {
        if (stadium.getLatitude() == 0 && stadium.getLongitude() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public String getAddress() {
        String address = null;
        if (stadium.getStadiumCity() != null) {
            String cityName = stadium.getStadiumCity().getName();
            if (!Utils.isNullOrEmpty(cityName)) {
                address = cityName.trim();
            }

            if (stadium.getStadiumCity().getCountry() != null) {
                String countryName = stadium.getStadiumCity().getCountry().getName();

                if (!Utils.isNullOrEmpty(countryName)) {
                    if (address != null) {
                        address += " - " + countryName.trim();
                    } else {
                        address = countryName.trim();
                    }
                }
            }
        }

        return address;
    }
}
