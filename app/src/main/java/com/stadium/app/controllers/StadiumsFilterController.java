package com.stadium.app.controllers;

import com.stadium.app.models.entities.StadiumsFilter;

/**
 * Created by Shamyyoun on 11/12/16.
 */

public class StadiumsFilterController {

    public boolean hasFilters(StadiumsFilter filter) {
        if (filter.getCity() != null) {
            return true;
        }

        if (filter.getName() != null) {
            return true;
        }

        if (filter.getFieldCapacity() != null) {
            return true;
        }

        if (filter.getDate() != null) {
            return true;
        }

        if (filter.getTimeStart() != null && filter.getTimeEnd() != null) {
            return true;
        }

        return false;
    }
}
