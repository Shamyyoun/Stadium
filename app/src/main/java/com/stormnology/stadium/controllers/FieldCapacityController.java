package com.stormnology.stadium.controllers;

import com.stormnology.stadium.models.entities.FieldCapacity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 11/2/16.
 */

public class FieldCapacityController {

    public List<FieldCapacity> createList(String[] fieldsArr) {
        List<FieldCapacity> fields = new ArrayList<>(fieldsArr.length);
        for (int i = 0; i < fieldsArr.length; i++) {
            FieldCapacity fieldCapacity = new FieldCapacity();
            fieldCapacity.setName(fieldsArr[i]);
            fields.add(fieldCapacity);
        }

        return fields;
    }

    public int getItemPosition(List<FieldCapacity> fields, String fieldCapacity) {
        if (fieldCapacity == null) {
            return -1;
        }

        for (int i = 0; i < fields.size(); i++) {
            if (fieldCapacity.equals(fields.get(i).getName())) {
                return i;
            }
        }

        return -1;
    }

    public List<FieldCapacity> addDefaultItem(List<FieldCapacity> fields, String defaultItemName) {
        if (fields == null) {
            fields = new ArrayList<>();
        }

        // add the default item
        FieldCapacity fieldCapacity = new FieldCapacity();
        fieldCapacity.setName(defaultItemName);
        fields.add(0, fieldCapacity);

        return fields;
    }
}
