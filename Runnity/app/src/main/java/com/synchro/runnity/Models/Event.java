package com.synchro.runnity.Models;

import io.realm.RealmObject;

/**
 * Created by Shirco on 01/03/2016.
 */
public class Event {
    private Float src_latitude;
    private Float src_longitude;
    private Float dest_latitude;
    private Float dest_longitude;
    private String name;
    private String description;
    private String city;
    private String user_id;
    private Float distance;
    private Boolean private_status;
    private String id;
    private String start_date;
    private Boolean is_register;

//    private Coordinate coordinates[];

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

//    public Coordinate[] getCoordinates() {
//        return coordinates;
//    }
//
//    public void setCoordinates(Coordinate[] coordinates) {
//        this.coordinates = coordinates;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Boolean get_private() {
        return private_status;
    }

    public void set_private(Boolean _private) {
        this.private_status = _private;
    }

    public String get_id() {
        return id;
    }

    public void set_id(String _id) {
        this.id = _id;
    }

    public String getDate() {
        return start_date;
    }

    public void setDate(String date) {
        this.start_date = date;
    }

    public Boolean getPrivate_status() {
        return private_status;
    }

    public void setPrivate_status(Boolean private_status) {
        this.private_status = private_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public Boolean getIs_register() {
        return is_register;
    }

    public void setIs_register(Boolean is_register) {
        this.is_register = is_register;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
