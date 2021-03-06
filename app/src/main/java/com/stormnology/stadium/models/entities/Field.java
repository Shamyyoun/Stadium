package com.stormnology.stadium.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stormnology.stadium.models.Checkable;

import java.io.Serializable;

public class Field implements Serializable, Checkable {

    @SerializedName("Id")
    @Expose
    private int id;
    @SerializedName("fieldLong")
    @Expose
    private int fieldLong;
    @SerializedName("fieldNumber")
    @Expose
    private String fieldNumber;
    @SerializedName("fieldSize")
    @Expose
    private String fieldSize;
    @SerializedName("fieldSpace")
    @Expose
    private int fieldSpace;
    @SerializedName("fieldWidth")
    @Expose
    private int fieldWidth;
    @SerializedName("playerCapcity")
    @Expose
    private int playerCapacity;
    @SerializedName("price")
    @Expose
    private int price;
    private boolean checked;

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

    /**
     * @return The fieldLong
     */
    public int getFieldLong() {
        return fieldLong;
    }

    /**
     * @param fieldLong The fieldLong
     */
    public void setFieldLong(int fieldLong) {
        this.fieldLong = fieldLong;
    }

    /**
     * @return The fieldNumber
     */
    public String getFieldNumber() {
        return fieldNumber;
    }

    /**
     * @param fieldNumber The fieldNumber
     */
    public void setFieldNumber(String fieldNumber) {
        this.fieldNumber = fieldNumber;
    }

    /**
     * @return The fieldSize
     */
    public String getFieldSize() {
        return fieldSize;
    }

    /**
     * @param fieldSize The fieldSize
     */
    public void setFieldSize(String fieldSize) {
        this.fieldSize = fieldSize;
    }

    /**
     * @return The fieldSpace
     */
    public int getFieldSpace() {
        return fieldSpace;
    }

    /**
     * @param fieldSpace The fieldSpace
     */
    public void setFieldSpace(int fieldSpace) {
        this.fieldSpace = fieldSpace;
    }

    /**
     * @return The fieldWidth
     */
    public int getFieldWidth() {
        return fieldWidth;
    }

    /**
     * @param fieldWidth The fieldWidth
     */
    public void setFieldWidth(int fieldWidth) {
        this.fieldWidth = fieldWidth;
    }

    /**
     * @return The playerCapacity
     */
    public int getPlayerCapacity() {
        return playerCapacity;
    }

    /**
     * @param playerCapacity The playerCapacity
     */
    public void setPlayerCapacity(int playerCapacity) {
        this.playerCapacity = playerCapacity;
    }

    /**
     * @return The price
     */
    public int getPrice() {
        return price;
    }

    /**
     * @param price The price
     */
    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public String toString() {
        return fieldNumber;
    }
}
