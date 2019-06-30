
package com.smartdev.fishfarm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogModel {

    @SerializedName("ID")
    @Expose
    private String iD;
    @SerializedName("Event")
    @Expose
    private String event;
    @SerializedName("PH")
    @Expose
    private String pH;
    @SerializedName("Temperature")
    @Expose
    private String temperature;
    @SerializedName("Sanility")
    @Expose
    private String sanility;
    @SerializedName("DissolveOxy")
    @Expose
    private String dissolveOxy;

    public String getID() {
        return iD;
    }

    public void setID(String iD) {
        this.iD = iD;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getPH() {
        return pH;
    }

    public void setPH(String pH) {
        this.pH = pH;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getSanility() {
        return sanility;
    }

    public void setSanility(String sanility) {
        this.sanility = sanility;
    }

    public String getDissolveOxy() {
        return dissolveOxy;
    }

    public void setDissolveOxy(String dissolveOxy) {
        this.dissolveOxy = dissolveOxy;
    }

}
