package com.stadium.app.controllers;

import com.stadium.app.models.entities.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 11/2/16.
 */

public class PositionController {

    public List<Position> createList(String[] positionsArr) {
        List<Position> positions = new ArrayList<>(positionsArr.length);
        for (int i = 0; i < positionsArr.length; i++) {
            Position position = new Position();
            position.setName(positionsArr[i]);
            positions.add(position);
        }

        return positions;
    }

    public int getItemPosition(List<Position> positions, String position) {
        if (position == null) {
            return -1;
        }

        for (int i = 0; i < positions.size(); i++) {
            if (position.equals(positions.get(i).getName())) {
                return i;
            }
        }

        return -1;
    }

    public List<Position> addDefaultItem(List<Position> positions, String defaultItemName) {
        if (positions == null) {
            positions = new ArrayList<>();
        }

        // add the default item
        Position position = new Position();
        position.setName(defaultItemName);
        positions.add(0, position);

        return positions;
    }
}
