package com.synchro.runnity.Models;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Shirco on 01/03/2016.
 */
public class Coordinate extends RealmObject  {
    private Float latitude;
    private Float longitude;
    private String timestamp;

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getStarted_at() {
        return timestamp;
    }

    public void setStarted_at(String started_at) {
        this.timestamp = started_at;
    }
}
