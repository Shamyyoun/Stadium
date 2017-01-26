
package com.stadium.app.models.payloads;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParsePayload {

    @SerializedName("alert")
    @Expose
    private String alert;
    @SerializedName("parsePushId")
    @Expose
    private String parsePushId;
    @SerializedName("push_hash")
    @Expose
    private String pushHash;

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getParsePushId() {
        return parsePushId;
    }

    public void setParsePushId(String parsePushId) {
        this.parsePushId = parsePushId;
    }

    public String getPushHash() {
        return pushHash;
    }

    public void setPushHash(String pushHash) {
        this.pushHash = pushHash;
    }

}
