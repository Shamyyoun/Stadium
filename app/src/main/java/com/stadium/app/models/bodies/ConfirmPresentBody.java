package com.stadium.app.models.bodies;

/**
 * Created by Shamyyoun on 10/23/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stadium.app.models.entities.User;

public class ConfirmPresentBody {
    @SerializedName("player")
    @Expose
    private User player;
    @SerializedName("res")
    @Expose
    private Res res;
    @SerializedName("Type")
    @Expose
    private int type;

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
    public Res getRes() {
        return res;
    }

    /**
     * @param res The res
     */
    public void setRes(Res res) {
        this.res = res;
    }

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

    // -------------------- Res Class --------------------
    public static class Res {

        @SerializedName("Id")
        @Expose
        private int id;

        /**
         * @return The id
         */
        public int getId() {
            return id;
        }

        /**
         * @param id The Id
         */
        public void setId(int id) {
            this.id = id;
        }
    }
}
