package com.stormnology.stadium.controllers;

import com.stormnology.stadium.models.entities.Field;

import java.util.List;

/**
 * Created by Shamyyoun on 9/3/16.
 */
public class FieldController {

    public int getItemPosition(List<Field> fields, int fieldId) {
        for (int i = 0; i < fields.size(); i++) {
            if (fieldId == fields.get(i).getId()) {
                return i;
            }
        }

        return -1;
    }
}
