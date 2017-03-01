package com.stormnology.stadium.controllers;

import com.stormnology.stadium.models.entities.Stadium;
import com.stormnology.stadium.utils.Utils;

import java.util.List;

/**
 * Created by Shamyyoun on 9/4/16.
 */
public class StadiumController {
    public boolean hasContactInfo(Stadium stadium) {
        if (Utils.isNullOrEmpty(stadium.getPhoneNumber()) && Utils.isNullOrEmpty(stadium.getEmail())) {
            return false;
        } else {
            return true;
        }
    }

    public boolean hasLocation(Stadium stadium) {
        if (stadium.getLatitude() == 0 && stadium.getLongitude() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public String getAddress(Stadium stadium) {
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

    public int getItemPosition(List<Stadium> stadiums, int itemId) {
        for (int i = 0; i < stadiums.size(); i++) {
            if (stadiums.get(i).getId() == itemId) {
                return i;
            }
        }

        return -1;
    }

    public String getCityName(Stadium stadium) {
        if (stadium.getStadiumCity() == null) {
            return null;
        } else {
            return stadium.getStadiumCity().getName().trim();
        }
    }
}
