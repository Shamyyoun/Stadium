package com.stadium.app.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Reservation implements Serializable {
    public static final String DATE_FORMAT = "yyyy/MM/dd";
    public static final String TIME_FORMAT = "hh:mm:ss";

    @SerializedName("Id")
    @Expose
    private int id;
    @SerializedName("IntrvalNum")
    @Expose
    private int intrvalNum;
    @SerializedName("Price")
    @Expose
    private int price;
    @SerializedName("absent")
    @Expose
    private Object absent;
    @SerializedName("confirm")
    @Expose
    private Object confirm;
    @SerializedName("counter")
    @Expose
    private boolean counter;
    @SerializedName("customerName")
    @Expose
    private Object customerName;
    @SerializedName("customerPhone")
    @Expose
    private Object customerPhone;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("playerCounter")
    @Expose
    private int playerCounter;
    @SerializedName("playerId")
    @Expose
    private Object playerId;
    @SerializedName("reservationFiled")
    @Expose
    private Field field;
    @SerializedName("reservationStadium")
    @Expose
    private Stadium reservationStadium;
    @SerializedName("reservationTeam")
    @Expose
    private Team reservationTeam;
    @SerializedName("timeEnd")
    @Expose
    private String timeEnd;
    @SerializedName("timeStart")
    @Expose
    private String timeStart;

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
     * @return The intrvalNum
     */
    public int getIntrvalNum() {
        return intrvalNum;
    }

    /**
     * @param intrvalNum The IntrvalNum
     */
    public void setIntrvalNum(int intrvalNum) {
        this.intrvalNum = intrvalNum;
    }

    /**
     * @return The price
     */
    public int getPrice() {
        return price;
    }

    /**
     * @param price The Price
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * @return The absent
     */
    public Object getAbsent() {
        return absent;
    }

    /**
     * @param absent The absent
     */
    public void setAbsent(Object absent) {
        this.absent = absent;
    }

    /**
     * @return The confirm
     */
    public Object getConfirm() {
        return confirm;
    }

    /**
     * @param confirm The confirm
     */
    public void setConfirm(Object confirm) {
        this.confirm = confirm;
    }

    /**
     * @return The counter
     */
    public boolean isCounter() {
        return counter;
    }

    /**
     * @param counter The counter
     */
    public void setCounter(boolean counter) {
        this.counter = counter;
    }

    /**
     * @return The customerName
     */
    public Object getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName The customerName
     */
    public void setCustomerName(Object customerName) {
        this.customerName = customerName;
    }

    /**
     * @return The customerPhone
     */
    public Object getCustomerPhone() {
        return customerPhone;
    }

    /**
     * @param customerPhone The customerPhone
     */
    public void setCustomerPhone(Object customerPhone) {
        this.customerPhone = customerPhone;
    }

    /**
     * @return The date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date The date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return The playerCounter
     */
    public int getPlayerCounter() {
        return playerCounter;
    }

    /**
     * @param playerCounter The playerCounter
     */
    public void setPlayerCounter(int playerCounter) {
        this.playerCounter = playerCounter;
    }

    /**
     * @return The playerId
     */
    public Object getPlayerId() {
        return playerId;
    }

    /**
     * @param playerId The playerId
     */
    public void setPlayerId(Object playerId) {
        this.playerId = playerId;
    }

    /**
     * @return The field
     */
    public Field getField() {
        return field;
    }

    /**
     * @param field The field
     */
    public void setField(Field field) {
        this.field = field;
    }

    /**
     * @return The reservationStadium
     */
    public Stadium getReservationStadium() {
        return reservationStadium;
    }

    /**
     * @param reservationStadium The reservationStadium
     */
    public void setReservationStadium(Stadium reservationStadium) {
        this.reservationStadium = reservationStadium;
    }

    /**
     * @return The reservationTeam
     */
    public Team getReservationTeam() {
        return reservationTeam;
    }

    /**
     * @param reservationTeam The reservationTeam
     */
    public void setReservationTeam(Team reservationTeam) {
        this.reservationTeam = reservationTeam;
    }

    /**
     * @return The timeEnd
     */
    public String getTimeEnd() {
        return timeEnd;
    }

    /**
     * @param timeEnd The timeEnd
     */
    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    /**
     * @return The timeStart
     */
    public String getTimeStart() {
        return timeStart;
    }

    /**
     * @param timeStart The timeStart
     */
    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

}
