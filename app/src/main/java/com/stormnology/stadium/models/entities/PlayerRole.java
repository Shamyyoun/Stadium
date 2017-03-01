package com.stormnology.stadium.models.entities;

/**
 * Created by Shamyyoun on 10/26/16.
 */

public class PlayerRole {
    private String _char;
    private int backgroundResId;

    public PlayerRole(String _char, int backgroundResId) {
        this._char = _char;
        this.backgroundResId = backgroundResId;
    }

    public String getChar() {
        return _char;
    }

    public void setChar(String _char) {
        this._char = _char;
    }

    public int getBackgroundResId() {
        return backgroundResId;
    }

    public void setBackgroundResId(int backgroundResId) {
        this.backgroundResId = backgroundResId;
    }
}
