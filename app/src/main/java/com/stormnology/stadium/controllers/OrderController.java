package com.stormnology.stadium.controllers;

import android.content.Context;

import com.stormnology.stadium.R;
import com.stormnology.stadium.models.entities.OrderCriteria;
import com.stormnology.stadium.models.entities.Stadium;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Shamyyoun on 11/3/16.
 */

public class OrderController {

    public List<OrderCriteria> getPlayersCriterias(Context context) {
        List<OrderCriteria> orderCriterias = new ArrayList<>();

        OrderCriteria orderCriteria1 = new OrderCriteria();
        orderCriteria1.setType(OrderCriteria.TYPE_NAME);
        orderCriteria1.setName(context.getString(R.string.player_name));
        orderCriterias.add(orderCriteria1);

        OrderCriteria orderCriteria2 = new OrderCriteria();
        orderCriteria2.setType(OrderCriteria.TYPE_RATE);
        orderCriteria2.setName(context.getString(R.string.player_rating));
        orderCriterias.add(orderCriteria2);

        return orderCriterias;
    }

    public int getItemPosition(List<OrderCriteria> orderCriterias, int itemType) {
        for (int i = 0; i < orderCriterias.size(); i++) {
            if (orderCriterias.get(i).getType() == itemType) {
                return i;
            }
        }

        return -1;
    }

    public List<OrderCriteria> getStadiumsCriterias(Context context) {
        List<OrderCriteria> orderCriterias = new ArrayList<>();

        OrderCriteria orderCriteria1 = new OrderCriteria();
        orderCriteria1.setType(OrderCriteria.TYPE_DEFAULT);
        orderCriteria1.setName(context.getString(R.string._default));
        orderCriterias.add(orderCriteria1);

        OrderCriteria orderCriteria2 = new OrderCriteria();
        orderCriteria2.setType(OrderCriteria.TYPE_NAME);
        orderCriteria2.setName(context.getString(R.string.stadium_name));
        orderCriterias.add(orderCriteria2);

        OrderCriteria orderCriteria3 = new OrderCriteria();
        orderCriteria3.setType(OrderCriteria.TYPE_LOCATION);
        orderCriteria3.setName(context.getString(R.string.location));
        orderCriterias.add(orderCriteria3);

        return orderCriterias;
    }

    public void orderStadiums(List<Stadium> stadiums, final int criteriaType) {
        Comparator<Stadium> comparator = new Comparator<Stadium>() {
            @Override
            public int compare(Stadium lhs, Stadium rhs) {
                if (criteriaType == OrderCriteria.TYPE_NAME) {
                    return lhs.getName().compareToIgnoreCase(rhs.getName());
                } else {
                    return 0;
                }
            }
        };

        Collections.sort(stadiums, comparator);
    }
}
