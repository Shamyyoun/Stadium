package com.stadium.app.models.entities;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stadium.app.models.responses.ServerResponse;

public class Team extends ServerResponse {

    @SerializedName("Id")
    @Expose
    private int id;
    @SerializedName("TeamImage")
    @Expose
    private Image teamImage;
    @SerializedName("absentRes")
    @Expose
    private int absentRes;
    @SerializedName("asstent")
    @Expose
    private Object asstent;
    @SerializedName("blockTimes")
    @Expose
    private int blockTimes;
    @SerializedName("captin")
    @Expose
    private User captin;
    @SerializedName("descrption")
    @Expose
    private String descrption;
    @SerializedName("imageLink")
    @Expose
    private String imageLink;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("numberOfPlyers")
    @Expose
    private int numberOfPlyers;
    @SerializedName("players")
    @Expose
    private Object players;
    @SerializedName("preferStadiumId")
    @Expose
    private int preferStadiumId;
    @SerializedName("rate")
    @Expose
    private int rate;
    @SerializedName("rateCounter")
    @Expose
    private int rateCounter;
    @SerializedName("totalRes")
    @Expose
    private int totalRes;

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
     * @return The teamImage
     */
    public Image getTeamImage() {
        return teamImage;
    }

    /**
     * @param teamImage The TeamImage
     */
    public void setTeamImage(Image teamImage) {
        this.teamImage = teamImage;
    }

    /**
     * @return The absentRes
     */
    public int getAbsentRes() {
        return absentRes;
    }

    /**
     * @param absentRes The absentRes
     */
    public void setAbsentRes(int absentRes) {
        this.absentRes = absentRes;
    }

    /**
     * @return The asstent
     */
    public Object getAsstent() {
        return asstent;
    }

    /**
     * @param asstent The asstent
     */
    public void setAsstent(Object asstent) {
        this.asstent = asstent;
    }

    /**
     * @return The blockTimes
     */
    public int getBlockTimes() {
        return blockTimes;
    }

    /**
     * @param blockTimes The blockTimes
     */
    public void setBlockTimes(int blockTimes) {
        this.blockTimes = blockTimes;
    }

    /**
     * @return The captin
     */
    public User getCaptin() {
        return captin;
    }

    /**
     * @param captin The captin
     */
    public void setCaptin(User captin) {
        this.captin = captin;
    }

    /**
     * @return The descrption
     */
    public String getDescrption() {
        return descrption;
    }

    /**
     * @param descrption The descrption
     */
    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    /**
     * @return The imageLink
     */
    public String getImageLink() {
        return imageLink;
    }

    /**
     * @param imageLink The imageLink
     */
    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The numberOfPlyers
     */
    public int getNumberOfPlyers() {
        return numberOfPlyers;
    }

    /**
     * @param numberOfPlyers The numberOfPlyers
     */
    public void setNumberOfPlyers(int numberOfPlyers) {
        this.numberOfPlyers = numberOfPlyers;
    }

    /**
     * @return The players
     */
    public Object getPlayers() {
        return players;
    }

    /**
     * @param players The players
     */
    public void setPlayers(Object players) {
        this.players = players;
    }

    /**
     * @return The preferStadiumId
     */
    public int getPreferStadiumId() {
        return preferStadiumId;
    }

    /**
     * @param preferStadiumId The preferStadiumId
     */
    public void setPreferStadiumId(int preferStadiumId) {
        this.preferStadiumId = preferStadiumId;
    }

    /**
     * @return The rate
     */
    public int getRate() {
        return rate;
    }

    /**
     * @param rate The rate
     */
    public void setRate(int rate) {
        this.rate = rate;
    }

    /**
     * @return The rateCounter
     */
    public int getRateCounter() {
        return rateCounter;
    }

    /**
     * @param rateCounter The rateCounter
     */
    public void setRateCounter(int rateCounter) {
        this.rateCounter = rateCounter;
    }

    /**
     * @return The totalRes
     */
    public int getTotalRes() {
        return totalRes;
    }

    /**
     * @param totalRes The totalRes
     */
    public void setTotalRes(int totalRes) {
        this.totalRes = totalRes;
    }

}
