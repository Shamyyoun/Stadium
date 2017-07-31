package com.stormnology.stadium.controllers;

import com.stormnology.stadium.models.entities.ChallengeType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 9/3/16.
 */
public class ChallengeTypeController {

    public int getItemPosition(List<ChallengeType> types, int typeId) {
        for (int i = 0; i < types.size(); i++) {
            if (typeId == types.get(i).getId()) {
                return i;
            }
        }

        return -1;
    }
}
