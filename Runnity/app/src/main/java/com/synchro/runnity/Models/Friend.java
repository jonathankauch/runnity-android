package com.synchro.runnity.Models;

import io.realm.RealmObject;

/**
 * Created by Shirco on 24/02/2016.
 */
public class Friend {
    private String firstname;
    private String lastname;
    private String authentication_token;
    private String logged_at;
    private String email;
    private String id;
    private String requests_id;
    private String average_speed;
    private String kilometer_done;
    private String total_calorie;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getToken() {
        return authentication_token;
    }

    public void setToken(String token) {
        this.authentication_token = token;
    }

    public String getLogged_at() {
        return logged_at;
    }

    public void setLogged_at(String logged_at) {
        this.logged_at = logged_at;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequests_id() {
        return requests_id;
    }

    public void setRequests_id(String requests_id) {
        this.requests_id = requests_id;
    }

    public String getAverage_speed() {
        return average_speed;
    }

    public void setAverage_speed(String average_speed) {
        this.average_speed = average_speed;
    }

    public String getKilometer_done() {
        return kilometer_done;
    }

    public void setKilometer_done(String kilometer_done) {
        this.kilometer_done = kilometer_done;
    }

    public String getTotal_calorie() {
        return total_calorie;
    }

    public void setTotal_calorie(String total_calorie) {
        this.total_calorie = total_calorie;
    }
}
