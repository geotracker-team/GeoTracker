package com.juanjo.udl.geotracker;

import java.util.Date;

/**
 * Created by David on 05/11/2017.
 */

public class Register {
    private String mDate;
    private String mUser;
    private Integer mProjectId;
    private String mProjectName;
    private Double mLatitude;
    private Double mLongitude;
    private String mDescription;

    public Register(String date, String user, String projectName, Double latitude, Double longitude, String description) {
        mDate = date;
        mUser = user;
        mProjectName = projectName;
        mLatitude = latitude;
        mLongitude = longitude;
        mDescription = description;
    }


    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public String getUser() {
        return mUser;
    }

    public void setUser(String user) {
        this.mUser = user;
    }

    public Integer getProjectId() {
        return mProjectId;
    }

    public void setProjectId(Integer projectId) {
        this.mProjectId = projectId;
    }

    public String getProjectName() {
        return mProjectName;
    }

    public void setProjectName(String projectName) {
        this.mProjectName = projectName;
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(Double latitude) {
        this.mLatitude = latitude;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(Double longitude) {
        this.mLongitude = longitude;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }
}
