
package com.stadium.app.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attendant {

    @SerializedName("Type")
    @Expose
    private int type;
    @SerializedName("player")
    @Expose
    private User player;
    @SerializedName("res")
    @Expose
    private Object res;
    @SerializedName("typeMessage")
    @Expose
    private String typeMessage;

    /**
     * @return The type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type The Type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return The player
     */
    public User getPlayer() {
        return player;
    }

    /**
     * @param player The player
     */
    public void setPlayer(User player) {
        this.player = player;
    }

    /**
     * @return The res
     */
    public Object getRes() {
        return res;
    }

    /**
     * @param res The res
     */
    public void setRes(Object res) {
        this.res = res;
    }

    /**
     * @return The typeMessage
     */
    public String getTypeMessage() {
        return typeMessage;
    }

    /**
     * @param typeMessage The typeMessage
     */
    public void setTypeMessage(String typeMessage) {
        this.typeMessage = typeMessage;
    }

}
