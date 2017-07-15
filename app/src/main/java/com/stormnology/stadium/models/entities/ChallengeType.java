
package com.stormnology.stadium.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChallengeType {

    @SerializedName("Id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
