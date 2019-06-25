package com.smartdev.fishfarm.Model;
import java.util.Date;

public class DataModel {

    private String PHdata;

    private String Tempdata;

    private String Salinitydata;

    private String DissolvedOXdata;
    private String mDate;

    public String getPHData() {
        return PHdata;
    }

    public void setPHData(String phdata) {
        this.PHdata = phdata;
    }
    public String getTempData() {
        return Tempdata;
    }
    public void setTempData(String Tempdata) {
        this.Tempdata = Tempdata;
    }
    public void setSalinitydata(String Salinitydata) {
        this.Salinitydata = Salinitydata;
    }
    public String getSalinitydata() {
        return Salinitydata;
    }
    public void setDissolvedOXdata(String DissolvedOXdata) {
        this.DissolvedOXdata = DissolvedOXdata;
    }
    public String getDissolvedOXdata() {
        return DissolvedOXdata;
    }


    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
       this.mDate = date;
    }
}
