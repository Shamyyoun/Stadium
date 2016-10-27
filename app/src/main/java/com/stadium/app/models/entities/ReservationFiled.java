package com.stadium.app.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReservationFiled implements Serializable {

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
    private Object fieldSize;
    @SerializedName("fieldSpace")
    @Expose
    private int fieldSpace;
    @SerializedName("fieldWidth")
    @Expose
    private int fieldWidth;
    @SerializedName("playerCapcity")
    @Expose
    private int playerCapcity;
    @SerializedName("price")
    @Expose
    private int price;

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
    public Object getFieldSize() {
        return fieldSize;
    }

    /**
     * @param fieldSize The fieldSize
     */
    public void setFieldSize(Object fieldSize) {
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
     * @return The playerCapcity
     */
    public int getPlayerCapcity() {
        return playerCapcity;
    }

    /**
     * @param playerCapcity The playerCapcity
     */
    public void setPlayerCapcity(int playerCapcity) {
        this.playerCapcity = playerCapcity;
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

}
