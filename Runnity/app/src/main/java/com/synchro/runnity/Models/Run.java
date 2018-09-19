package com.synchro.runnity.Models;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Shirco on 01/03/2016.
 */
public class Run {
    private String id;
    private String user_id;
    private Float src_latitude;
    private Float src_longitude;
    private Float dest_latitude;
    private Float dest_longitude;
    private String started_at;
    private String name;
    private String dest_name;
    private Float total_distance;
    private String total_time;
    private Float max_speed;
    private String coordinates;
    private boolean is_spot;
    private Coordinate real_coordinates[];

    public int getLikes() {
        return 0;
    }

    public boolean is_spot() {
        return is_spot;
    }

    public void setIs_spot(boolean is_spot) {
        this.is_spot = is_spot;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Float getTotal_distance() {
        return total_distance;
    }

    public void setTotal_distance(Float total_distance) {
        this.total_distance = total_distance;
    }

    public String getTotal_time() {
        return total_time;
    }

    public void setTotal_time(String total_time) {
        this.total_time = total_time;
    }

    public Float getMax_speed() {
        return max_speed;
    }

    public void setMax_speed(Float max_speed) {
        this.max_speed = max_speed;
    }

    public Float getSrc_latitude() {
        return src_latitude;
    }

    public void setSrc_latitude(Float src_latitude) {
        this.src_latitude = src_latitude;
    }

    public Float getSrc_longitude() {
        return src_longitude;
    }

    public void setSrc_longitude(Float src_longitude) {
        this.src_longitude = src_longitude;
    }

    public Float getDest_latitude() {
        return dest_latitude;
    }

    public void setDest_latitude(Float dest_latitude) {
        this.dest_latitude = dest_latitude;
    }

    public Float getDest_longitude() {
        return dest_longitude;
    }

    public void setDest_longitude(Float dest_longitude) {
        this.dest_longitude = dest_longitude;
    }

    public String getStarted_at() {
        return started_at;
    }

    public void setStarted_at(String started_at) {
        this.started_at = started_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDest_name() {
        return dest_name;
    }

    public void setDest_name(String dest_name) {
        this.dest_name = dest_name;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String  coordinates) {
        this.coordinates = coordinates;
    }

    public Coordinate[] getRealCoordinates() {
        return real_coordinates;
    }

    public void setRealCoordinates(Coordinate[] realCoordinates) {
        this.real_coordinates = realCoordinates;
    }
}
