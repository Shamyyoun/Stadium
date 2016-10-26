package com.stadium.app.models.entities;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stadium.app.models.Checkable;
import com.stadium.app.models.responses.ServerResponse;

public class Team extends ServerResponse implements Checkable {

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
    private User asstent;
    @SerializedName("blockTimes")
    @Expose
    private int blockTimes;
    @SerializedName("captin")
    @Expose
    private User captain;
    @SerializedName("descrption")
    @Expose
    private String description;
    @SerializedName("imageLink")
    @Expose
    private String imageLink;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("numberOfPlyers")
    @Expose
    private int numberOfPlayers;
    @SerializedName("players")
    @Expose
    private Object players;
    @SerializedName("preferStadiumId")
    @Expose
    private int preferStadiumId;
    @SerializedName("preferStadiumName")
    @Expose
    private String preferStadiumName;
    @SerializedName("rate")
    @Expose
    private double rate;
    @SerializedName("rateCounter")
    @Expose
    private int rateCounter;
    @SerializedName("totalRes")
    @Expose
    private int totalRes;
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
    public User getAsstent() {
        return asstent;
    }

    /**
     * @param asstent The asstent
     */
    public void setAsstent(User asstent) {
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
     * @return The captain
     */
    public User getCaptain() {
        return captain;
    }

    /**
     * @param captain The captain
     */
    public void setCaptain(User captain) {
        this.captain = captain;
    }

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
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
     * @return The numberOfPlayers
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     * @param numberOfPlayers The numberOfPlayers
     */
    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
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
     * @param preferStadiumName The preferStadiumName
     */
    public void setPreferStadiumName(String preferStadiumName) {
        this.preferStadiumName = preferStadiumName;
    }

    /**
     * @return The preferStadiumName
     */
    public String getPreferStadiumName() {
        return preferStadiumName;
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
    public double getRate() {
        return rate;
    }

    /**
     * @param rate The rate
     */
    public void setRate(double rate) {
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
        return name;
    }
}
